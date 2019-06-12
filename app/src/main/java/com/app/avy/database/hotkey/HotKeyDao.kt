package com.app.avy.database.hotkey

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HotKeyDao {
    @Insert
    fun insert(hotkey: Hotkey)

    @Query("DELETE FROM hotkey_table")
    fun deleteAll()

    @Query("SELECT * FROM hotkey_table")
    fun getAllHotkey(): LiveData<List<Hotkey>>

    @Query("UPDATE hotkey_table SET hotkey = :hotkey, `view`= :view, save = :save WHERE id = :id")
    fun updateHotkey(id: String, hotkey: String, view: String, save: Boolean)

}