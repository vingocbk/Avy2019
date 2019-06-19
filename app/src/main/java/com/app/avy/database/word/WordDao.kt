package com.app.avy.database.word

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(word: Word)

    @Query("DELETE FROM word_table")
    fun deleteAll()

    @Query("SELECT * from word_table ORDER BY word ASC")
    fun getAllWords(): LiveData<List<Word>>

    @Query("UPDATE word_table SET `select` = :select WHERE type = :type AND id = :id")
    fun updateWord(id: String, type: String, select: Boolean)

    @Query("SELECT * from word_table WHERE type =:type ORDER BY word ASC")
    fun getWordWithId(type: String): LiveData<List<Word>>

    @Query("UPDATE word_table SET `select` = :select WHERE type = :type AND word = :word")
    fun updateWordWithType(word: String, type: String, select: Boolean)

}