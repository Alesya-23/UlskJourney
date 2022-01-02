package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.MapFragmentBinding
import com.ulskjourney.ulskjourney.model.database.MarkStorage
import com.ulskjourney.ulskjourney.model.models.Mark
import com.ulskjourney.ulskjourney.utils.Report
import com.ulskjourney.ulskjourney.view.activities.AuthActivity
import com.ulskjourney.ulskjourney.view.listeners.YandexMapObjectTapListener
import com.ulskjourney.ulskjourney.viewModel.UserViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val MAPKIT_API_KEY = "15773253-c0df-40c7-86e5-66e2a9ed4f3f"

class MapFragment : Fragment(R.layout.map_fragment) {
    private lateinit var mapBinding: MapFragmentBinding
    private val TARGET_LOCATION = Point(54.352550, 48.389436)
    private var listener: YandexMapObjectTapListener = YandexMapObjectTapListener()
    private var listMarks: ArrayList<Mark> = ArrayList<Mark>()
    private var userId: Int = -1
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapBinding = MapFragmentBinding.bind(view)
        mapBinding.mapview.map
                ?.move(
                        CameraPosition(TARGET_LOCATION, 15.0f, 0.0f, 0.0f),
                        Animation(Animation.Type.SMOOTH, 0f), null
                )
        userViewModel.idUser.observe(viewLifecycleOwner, Observer {
            userId = userViewModel.getItem()!!
        })
        loadListMarks()
        buttonCreateNewMark()
        actionButtonMenu()
        updateMapView()
        loadMarksFromFirebase()
        activity?.title = "Карта"
    }

    private fun addMarksInMap() {
        if (listMarks.isNotEmpty())
            for (items: Mark in listMarks) {
                createMapObject(Point(items.latitude, items.longitude))
            }
    }

    private fun createMapObject(point: Point): PlacemarkMapObject? {
        val mark: PlacemarkMapObject? = mapBinding.mapview.map?.mapObjects?.addPlacemark(point)
        mark?.opacity = 0.5f
        mark?.setIcon(ImageProvider.fromResource(activity, R.drawable.mark))
        activity?.applicationContext?.let { listener.getContext(it) }
        mark?.addTapListener(listener)
        return mark
    }

    private fun loadListMarks() {
        val markStorage = activity?.applicationContext?.let { MarkStorage(it) }
        markStorage?.open()
        val listMark = markStorage?.getFullList()
        if (listMark == null) {
            Toast.makeText(activity?.applicationContext, "Добавьте метки", Toast.LENGTH_SHORT)
                    .show()
        } else {
            listMarks = listMark as ArrayList<Mark>
            addMarksInMap()
        }
        markStorage?.close()
        addMarksInMap()
    }

    private fun loadMarksFromFirebase() {
        var listMark = ArrayList<Mark>()
        CoroutineScope(Dispatchers.Default).launch {
            listMark = (activity as AuthActivity).getFirebasePostService().getListMarks()
            (activity as AuthActivity).getFirebasePostService().getListUsers()
            withContext(Dispatchers.Main) {
            }
        }

        val markStorage = activity?.applicationContext?.let { MarkStorage(it) }
        markStorage?.open()
        val listMarkDB = markStorage?.getFullList()
        if (listMark.isEmpty()) {
            Toast.makeText(activity?.applicationContext, "Включите интернет для синхонизации", Toast.LENGTH_SHORT)
                    .show()
        } else {
            //проход по бд
            if (listMarkDB != null) {
                for (item in listMark) {
                    val check = listMarkDB.find {
                        it?.latitude == item.latitude
                                && it.longitude == item.longitude
                    }
                    if (check == null)
                        markStorage.insert(item)
                }
            }
        }
        markStorage?.close()

    }

    private fun updateMapView() {
        mapBinding.updateMap.setOnClickListener {
            loadListMarks()
            Toast.makeText(activity?.applicationContext, "Пожалуйста подождите", Toast.LENGTH_SHORT)
                    .show()
            loadMarksFromFirebase()
            addMarksInMap()
        }
    }

    private fun buttonCreateNewMark() {
        mapBinding.buttonCreateNewMark.setOnClickListener {
            val addNewMarkFragment = AddNewMarkFragment.newInstance(userId)
            parentFragmentManager.beginTransaction()
                    .addToBackStack(addNewMarkFragment.tag)
                    .replace(R.id.auth_activity, addNewMarkFragment)
                    .commit()
        }
    }

    private fun actionButtonMenu() {
        with(mapBinding) {
            buttonProfile.setOnClickListener {
                val profileFragment = ProfileFragment.newInstance(userId)
                parentFragmentManager.beginTransaction()
                        .addToBackStack(profileFragment.tag)
                        .replace(R.id.auth_activity, profileFragment)
                        .commit()
            }
            buttonListMark.setOnClickListener {
                val listMarkFragment = ListMarkFragment()
                parentFragmentManager.beginTransaction()
                        .addToBackStack(listMarkFragment.tag)
                        .replace(R.id.auth_activity, listMarkFragment)
                        .commit()
            }
            buttonDownloadDataMarks.setOnClickListener {
                var report = Report()
                report.generatePdf(listMarks)
                Toast.makeText(
                        activity?.applicationContext,
                        "Готово",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapBinding.mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapBinding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }
}
