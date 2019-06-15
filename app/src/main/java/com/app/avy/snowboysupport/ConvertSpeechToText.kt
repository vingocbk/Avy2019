package com.app.avy.snowboysupport

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.app.avy.MyApplication
import com.app.avy.module.MusicModule
import com.app.avy.module.WeatherData
import com.app.avy.utils.Constant

class ConvertSpeechToText(val application: MyApplication, var result: String) {
    val TAG = ConvertSpeechToText::class.java.simpleName

    fun convertText() {
        // music
        when (result.trim()) {
            "tạm dừng" -> {
                application.bus().send(MusicModule(result, PlaybackStateCompat.STATE_PAUSED))
            }
            "tiếp tục" -> {
                application.bus().send(MusicModule(result, PlaybackStateCompat.STATE_PLAYING))
            }
            "chuyển bài" -> {
                application.bus().send(MusicModule(result, PlaybackStateCompat.STATE_SKIPPING_TO_NEXT))
            }
            "lùi lại" -> {
                application.bus().send(MusicModule(result, PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS))
            }
            "tắt loa" -> {
                application.bus().send(MusicModule(result, Constant.OFF_VOLUME))
            }
            "bật loa" -> {
                application.bus().send(MusicModule(result, Constant.ON_VOLUME))
            }
            "tăng loa" -> {
                application.bus().send(MusicModule(result, Constant.INCREASE_VOLUME))
            }
            "giảm loa" -> {
                application.bus().send(MusicModule(result, Constant.REDUCTION_VOLUME))
            }
        }

        Log.e(TAG, "------->" + result)
        // weather
        if (result.trim() == "hôm nay thời tiết thế nào" || result.trim() == "Hôm nay thời tiết thế nào" || result.trim() == "thời tiết hôm nay thế nào" || result.trim() == "Thời tiết hôm nay thế nào") {
            application.bus().send(WeatherData)
        }
    }
}