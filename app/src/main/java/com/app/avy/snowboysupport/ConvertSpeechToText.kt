package com.app.avy.snowboysupport

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.avy.BaseActivity
import com.app.avy.MyApplication
import com.app.avy.R
import com.app.avy.database.word.Word
import com.app.avy.database.word.WordViewModel
import com.app.avy.module.*
import com.app.avy.network.MyObserver
import com.app.avy.utils.Constant
import com.app.avy.utils.SharedPreferencesManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ConvertSpeechToText(val activity: BaseActivity, var result: String, var count: Int) {
    val TAG = ConvertSpeechToText::class.java.simpleName
    lateinit var mWordViewModel: WordViewModel
    val mMyApplication: MyApplication = activity.application as MyApplication
    private val mPref: SharedPreferencesManager = SharedPreferencesManager.getInstance(activity)
    var mCount: Int = mPref.getIntFromSharePreferen(SharedPreferencesManager.CABINET_NUMBER_DEFAULT)!!
    var mHeadIP: String? = null
    var mLastIP: String? = null

    init {
        mHeadIP = mPref.getStringFromSharePreferen(SharedPreferencesManager.HEADER_IP)
        mLastIP = mPref.getStringFromSharePreferen(SharedPreferencesManager.LASST_IP)
    }

    fun convertText() {
        ConfigData.getConfig()?.let {
            this.result = Constant.handleConfig(it, result).toLowerCase()
        }
        this.result = result.toLowerCase()

        // music
        val application = activity.application as MyApplication
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
        } else
        // open window
            if (result.contains("mở") || result.contains("Mở")) {
                for (i in 1..mCount) {
                    if (result.contains(i.toString())) {
                        mMyApplication.retrofitHelper()
                            .getNetworkService("${Constant.HTTP}$mHeadIP".plus(mLastIP!!.toInt() + i))
                            .openWindow()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(MyObserver(activity))
                    }
                }
            }

        // close window
        if (result.contains("đóng") || result.contains("Đóng")) {
            for (i in 1..mCount) {
                if (result.contains(i.toString())) {
                    mMyApplication.retrofitHelper()
                        .getNetworkService("${Constant.HTTP}$mHeadIP".plus(mLastIP!!.toInt() + i))
                        .closeWindow()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(MyObserver(activity))
                }
            }
        }

        // open all
        if (result.contains(activity.getString(R.string.open)) && result.contains(activity.getString(R.string.all))) {
            Observable.merge(createOpenObservable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(MyObserver(activity))
        } else if (result.contains(activity.getString(R.string.close)) && result.contains(activity.getString(R.string.all))) {
            Observable.merge(createCloseObservable())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(MyObserver(activity))
        }
        // open light
        if (result.contains(activity.getString(R.string.open_led))) {
            mPref.storeBooleanInSharePreferen(SharedPreferencesManager.TURN_LIGHT, true)
            val request = LightModule("on", "255", "255", "255")
            changeLight(request)
        }
        // close light
        if (result.contains(activity.getString(R.string.close_led))) {
            mPref.storeBooleanInSharePreferen(SharedPreferencesManager.TURN_LIGHT, false)
            val request = LightModule("off", "255", "255", "255")
            changeLight(request)
        }
        // change color
        if (result.contains(activity.getString(R.string.color_red))) {
            val request = LightModule("change", "255", "0", "0")
            changeLight(request)
        }
        if (result.contains(activity.getString(R.string.color_blue))) {
            val request = LightModule("change", "0", "255", "0")
            changeLight(request)
        }

        if (result.contains(activity.getString(R.string.color_blue_1))) {
            val request = LightModule("change", "0", "0", "255")
            changeLight(request)
        }

        if (result.contains(activity.getString(R.string.color_yt))) {
            val request = LightModule("change", "255", "255", "0")
            changeLight(request)
        }

        if (result.contains(activity.getString(R.string.color_h))) {
            val request = LightModule("change", "255", "0", "255")
            changeLight(request)
        }

        if (result.contains(activity.getString(R.string.color_t))) {
            val request = LightModule("change", "139", "0", "255")
            changeLight(request)
        }
        if (result.contains(activity.getString(R.string.color_w))) {
            val request = LightModule("change", "255", "255", "0")
            changeLight(request)
        }
        if (result.contains(activity.getString(R.string.color_orange))) {
            val request = LightModule("change", "255", "152", "0")
            changeLight(request)
        }
        if (result.contains(activity.getString(R.string.color_n))) {
            val request = LightModule("change", "121", "85", "72")
            changeLight(request)
        }
        if (result.contains(activity.getString(R.string.color_cyan))) {
            val request = LightModule("change", "0", "188", "212")
            changeLight(request)
        }
        if (result.contains(activity.getString(R.string.color_grey))) {
            val request = LightModule("change", "158", "158", "158")
            changeLight(request)
        }
        // find item
        if (result.contains(activity.getString(R.string.find))) {
            Log.e(
                TAG,
                "find-------> ${result.substring(
                    activity.getString(R.string.find).length + 1,
                    result.trim().length
                ).toUpperCase()}"
            )

            findItem(
                result.substring(
                    activity.getString(R.string.find).length + 1,
                    result.trim().length
                ).trim().toUpperCase()
            )
        }

    }

    fun findItem(item: String) {
        val listItem: ArrayList<String> = ArrayList()
        mWordViewModel.getAllWords().observe(activity, Observer {
            for (i in it.indices) {
                if (it[i].mWord.toUpperCase().trim().contains(item) && it[i].select) {
                    if (listItem.isEmpty()) {
                        listItem.add(it[i].type)
                    } else {
                        if (!listItem.contains(it[i].type)) {
                            listItem.add(it[i].type)
                        }
                    }
                }
            }

            Observable.merge(createFindItemObservable(listItem))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(MyObserver(activity))

        })
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


    private fun createOpenObservable(): List<Observable<*>> {
        val result = ArrayList<Observable<*>>()
        for (i in 1..count) {
            result.add(
                mMyApplication.retrofitHelper()
                    .getNetworkService("${Constant.HTTP}$mHeadIP".plus(mLastIP!!.toInt() + i))
                    .openWindow()
            )
        }
        return result
    }

    private fun createCloseObservable(): List<Observable<*>> {
        val result = ArrayList<Observable<*>>()
        for (i in 1..count) {
            result.add(
                mMyApplication.retrofitHelper()
                    .getNetworkService("${Constant.HTTP}$mHeadIP".plus(mLastIP!!.toInt() + i))
                    .closeWindow()
            )
        }
        return result
    }

    private fun changeLight(request: LightModule) {
        mMyApplication.retrofitHelper()
            .getNetworkService("${Constant.HTTP}$mHeadIP".plus(mLastIP!!.toInt() + 11))
            .changeLight(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(MyObserver(activity))
    }

    private fun createFindItemObservable(listItem: ArrayList<String>): List<Observable<*>> {
        val result = ArrayList<Observable<*>>()
        for (i in listItem.indices) {
            result.add(
                mMyApplication.retrofitHelper()
                    .getNetworkService("${Constant.HTTP}$mHeadIP".plus(mLastIP!!.toInt() + listItem[i].toInt()))
                    .openWindow()
            )
        }
        return result
    }


}