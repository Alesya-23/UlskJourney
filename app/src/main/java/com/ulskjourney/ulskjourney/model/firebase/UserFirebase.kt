package com.ulskjourney.ulskjourney.model.firebase

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ulskjourney.ulskjourney.model.database.UserStorage
import com.ulskjourney.ulskjourney.model.models.User

class UserFirebase {
    private val TABLE = "user"
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference(TABLE)
    fun syncUsers(context: Context) {
        val logic = UserStorage(context)
        logic.open()
        val models: List<User> = logic.getFullList() as List<User>
        logic.close()
        database.removeValue()
        for (model in models) {
            database.push().setValue(model)
        }
    }
}