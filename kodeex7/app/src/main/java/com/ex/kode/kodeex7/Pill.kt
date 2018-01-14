package com.ex.kode.kodeex7

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class Pill(
        var id: Int,
        var name: String,
        var desc: String,
        var notes: String,
        var sched: String,
        var pic: String) {

    fun completePillValues(pill: Pill): ContentValues {
        val values = ContentValues()

        values.put(DBPill.COL_NAME, pill.name)
        values.put(DBPill.COL_DESC, pill.desc)
        values.put(DBPill.COL_NOTE, pill.notes)
        values.put(DBPill.COL_SCHED, pill.sched)
        values.put(DBPill.COL_PIC, pill.pic)

        return values
    }

    fun addNewPill(ctx: Context, pill: Pill): Long? {
        val db: SQLiteDatabase = DBPill(ctx).writableDatabase
        val values = completePillValues(pill)
        val new_pill = db.insert(DBPill.SQL_TABLE, null, values)

        return new_pill
    }

    fun updatePill(ctx: Context, pill: Pill, pillUpdate: Pill) {
        val db: SQLiteDatabase = DBPill(ctx).writableDatabase
        val values = completePillValues(pillUpdate)

        db.update(DBPill.SQL_TABLE, values, "%s = %s".format(DBPill.COL_ID, pill.id), null)
    }

    fun deletePill(ctx: Context, pill: Pill) {
        val db: SQLiteDatabase = DBPill(ctx).writableDatabase

        db.delete(DBPill.SQL_TABLE, "%s = %s".format(DBPill.COL_ID, pill.id), null)
    }

    fun getSchedule(): HashMap<String, String> {
        val type = object: TypeToken<HashMap<String, String>>(){}.type
        return Gson().fromJson(sched, type)
    }

    companion object {
        fun getPills(ctx: Context?): ArrayList<Pill> {
            val db: SQLiteDatabase = DBPill(ctx as Context).writableDatabase

            val projection: Array<String> = arrayOf(
                    DBPill.COL_ID,
                    DBPill.COL_NAME,
                    DBPill.COL_DESC,
                    DBPill.COL_NOTE,
                    DBPill.COL_SCHED,
                    DBPill.COL_PIC
            )

            val sort: String = DBPill.COL_NAME + " ASC"

            val pills: ArrayList<Pill> = ArrayList<Pill>()

            val cursor: Cursor = db.query(
                    DBPill.SQL_TABLE,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sort)

            while (cursor.moveToNext()) {
                pills.add(Pill(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DBPill.COL_ID)).toInt(),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBPill.COL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBPill.COL_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBPill.COL_NOTE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBPill.COL_SCHED)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DBPill.COL_PIC))
                ))
            }

            cursor.close()

            return pills
        }

        fun getPill(id: String, ctx: Context): Pill? {
            var pill: Pill? = null

            getPills(ctx).forEach { p: Pill ->
                if( p.id == id.toInt() )
                    pill = p
            }

            return pill
        }

        fun getDate(format: Boolean): String {
            if( format ) {
                val date = (System.currentTimeMillis()/1000).toString()
                return this.parseDate(date, "dd.MM.yyyy")
            } else
                return (System.currentTimeMillis()/1000).toString()
        }

        @SuppressLint("SimpleDateFormat")
        fun parseDate(dt: String, pattern: String): String {
            val f = SimpleDateFormat(pattern)
            return f.format(dt.toLong()*1000)
        }

        fun getPillTS(ts: String): Long {
            // ts format HH:mm
            val calendar_ts = Calendar.getInstance()

            var hour: Int = ts.split(":")[0].toInt()
//            if( hour == 0 )
//                hour = 24

            calendar_ts.set(Calendar.HOUR_OF_DAY, hour)
            calendar_ts.set(Calendar.MINUTE, ts.split(":")[1].toInt())

            return calendar_ts.timeInMillis/1000
        }
    }
}