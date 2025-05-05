package com.example.webservice

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE USUARIOS (
                ID INTEGER PRIMARY KEY AUTOINCREMENT,
                NOMBRE TEXT,
                APELLIDO TEXT,
                EMAIL TEXT,
                CLAVE TEXT
            )
        """.trimIndent()
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS USUARIOS")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "APPSQLITE"
        private const val DATABASE_VERSION = 1
    }
}
