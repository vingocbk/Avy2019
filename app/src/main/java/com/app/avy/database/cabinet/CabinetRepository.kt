package com.app.avy.database.cabinet

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.app.avy.database.AvyRoomDatabase

class CabinetRepository(var application: Application) {
    var mCabinetDao: CabinetDao
    var mAllCabinet: LiveData<List<Cabinet>>

    init {
        val db = AvyRoomDatabase.getDatabase(application)
        mCabinetDao = db.cabinetDao()
        mAllCabinet = mCabinetDao.getAllCabinet()

    }

    fun getAllCabinet(): LiveData<List<Cabinet>> {
        return mAllCabinet
    }

    fun updateCabinet(cabinet: Cabinet) {
        UpdateCabinet(mCabinetDao).execute(cabinet)
    }

    fun insertCabinet(cabinet: Cabinet) {
        InsertCabinet(mCabinetDao).execute(cabinet)
    }

    fun deleteCabinet() {
        DeleteCabinet(mCabinetDao).execute()
    }


    class UpdateCabinet constructor(var cabinetDao: CabinetDao) : AsyncTask<Cabinet, Void, Int>() {
        override fun doInBackground(vararg params: Cabinet?): Int {
            params[0]!!.let {
                cabinetDao.updateCabinet(it.id, it.type, it.select)
            }
            return 1
        }
    }

    class InsertCabinet constructor(var cabinetDao: CabinetDao) : AsyncTask<Cabinet, Void, Int>() {
        override fun doInBackground(vararg params: Cabinet?): Int {
            params[0]!!.let {
                cabinetDao.insert(it)
            }
            return 1
        }

    }

    class DeleteCabinet constructor(var cabinetDao: CabinetDao) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            cabinetDao.deleteAll()
            return 1
        }

    }

}