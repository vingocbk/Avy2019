package com.app.avy.database.hotkey

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotkey_table")
class Hotkey(
    @PrimaryKey @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "hotkey") var hotkey: String,
    @ColumnInfo(name = "view") var view: String,
    @ColumnInfo(name = "save") var isSave: Boolean

)