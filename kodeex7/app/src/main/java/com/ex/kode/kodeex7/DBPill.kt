package com.ex.kode.kodeex7

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBPill(ctx: Context) : SQLiteOpenHelper(ctx, DB_NAME, null, DB_V) {
    companion object {
        val DB_V: Int = 1
        val DB_NAME: String = "pills.db"

        val SQL_TABLE = "pills"
        val COL_ID = "id"
        val COL_NAME = "name"
        val COL_DESC = "desc"
        val COL_NOTE = "note"
        val COL_SCHED = "sched"
        val COL_PIC = "pic"
    }

    private val SQL_CREATE_ENTRIES: String = "CREATE TABLE $SQL_TABLE ($COL_ID INTEGER PRIMARY KEY,$COL_NAME TEXT,$COL_DESC TEXT,$COL_NOTE TEXT, $COL_SCHED TEXT, $COL_PIC TEXT)"

    private val SQL_DELET_ENTRIES: String = "DROP TABLE IF EXISTS $SQL_TABLE"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, v1: Int, v2: Int) {
        db.execSQL(SQL_DELET_ENTRIES)
        onCreate(db)
    }
}