package com.app.avy.database.word

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "word_table")
@Parcelize
class Word(
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "word") var mWord: String,
    @ColumnInfo(name = "select") var select: Boolean
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var wId: Int = 0 // or foodId: Int? = null
}