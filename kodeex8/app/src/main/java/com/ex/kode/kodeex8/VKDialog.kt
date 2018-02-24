package com.ex.kode.kodeex8

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class VKDialog(
        val id: Int,
        val last_name: String,
        val first_name: String,
        val ts: Long,
        val body: String,
        val unread: Boolean,
        val out: Boolean) {

    fun getName(): String {
        return "%s %s".format(first_name, last_name)
    }

    companion object {
        fun getTs(dts: Long): String {
            val cts: Long = Date().time/1000

            var sdf = SimpleDateFormat("HH:mm")

            if( cts-dts > 43200 )
                sdf = SimpleDateFormat("dd MMM")

            val dt = Date(dts)
            return sdf.format(dt)
        }
    }
}