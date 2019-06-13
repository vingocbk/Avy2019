package com.app.avy

import android.app.Application
import com.app.avy.utils.RxBus

class MyApplication : Application() {
    private var bus: RxBus? = null

    override fun onCreate() {
        super.onCreate()
        bus = RxBus()
    }

    fun bus(): RxBus {
        return bus!!
    }

}