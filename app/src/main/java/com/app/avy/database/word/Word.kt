package com.app.avy.database.word

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo


@Entity(tableName = "word_table")
class Word(
    @ColumnInfo(name = "word") val mWord: String
) {
    @PrimaryKey(autoGenerate = true)
    var foodId: Int = 0 // or foodId: Int? = null
}