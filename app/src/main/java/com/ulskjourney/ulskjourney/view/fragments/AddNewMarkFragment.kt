package com.ulskjourney.ulskjourney.view.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ulskjourney.ulskjourney.R
import com.ulskjourney.ulskjourney.databinding.AddMarkFragmentBinding

class AddNewMarkFragment : Fragment(R.layout.add_mark_fragment) {
    private lateinit var addMarkFragmentBinding: AddMarkFragmentBinding
    private var name: String = ""
    private var longitude: Double? = null
    private var latitude: Double? = null
    private var description: String = ""
    private var isEdit: Boolean = false

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
        val map: HashMap<String, Any> =
            hashMapOf(
                "id" to 1,
                "name" to name,
                "longitude" to longitude.toString(),
                "latitude" to latitude.toString(),
                "description" to description,
            )
        FirebaseAuth.getInstance().signOut()
        //проверка есть ли такой ребёнок в бд
        val rootRef = FirebaseDatabase.getInstance().reference.child("map").child("marks")
            .child(name)
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(name).exists()) {
                    isEdit = true
                }
            }
        })
        if (!isEdit) {
            FirebaseDatabase.getInstance().reference.child("map").child("marks")
                .child(name).setValue(map)
            val toast: Toast = Toast.makeText(
                activity?.applicationContext,
                "Метка добавлена",
                Toast.LENGTH_LONG
            )
            return toast.show()
        }
    }
}