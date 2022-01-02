package com.ulskjourney.ulskjourney.model.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.ulskjourney.ulskjourney.model.DatabaseHelper
import com.ulskjourney.ulskjourney.model.models.Mark

class MarkStorage(context: Context) {
    private var sqlHelper: DatabaseHelper = DatabaseHelper(context)
    private var db = sqlHelper.writableDatabase
    private val TABLE = "mark"
    private val COLUMN_ID = "id"
    private val COLUMN_NAME = "name"
    private val COLUMN_LATITUDE = "latitude"
    private val COLUMN_LONGITUDE = "longitude"
    private val COLUMN_DESCRIPTION = "description"
    private val COLUMN_USERID = "userId"

    fun open(): MarkStorage {
        db = sqlHelper.writableDatabase
        return this
    }

    fun close() {
        db.close()
    }

    fun getFullList(): List<Mark?> {
        val database = sqlHelper.readableDatabase
        val cursor: Cursor = database.rawQuery("select * from $TABLE", null)
        val list: MutableList<Mark?> = ArrayList()
        if (!cursor.moveToFirst()) {
            return list
        }
        do {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            var name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            var latitude = cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE))
            var longitude = cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE))
            var description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
            var userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USERID))
            list.add(Mark(id, name, latitude.toDouble(), longitude.toDouble(), description, userId))
            cursor.moveToNext()
        } while (!cursor.isAfterLast)
        database.close()
        return list
    }

    fun getElement(id: Int): Mark? {
        val database = sqlHelper.readableDatabase
        val cursor: Cursor = database.rawQuery(
                "select * from " + TABLE + " where "
                        + COLUMN_ID + " = " + id, null
        )
        if (!cursor.moveToFirst()) {
            return null
        }
        var idMark = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
        var name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        var latitude = cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE))
        var longitude = cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE))
        var description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
        var userId = cursor.getInt(cursor.getColumnIndex(COLUMN_USERID))
        cursor.close()
        database.close()
        return Mark(idMark, name, latitude.toDouble(), longitude.toDouble(), description, userId.toInt())
    }

    fun insert(model: Mark) {
        val content = ContentValues()
        content.put(COLUMN_NAME, model.name)
        content.put(COLUMN_LATITUDE, model.latitude)
        content.put(COLUMN_LONGITUDE, model.longitude)
        content.put(COLUMN_DESCRIPTION, model.description)
        content.put(COLUMN_USERID, model.userId)
        val database = this.sqlHelper.writableDatabase
        database.insert(TABLE, null, content)
    }

    fun update(model: Mark) {
        val content = ContentValues()
        content.put(COLUMN_NAME, model.name)
        content.put(COLUMN_LATITUDE, model.latitude)
        content.put(COLUMN_LONGITUDE, model.longitude)
        content.put(COLUMN_DESCRIPTION, model.description)
        content.put(COLUMN_USERID, model.userId)
        val where = COLUMN_ID + " = " + model.id
        val database = this.sqlHelper.writableDatabase
        database.update(TABLE, content, where, null)
    }

    fun delete(id: Int) {
        val database = this.sqlHelper.writableDatabase
        val where = "$COLUMN_ID = $id"
        database.delete(TABLE, where, null)
    }
}