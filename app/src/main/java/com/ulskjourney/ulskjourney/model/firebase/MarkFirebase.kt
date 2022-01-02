package com.ulskjourney.ulskjourney.model.firebase

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ulskjourney.ulskjourney.model.database.MarkStorage
import com.ulskjourney.ulskjourney.model.models.Mark

class MarkFirebase {
    private val TABLE = "mark"
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference(TABLE)
    fun syncUsers(context: Context) {
        val logic = MarkStorage(context)
        logic.open()
        val models: List<Mark> = logic.getFullList() as List<Mark>
        logic.close()
        database.removeValue()
        for (model in models) {
            database.push().setValue(model)
        }
    }
}