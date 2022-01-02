package com.ulskjourney.ulskjourney.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS user (\n" +
                        "    id integer PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name string character(100) NOT NULL,\n" +
                        "    login character(100) NOT NULL,\n" +
                        "    password character(100) NOT NULL);\n"
        )
        db.execSQL(
                "CREATE TABLE mark (\n" +
                        "    id integer PRIMARY KEY AUTOINCREMENT,\n" +
                        "    name character(100) NOT NULL,\n" +
                        "    latitude character(100) NOT NULL,\n" +
                        "    longitude character(100) NOT NULL,\n" +
                        "    description character(250) NOT NULL,\n" +
                        "    userId integer NOT NULL);\n"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $DATABASE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "UlskJourney.db" // название бд
        private const val SCHEMA = 1 // версия базы данных
    }
}

