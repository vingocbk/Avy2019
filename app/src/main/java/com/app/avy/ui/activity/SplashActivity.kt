package com.app.avy.ui.activity

import android.content.Intent
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.avy.BaseActivity
import com.app.avy.MainActivity
import com.app.avy.R
import com.app.avy.database.cabinet.Cabinet
import com.app.avy.database.cabinet.CabinetViewModle
import com.app.avy.database.hotkey.HokeyViewModle
import com.app.avy.database.hotkey.Hotkey
import com.app.avy.database.word.Word
import com.app.avy.database.word.WordViewModel
import com.app.avy.utils.Constant
import com.app.avy.utils.SharedPreferencesManager


class SplashActivity : BaseActivity() {
    lateinit var mHotkeyViewModel: HokeyViewModle
    lateinit var mCabinetViewModle: CabinetViewModle
    lateinit var mWordViewModel: WordViewModel
    var mCount: Int = 0

    override fun getId() = R.layout.splash_activity

    override fun onViewReady() {
        mCount =
            SharedPreferencesManager.getInstance(this).getIntFromSharePreferen(SharedPreferencesManager.CABINET_NUMBER_DEFAULT)!!
        mHotkeyViewModel = ViewModelProviders.of(this).get(HokeyViewModle::class.java)
        mCabinetViewModle = ViewModelProviders.of(this).get(CabinetViewModle::class.java)
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)
        loadDefaultConfig()
        handleHotkey()
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }

    fun handleHotkey() {

        mHotkeyViewModel.getAllWords().observe(this,
            Observer<List<Hotkey>> {
                if (it.isEmpty()) {
                    for (i in 1..Constant.SIZE_KEY) {
                        mHotkeyViewModel.insert(
                            Hotkey(
                                i.toString(),
                                getString(R.string.txt_hotkey),
                                getString(R.string.txt_view),
                                false
                            )
                        )
                    }
                }
            })

        // mCabinetViewModle.deleteCabinet()
        mCabinetViewModle.getAllCabinet().observe(this,
            Observer {
                if (it.isEmpty()) {
                    for (i in 1..Constant.SIZE_KEY) {
                        for (j in 1..30) {
                            mCabinetViewModle.insert(Cabinet(j.toString(), i.toString(), "Tủ ${j.toString()}", false))
                        }
                    }
                }
            })


        //mWordViewModel.deleteWord()
        mWordViewModel.getAllWords().observe(this, Observer {
            if (it.isEmpty()) {
                for (i in 1..mCount) {
                    for (j in Constant.itemDefault.indices) {
                        mWordViewModel.insert(
                            Word(
                                j.toString(),
                                i.toString(),
                                Constant.itemDefault[j].toUpperCase(),
                                false
                            )
                        )
                        Log.e("mWordViewModel", "insert----->" + Constant.itemDefault[j].toUpperCase() + "----" + i)
                    }
                }
            }
        })


    }

    fun loadDefaultConfig() {
        SharedPreferencesManager.getInstance(this)
            .storeIntInSharePreferen(SharedPreferencesManager.CABINET_NUMBER_DEFAULT, 30)

        SharedPreferencesManager.getInstance(this)
            .storeStringInSharePreferen(SharedPreferencesManager.ADVANCED_PASS, "12345")
    }
}