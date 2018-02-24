package com.ex.kode.kodeex8

import android.app.Application
import android.os.Build
import android.support.annotation.RequiresApi
import com.vk.sdk.VKSdk
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

// Class for VK sdl initialization

class VKode: Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(applicationContext)
    }

    companion object {
        fun parseTS(ts: Long): String {
            val sdf = SimpleDateFormat("HH:mm")
            val dt = Date(ts)
            return sdf.format(dt)
        }
    }
}