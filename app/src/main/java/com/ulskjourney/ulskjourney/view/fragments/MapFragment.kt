package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.MapFragmentBinding
import com.ulskjourney.ulskjourney.model.models.Mark
import com.ulskjourney.ulskjourney.view.listeners.YandexMapObjectTapListener
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

private const val MAPKIT_API_KEY = "15773253-c0df-40c7-86e5-66e2a9ed4f3f"

class MapFragment : Fragment(R.layout.map_fragment) {
    private lateinit var mapBinding: MapFragmentBinding
    private val TARGET_LOCATION = Point(54.352550, 48.389436)
    private var listener: YandexMapObjectTapListener = YandexMapObjectTapListener()
    private var listMarks: ArrayList<Mark> = ArrayList<Mark>()

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
                        CameraPosition(TARGET_LOCATION, 11.0f, 0.0f, 0.0f),
                        Animation(Animation.Type.SMOOTH, 0f), null
                )
        createMapObject(Point(54.352550, 48.389436))
        buttonCreateNewMark()
        actionButtonMenu()
        loadListMarks()
        activity?.title = "Карта"
    }

    private fun addMarksInMap() {
        if (listMarks.isNotEmpty())
            for (items: Mark in listMarks) {
                createMapObject(Point(items.latitude, items.longitude))
            }
    }

    private fun createMapObject(point: Point) {
        var mark: PlacemarkMapObject? = mapBinding.mapview?.map?.mapObjects?.addPlacemark(point)
        mark?.opacity = 0.5f
        mark?.setIcon(ImageProvider.fromResource(activity, R.drawable.mark))
        activity?.applicationContext?.let { listener.getContext(it) }
        mark?.addTapListener(listener)
    }

    private fun buttonCreateNewMark() {
        mapBinding.buttonCreateNewMark.setOnClickListener {
            val addNewMarkFragment = AddNewMarkFragment()
            parentFragmentManager.beginTransaction()
                    .addToBackStack(addNewMarkFragment.tag)
                    .replace(R.id.auth_activity, addNewMarkFragment)
                    .commit()
        }
    }

    private fun actionButtonMenu() {
        with(mapBinding) {
            buttonProfile.setOnClickListener {
                val profileFragment = ProfileFragment()
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
                //отчет по твоим маркам
            }
        }
    }

    private fun loadListMarks() {
        var myRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        val rootRef = FirebaseDatabase.getInstance().reference.child("map").child("marks")
                .addValueEventListener(object :
                        ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val listMark: ArrayList<Mark> = ArrayList<Mark>()
                        for (dataSnapshot: DataSnapshot in snapshot.children) {
                            var id =
                                    snapshot.child(dataSnapshot.key.toString()).child("id").value.toString()
                                            .toInt()
                            var name = snapshot.child(dataSnapshot.key.toString())
                                    .child("name").value.toString()
                            var description = snapshot.child(dataSnapshot.key.toString())
                                    .child("description").value.toString()
                            var latitude = snapshot.child(dataSnapshot.key.toString())
                                    .child("latitude").value.toString()
                            var longitude = snapshot.child(dataSnapshot.key.toString())
                                    .child("longitude").value.toString()
                            listMark.add(
                                    Mark(
                                            id,
                                            name,
                                            longitude.toDouble(),
                                            latitude.toDouble(),
                                            description
                                    )
                            )
                        }
                        listMarks = listMark
                    }
                })
        addMarksInMap()
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
