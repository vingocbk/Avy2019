package com.app.avy.database.hotkey

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class HokeyViewModle(application: Application) : AndroidViewModel(application) {
    var mRepository: HotkeyRepository
    var mListkey: LiveData<List<Hotkey>>

    init {
        mRepository = HotkeyRepository(application)
        mListkey = mRepository.getAllHotkey()
    }

    fun getAllWords(): LiveData<List<Hotkey>> {
        return mListkey
    }

    fun insert(hotkey: Hotkey) {
        mRepository.insertHotkey(hotkey)
    }

    fun updateHotkey(hotkey: Hotkey) {
        mRepository.updateHotkey(hotkey)
    }

}