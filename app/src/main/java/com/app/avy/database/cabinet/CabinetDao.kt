package com.app.avy.database.cabinet

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CabinetDao {
    @Insert
    fun insert(cabinet: Cabinet)

    @Query("DELETE FROM cabinet_table")
    fun deleteAll()

    @Query("SELECT * FROM cabinet_table")
    fun getAllCabinet(): LiveData<List<Cabinet>>

    @Query("UPDATE cabinet_table SET `select` = :select WHERE type = :type AND id = :id")
    fun updateCabinet(id: String, type: String, select: Boolean)
}