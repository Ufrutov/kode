package com.ex.kode.kodeex7

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class History(
        var id: Int,
        var pill: Int,
        var name: String,
        var date: String,
        var status: String) {

    fun completePillValues(pill: Pill, status: String): ContentValues {
        val values = ContentValues()

        values.put(DBHistory.COL_NAME, pill.name)
        values.put(DBHistory.COL_PILL, pill.id)
        values.put(DBHistory.COL_STATUS, status)
        values.put(DBHistory.COL_DATE, Pill.getDate(false))

        return values
    }

    fun addNewTake(ctx: Context, pill: Pill, status: String): Long? {
        val db: SQLiteDatabase = DBHistory(ctx).writableDatabase
        val values = completePillValues(pill, status)
        val new_take = db.insert(DBHistory.SQL_TABLE, null, values)

        return new_take
    }

    companion object {
        fun getHistory(ctx: Context, id: Int): ArrayList<History> {
            val db: SQLiteDatabase = DBHistory(ctx).writableDatabase

            val projection: Array<String> = arrayOf(
                    DBHistory.COL_ID,
                    DBHistory.COL_PILL,
                    DBHistory.COL_NAME,
                    DBHistory.COL_DATE,
                    DBHistory.COL_STATUS
            )

            val sort: String = DBHistory.COL_DATE + " DESC"

            var history: ArrayList<History> = ArrayList<History>()

            val cursor: Cursor = db.query(
                    DBHistory.SQL_TABLE,
                    projection,
                    null,
                    null,
                    null, // (+) Can be grouped by Pill name
                    null,
                    sort)

            while (cursor.moveToNext()) {
                if( id == -1 || id == cursor.getLong(cursor.getColumnIndexOrThrow(DBHistory.COL_PILL)).toInt() )
                    history.add(History(
                            cursor.getLong(cursor.getColumnIndexOrThrow(DBHistory.COL_ID)).toInt(),
                            cursor.getLong(cursor.getColumnIndexOrThrow(DBHistory.COL_PILL)).toInt(),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHistory.COL_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHistory.COL_DATE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DBHistory.COL_STATUS))
                    ))
            }

            cursor.close()

            return history
        }
    }
}