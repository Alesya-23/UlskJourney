package com.ulskjourney.ulskjourney.model.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import android.database.sqlite.SQLiteDatabase
import com.ulskjourney.ulskjourney.model.DatabaseHelper
import com.ulskjourney.ulskjourney.model.models.User

class UserStorage(context: Context) {
    private var sqlHelper: DatabaseHelper = DatabaseHelper(context)
    private var db = sqlHelper.writableDatabase
    private val TABLE = "user"
    private val COLUMN_ID = "id"
    private val COLUMN_LOGIN = "login"
    private val COLUMN_PASSWORD = "password"
    private val COLUMN_NAME = "name"


    fun open(): UserStorage {
        db = sqlHelper.writableDatabase
        return this
    }

    fun close() {
        db.close()
    }

    fun getFullList(): List<User?> {
        val database = sqlHelper.readableDatabase
        val cursor: Cursor = database.rawQuery("select * from $TABLE", null)
        val list: MutableList<User?> = ArrayList()
        if (!cursor.moveToFirst()) {
            return list
        }
        do {
            var id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            var login = cursor.getString(cursor.getColumnIndex(COLUMN_LOGIN))
            var password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            var name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            list.add(User(id, login, password, name))
            cursor.moveToNext()
        } while (!cursor.isAfterLast)
        cursor.close()
        database.close()
        return list
    }

    fun getElement(id: Int): User? {
        val database = sqlHelper.readableDatabase
        val cursor: Cursor = database.rawQuery(
                "select * from " + TABLE + " where "
                        + COLUMN_ID + " = " + id, null
        )
        if (!cursor.moveToFirst()) {
            return null
        }
        var idUser = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
        var login = cursor.getString(cursor.getColumnIndex(COLUMN_LOGIN))
        var password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
        var name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        cursor.close()
        database.close()
        return User(idUser, login, password, name)
    }

    fun insert(model: User) {
        val content = ContentValues()
        content.put(COLUMN_LOGIN, model.login)
        content.put(COLUMN_PASSWORD, model.password)
        content.put(COLUMN_NAME, model.name)
        val database = this.sqlHelper.writableDatabase
        database.insert(TABLE, null, content)
    }

    fun update(model: User) {
        val content = ContentValues()
        content.put(COLUMN_LOGIN, model.login)
        content.put(COLUMN_PASSWORD, model.password)
        content.put(COLUMN_NAME, model.name)
        val where = COLUMN_ID + " = " + model.id
        val database = this.sqlHelper.writableDatabase
        database.update(TABLE, content, where, null)
    }
}