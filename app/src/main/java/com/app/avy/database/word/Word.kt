package com.app.avy.database.word

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
class Word(
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "word") var mWord: String,
    @ColumnInfo(name = "select") var select: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var wId: Int = 0 // or foodId: Int? = null
}