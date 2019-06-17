package com.app.avy.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.avy.database.cabinet.Cabinet
import com.app.avy.database.cabinet.CabinetDao
import com.app.avy.database.hotkey.HotKeyDao
import com.app.avy.database.hotkey.Hotkey
import com.app.avy.database.word.Word
import com.app.avy.database.word.WordDao


@Database(entities = [Word::class, Hotkey::class, Cabinet::class], version = 6)
abstract class AvyRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao
    abstract fun hotkeyDao(): HotKeyDao
    abstract fun cabinetDao(): CabinetDao

    companion object {
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE hotkey_table (id TEXT, hotkey TEXT, `view` TEXT,save Boolean, PRIMARY KEY(id))")
                //  database.execSQL("INSERT INTO hotkey_table (id, hotkey, `view`,save ) SELECT id, hotkey, `view`,save FROM hotkey_table")
            }
        }

        private var instance: AvyRoomDatabase? = null
        fun getDatabase(context: Context): AvyRoomDatabase {
            if (instance == null) {
                synchronized(AvyRoomDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AvyRoomDatabase::class.java, "word_database"
                        )
                            .fallbackToDestructiveMigration()
                           // .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return instance!!
        }

        private val sRoomDatabaseCallback = object : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                //PopulateDbAsync(instance!!).execute()
            }
        }
    }




}