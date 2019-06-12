package com.app.avy.database.cabinet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class CabinetViewModle(application: Application) : AndroidViewModel(application) {
    var mRepository: CabinetRepository
    var mListCabinet: LiveData<List<Cabinet>>

    init {
        mRepository = CabinetRepository(application)
        mListCabinet = mRepository.getAllCabinet()
    }

    fun getAllCabinet(): LiveData<List<Cabinet>> {
        return mListCabinet
    }

    fun deleteCabinet(){
        mRepository.deleteCabinet()
    }

    fun insert(cabinet: Cabinet) {
        mRepository.insertCabinet(cabinet)
    }

    fun updateCabinet(cabinet: Cabinet) {
        mRepository.updateCabinet(cabinet)
    }
}