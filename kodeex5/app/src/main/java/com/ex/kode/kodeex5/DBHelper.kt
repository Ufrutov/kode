package com.ex.kode.kodeex5

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(ctx: Context) : SQLiteOpenHelper(ctx, DB_NAME, null, DB_V) {

    companion object {
        val DB_V: Int = 1
        val DB_NAME: String = "tasks.db"

        val SQL_TABLE = "tasks"
        val COL_ID = "id"
        val COL_TEXT = "text"
        val COL_ACTIVE = "active" // BOOLEAN type is saved as Int 1 = true, 0 = false
        val COL_DATE = "date"
    }

    private val SQL_CREATE_ENTRIES: String = "CREATE TABLE $SQL_TABLE ($COL_ID INTEGER PRIMARY KEY,$COL_TEXT TEXT,$COL_ACTIVE BOOLEAN, $COL_DATE TEXT)"

    private val SQL_DELET_ENTRIES: String = "DROP TABLE IF EXISTS $SQL_TABLE"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, v1: Int, v2: Int) {
        db.execSQL(SQL_DELET_ENTRIES)
        onCreate(db)
    }
}