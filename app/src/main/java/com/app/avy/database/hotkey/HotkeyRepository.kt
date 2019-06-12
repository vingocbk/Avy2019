package com.app.avy.database.hotkey

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import com.app.avy.database.AvyRoomDatabase

class HotkeyRepository(application: Application) {
    val TAG = HotkeyRepository::class.java.simpleName
    var mHoketDao: HotKeyDao
    var mAllHotkey: LiveData<List<Hotkey>>

    init {
        val db = AvyRoomDatabase.getDatabase(application)
        mHoketDao = db.hotkeyDao()
        mAllHotkey = mHoketDao.getAllHotkey()

    }

    fun getAllHotkey(): LiveData<List<Hotkey>> {
        return mAllHotkey
    }

    fun updateHotkey(hotkey: Hotkey) {
        UpdateHotkey(mHoketDao).execute(hotkey)
    }

    fun insertHotkey(hotkey: Hotkey) {
        InsertHotkey(mHoketDao).execute(hotkey)
    }


    class UpdateHotkey constructor(var keyDao: HotKeyDao) : AsyncTask<Hotkey, Void, Int>() {
        override fun doInBackground(vararg params: Hotkey?): Int {
            params[0]!!.let {
                keyDao.updateHotkey(it.id, it.hotkey, it.view, it.isSave)
                Log.e("HotkeyRepository", "doInBackground ")
            }
            return 1
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            Log.e("HotkeyRepository", "onPostExecute ")

        }
    }

    class InsertHotkey constructor(var keyDao: HotKeyDao) : AsyncTask<Hotkey, Void, Int>() {
        override fun doInBackground(vararg params: Hotkey?): Int {
            params[0]!!.let {
                keyDao.insert(it)
            }
            return 1
        }

    }


}