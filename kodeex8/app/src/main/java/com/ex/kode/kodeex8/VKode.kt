package com.ex.kode.kodeex8

import android.app.Application
import com.vk.sdk.VKSdk

// Class for VK sdl initialization

class VKode: Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(applicationContext)
    }
}