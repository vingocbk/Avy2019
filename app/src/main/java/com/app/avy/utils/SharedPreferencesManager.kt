package com.app.avy.utils

import android.content.Context
import android.content.SharedPreferences


class SharedPreferencesManager(context: Context) {
    val SHARE_PREFEREN_KEY = "com.app.avy"

    var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(SHARE_PREFEREN_KEY, Context.MODE_PRIVATE)
    }

    companion object {

        val CABINET_NUMBER_DEFAULT = "CABINET_NUMBER_DEFAULT"
        val USER_INFO_KEY = "USER_INFO_KEY"
        val ADVANCED_PASS = "ADVANCED_PASS"
        val SENCITY = "SENCITY"


        var instance: SharedPreferencesManager? = null
        fun getInstance(context: Context): SharedPreferencesManager {
            if (null == instance) {
                instance = SharedPreferencesManager(context.applicationContext)
            }
            return instance as SharedPreferencesManager
        }
    }


    // ->  String
    fun storeStringInSharePreferen(key: String, content: String) {
        var editor = sharedPreferences.edit()
        editor.putString(key, content)
        editor.apply()
    }

    fun getStringFromSharePreferen(key: String): String? {
        return sharedPreferences.getString(key, "")
    }
    // -> Int

    fun storeIntInSharePreferen(key: String, content: Int) {
        var editor = sharedPreferences.edit()
        editor.putInt(key, content)
        editor.apply()
    }

    fun getIntFromSharePreferen(key: String): Int? {
        return sharedPreferences.getInt(key, 0)
    }

}