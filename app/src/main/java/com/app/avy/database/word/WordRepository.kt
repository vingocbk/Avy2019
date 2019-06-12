package com.app.avy.database.word

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.app.avy.database.AvyRoomDatabase

class WordRepository(application: Application) {
    var mWordDao: WordDao
    var mAllWord: LiveData<List<Word>>

    init {
        val db = AvyRoomDatabase.getDatabase(application)
        mWordDao = db.wordDao()
        mAllWord = mWordDao.getAllWords()

    }

    fun getAllWords(): LiveData<List<Word>> {
        return mAllWord
    }


    fun insert(word: Word) {
        insertAsyncTask(mWordDao).execute(word)
    }

    class insertAsyncTask constructor(private val mAsyncTaskDao: WordDao) :
        AsyncTask<Word, Void, Int>() {

        override fun doInBackground(vararg params: Word): Int {
            mAsyncTaskDao.insert(params[0])
            return 1
        }
    }

}