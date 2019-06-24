package com.app.avy.utils

import android.content.Context
import android.text.format.DateFormat
import com.app.avy.utils.Constant.PREFS_KEY
import com.app.avy.utils.Constant.SUNDAY_FIRST
import com.app.avy.utils.Constant.USE_24_HOUR_FORMAT
import java.util.*

open class BaseConfig(val context: Context) {
    protected val prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    companion object {
        fun newInstance(context: Context) = BaseConfig(context)
    }

    var use24HourFormat: Boolean
        get() = prefs.getBoolean(USE_24_HOUR_FORMAT, DateFormat.is24HourFormat(context))
        set(use24HourFormat) = prefs.edit().putBoolean(USE_24_HOUR_FORMAT, use24HourFormat).apply()



    var isSundayFirst: Boolean
        get() {
            val isSundayFirst = Calendar.getInstance(Locale.getDefault()).firstDayOfWeek == Calendar.SUNDAY
            return prefs.getBoolean(SUNDAY_FIRST, isSundayFirst)
        }
        set(sundayFirst) = prefs.edit().putBoolean(SUNDAY_FIRST, sundayFirst).apply()

}
