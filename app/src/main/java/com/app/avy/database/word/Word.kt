package com.app.avy.database.word

import androidx.room.Entity
import androidx.room.ColumnInfo

@Entity(tableName = "word_table")
class Word(
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "word") var mWord: String,
    @ColumnInfo(name = "select") var select: Boolean
)