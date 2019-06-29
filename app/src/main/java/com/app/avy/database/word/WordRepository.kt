package com.app.avy.database.word

import android.app.Application
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import com.app.avy.database.AvyRoomDatabase
import com.app.avy.database.cabinet.Cabinet
import com.app.avy.database.cabinet.CabinetDao

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

    fun getWordWithId(type: String): LiveData<List<Word>> {
        return mWordDao.getWordWithId(type)
    }

    fun searchItem(word: String, select: Boolean): LiveData<List<String>> {
        return mWordDao.searchItem(word, select)
    }

    fun searchItemInCabinet(word: String, type: String) :LiveData<List<Word>>{
        return mWordDao.searchItemInCabinet(word, type)
    }

    fun insert(word: Word) {
        insertAsyncTask(mWordDao).execute(word)
    }

    fun deleteAllWord() {
        DeleteAllWord(mWordDao).execute()
    }

    fun updateWord(word: Word) {
        UpdateWord(mWordDao).execute(word)
    }

    fun updateWordWithType(word: String, type: String, select: Boolean) {
        UpdateWordWithType(mWordDao, type, word, select).execute()
    }

    class insertAsyncTask constructor(private val mAsyncTaskDao: WordDao) :
        AsyncTask<Word, Void, Int>() {

        override fun doInBackground(vararg params: Word): Int {
            mAsyncTaskDao.insert(params[0])
            return 1
        }
    }

    class DeleteAllWord constructor(var wordDao: WordDao) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            wordDao.deleteAll()
            return 1
        }

    }

    class UpdateWord constructor(var wordDao: WordDao) : AsyncTask<Word, Void, Int>() {
        override fun doInBackground(vararg params: Word?): Int {
            params[0]!!.let {
                wordDao.updateWord(it.id, it.type, it.select)
            }
            return 1
        }
    }

    class UpdateWordWithType constructor(
        var wordDao: WordDao,
        var type: String,
        var word: String,
        var select: Boolean
    ) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            Log.e("UpdateWordWithType", "------> $word  $type  $select")
            wordDao.updateWordWithType(word.trim(), type.trim(), select)
            return 1
        }
    }

}