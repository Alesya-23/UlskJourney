package com.ulskjourney.ulskjourney.utils

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import com.google.firebase.database.*
import com.ulskjourney.ulskjourney.model.models.Mark
import com.ulskjourney.ulskjourney.model.models.User

class FirebasePostService : Service(), IFirebasePostService {
    private var binder = ContactBinder()
    private var mBound: Boolean = false
    var listMarks: ArrayList<Mark> = ArrayList()
    var listUsers: ArrayList<User> = ArrayList()

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            binder = service as FirebasePostService.ContactBinder
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    fun getConnection(): ServiceConnection {
        return connection
    }

    fun getBinder(): ContactBinder {
        return binder
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    fun unBind() {
        mBound = false
    }

    inner class ContactBinder : Binder() {
        fun getService(): FirebasePostService = this@FirebasePostService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId) // do work
    }

    override suspend fun getListMarks(): ArrayList<Mark> {
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
        return listMarks
    }


    override suspend fun getListUsers(): ArrayList<User> {
        var myRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        val rootRef = FirebaseDatabase.getInstance().reference.child("map").child("users")
            .addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val listUser: ArrayList<User> = ArrayList<User>()
                    for (dataSnapshot: DataSnapshot in snapshot.children) {
                        var id =
                            snapshot.child(dataSnapshot.key.toString()).child("id").value.toString()
                        var name = snapshot.child(dataSnapshot.key.toString())
                            .child("name").value.toString()
                        var login = snapshot.child(dataSnapshot.key.toString())
                            .child("login").value.toString()
                        var password = snapshot.child(dataSnapshot.key.toString())
                            .child("password").value.toString()
                        listUser.add(
                            User(
                                id,
                                name,
                                login,
                                password
                            )
                        )
                    }
                    listUsers = listUser
                }
            })
        return listUsers
    }

    override suspend fun getUser(id: String): User {
        val user = listUsers.find { it.login == id }
        if (user != null) {
            return user
        }
        return User(
            id = "-1",
            name = "Not found",
            login = "Not found",
            password = "none"
        )
    }

    override suspend fun getMark(id: Int): Mark {
        val mark = getListMarks().find { it.id == id }
        if (mark != null) {
            return mark
        }
        return Mark(
            id = -1,
            name = "Not found",
            longitude = 1.0,
            latitude = 1.0,
            description = "none",
        )
    }
}