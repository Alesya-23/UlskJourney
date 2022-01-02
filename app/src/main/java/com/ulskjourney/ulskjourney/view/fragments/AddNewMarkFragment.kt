package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.AddMarkFragmentBinding
import com.ulskjourney.ulskjourney.model.database.MarkStorage
import com.ulskjourney.ulskjourney.model.models.Mark
import com.ulskjourney.ulskjourney.viewModel.UserViewModel

private const val ID_ARGUMENT = "ID_ARGUMENT"

class AddNewMarkFragment : Fragment(R.layout.add_mark_fragment) {
    private lateinit var addMarkFragmentBinding: AddMarkFragmentBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private var name: String = ""
    private var longitude: Double = 1.0
    private var latitude: Double = 1.0
    private var description: String = ""
    private var isEdit: Boolean = false
    var userId = 0
    private val userDetailIdArgument by lazy {
        requireArguments().getInt(ID_ARGUMENT, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addMarkFragmentBinding = AddMarkFragmentBinding.bind(view)
        addMarkFragmentBinding.addMarkButton.setOnClickListener {
            addNewMark()
        }
        activity?.title = "Новая метка"
    }

    private fun addNewMark() {
        with(addMarkFragmentBinding) {
            name = nameNewMarkEdit.text.toString()
            longitude = textViewLongitude.text.toString().toDouble()
            latitude = textViewLatitude.text.toString().toDouble()
            description = descriptionEdit.text.toString()
        }
        if (checkFillFields()) {
            //добавляем точку
            addMarkToDatabase()
        } else return Toast.makeText(
                activity?.applicationContext,
                "Не заполнены поля",
                Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkFillFields(): Boolean {
        if (name.isEmpty()) {
            return false
        }
        if (longitude == null) {
            return false
        }
        if (latitude == null) {
            return false
        }
        if (description.isEmpty()) {
            return false
        }
        return true
    }

    private fun addMarkToDatabase() {
        val markStorage = activity?.applicationContext?.let { MarkStorage(it) }
        markStorage?.open()
        val listMark = markStorage?.getFullList()
        if (listMark != null) {
            val userOur = listMark.find { it?.longitude == longitude && it?.latitude == latitude }
            if (userOur != null) {
                isEdit = true
                Toast.makeText(activity?.applicationContext, "Метка уже есть в системе", Toast.LENGTH_SHORT).show()
            }
        }
        if (!isEdit) {
            markStorage?.insert(Mark(0, name,
                    latitude.toDouble(), longitude.toDouble(), description, userId))
            markStorage?.close()
            val toast: Toast = Toast.makeText(
                    activity?.applicationContext,
                    "Метка добавлена прошла усешно",
                    Toast.LENGTH_LONG
            )
            requireActivity().supportFragmentManager.popBackStack()
            return toast.show()
        }
    }

    companion object {
        fun newInstance(userID: Int) = AddNewMarkFragment().apply {
            this.arguments = bundleOf(
                    ID_ARGUMENT to userID
            )
            userId = userID
        }
    }
}