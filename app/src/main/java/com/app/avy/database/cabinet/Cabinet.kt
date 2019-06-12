package com.app.avy.database.cabinet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cabinet_table")
class Cabinet(
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "cabinet") var cabinet: String,
    @ColumnInfo(name = "select") var select: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var foodId: Int = 0 // or foodId: Int? = null
}