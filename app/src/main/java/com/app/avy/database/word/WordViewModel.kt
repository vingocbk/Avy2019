package com.app.avy.database.word

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class WordViewModel constructor(application: Application) : AndroidViewModel(application) {
    var mRepository: WordRepository? = null
    var mAllWords: LiveData<List<Word>>? = null

    init {
        mRepository = WordRepository(application)
        mAllWords = mRepository!!.getAllWords()
    }

    fun getAllWords(): LiveData<List<Word>> {
        return mAllWords!!
    }

    fun insert(word: Word) {
        mRepository!!.insert(word)
    }

    fun getWordsWithId(type: String): LiveData<List<Word>> {
        return mRepository!!.getWordWithId(type)
    }

    fun seachItem(word: String, select: Boolean): LiveData<List<String>> {
        return mRepository!!.searchItem(word, select)
    }

    fun searchItemInCabinet(word: String, type: String): LiveData<List<Word>> {
        return mRepository!!.searchItemInCabinet(word, type)
    }

    fun deleteWord() {
        mRepository!!.deleteAllWord()
    }

    fun updateWord(word: Word) {
        mRepository!!.updateWord(word)
    }

    fun updateWordWithType(word: String, type: String, select: Boolean) {
        mRepository!!.updateWordWithType(word, type, select)
    }
}