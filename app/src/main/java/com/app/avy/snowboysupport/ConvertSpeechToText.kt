package com.app.avy.snowboysupport

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.app.avy.BaseActivity
import com.app.avy.MyApplication
import com.app.avy.database.word.Word
import com.app.avy.database.word.WordViewModel
import com.app.avy.module.MusicModule
import com.app.avy.module.WeatherData
import com.app.avy.utils.Constant

class ConvertSpeechToText(val activity: BaseActivity, var result: String, var count: Int) {
    val TAG = ConvertSpeechToText::class.java.simpleName
    lateinit var mWordViewModel: WordViewModel

    fun convertText() {
        // music
        var application = activity.application as MyApplication
        mWordViewModel = ViewModelProviders.of(activity).get(WordViewModel::class.java)
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

        // weather
        if (result.trim() == "hôm nay thời tiết thế nào" || result.trim() == "Hôm nay thời tiết thế nào" || result.trim() == "thời tiết hôm nay thế nào" || result.trim() == "Thời tiết hôm nay thế nào") {
            application.bus().send(WeatherData)
        }

        // delete item
        if (result.trim().contains("bỏ")) {
            val teamp = result.substring("bỏ".length, result.indexOf("ở"))
            for (i in 1..count) {
                if (result.trim().contains(i.toString())) {
                    Log.e(TAG, "delete-------> $teamp  $i")
                    mWordViewModel.updateWordWithType(teamp.trim().toUpperCase(), i.toString(), false)
                }
            }
            // add item
        } else if (result.trim().contains(" ở ")) {
            // add thêm vào database nếu chưa tồn tại
            val teamp = result.substring(0, result.indexOf("ở"))
            insderWord(teamp)
            for (i in 1..count) {
                if (result.trim().contains(i.toString())) {
                    Log.e(TAG, "add-------> $teamp  $i")
                    mWordViewModel.updateWordWithType(teamp.trim().toUpperCase(), i.toString(), true)
                }
            }
        }
    }

    fun insderWord(word: String) {
        var isConstant = false
        for (i in Constant.itemDefault.indices) {
            if (Constant.itemDefault[i].toUpperCase() == word.trim().toUpperCase()) {
                isConstant = true
                return
            } else {
                isConstant = false
            }
        }

        Log.e(" ", "-------> ${word.trim()} $isConstant")

        if (!isConstant) {
            Constant.itemDefault.add(word.trim())
            for (i in 1..count) {
                mWordViewModel.insert(
                    Word(
                        Constant.itemDefault.size.toString(),
                        i.toString(),
                        word.trim().toUpperCase(),
                        false
                    )
                )
            }
        }
    }
}