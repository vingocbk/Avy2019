package com.app.avy

import android.app.Application
import com.app.avy.network.RetrofitHelper
import com.app.avy.utils.RxBus

class MyApplication : Application() {
    private var bus: RxBus? = null
    private var retrofitHelper: RetrofitHelper? = null

    override fun onCreate() {
        super.onCreate()
        retrofitHelper = RetrofitHelper.getInstance()
        bus = RxBus()
    }

    fun bus(): RxBus {
        return bus!!
    }

    fun retrofitHelper(): RetrofitHelper {
        return retrofitHelper!!
    }

}