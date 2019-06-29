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
        const val CABINET_NUMBER_DEFAULT = "CABINET_NUMBER_DEFAULT"
        const val USER_INFO_KEY = "USER_INFO_KEY"
        const val ADVANCED_PASS = "ADVANCED_PASS"
        const val SENCITY = "SENCITY"
        const val HEADER_IP = "HEADER_IP"
        const val LASST_IP = "LASST_IP"
        const val SPEED = "SPEED"
        const val SPEED_1 = "SPEED_1"
        const val SSID = "SSID"
        const val PASSWORD = "PASSWORD"
        const val DEVICEID = "DEVICEID"
        const val IPSEND = "IPSEND"
        const val CONFIG_DEVICE = "CONFIG_DEVICE"
        const val SLOW = "SLOW"
        const val SLOW_CONFIG = "SLOW_CONFIG"
        const val DISTANT = "DISTANT"
        const val TIME_RETURN = "TIME_RETURN"
        const val TIME_SELECT = "TIME_SELECT"
        const val OPACITY_COLOR = "OPACITY_COLOR"
        const val TURN_LIGHT = "TURN_LIGHT"
        const val COLOR_HEX = "COLOR_HEX"


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
        val editor = sharedPreferences.edit()
        editor.putString(key, content)
        editor.apply()
    }

    fun getStringFromSharePreferen(key: String): String? {
        return sharedPreferences.getString(key, " ")
    }

    // -> Int
    fun storeIntInSharePreferen(key: String, content: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, content)
        editor.apply()

    }

    fun getIntFromSharePreferen(key: String): Int? {
        return sharedPreferences.getInt(key, 0)
    }

    // -> Boolean
    fun storeBooleanInSharePreferen(key: String, boolean: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, boolean)
        editor.apply()
    }

    fun getBooleanInSharePreferen(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

}