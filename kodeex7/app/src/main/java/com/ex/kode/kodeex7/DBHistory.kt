package com.ex.kode.kodeex7

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHistory(ctx: Context) : SQLiteOpenHelper(ctx, DB_NAME, null, DB_V) {
    companion object {
        val DB_V: Int = 1
        val DB_NAME: String = "history.db"

        val SQL_TABLE = "history"
        val COL_ID = "id"
        val COL_PILL = "pill"
        val COL_NAME = "name"
        val COL_DATE = "date"
        val COL_STATUS = "status" // true, false, none
    }

    private val SQL_CREATE_ENTRIES: String = "CREATE TABLE $SQL_TABLE ($COL_ID INTEGER PRIMARY KEY,$COL_PILL INTEGER,$COL_NAME TEXT,$COL_DATE TEXT,$COL_STATUS TEXT)"

    private val SQL_DELET_ENTRIES: String = "DROP TABLE IF EXISTS $SQL_TABLE"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, v1: Int, v2: Int) {
        db.execSQL(SQL_DELET_ENTRIES)
        onCreate(db)
    }
}