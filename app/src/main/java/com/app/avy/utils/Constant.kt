package com.app.avy.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.app.avy.module.AppList
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import androidx.fragment.app.FragmentActivity
import com.app.avy.BaseActivity
import android.content.Context.INPUT_METHOD_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import com.app.avy.MyApplication
import com.app.avy.module.ConfigModule
import io.reactivex.Observable


object Constant {

    const val HTTP_CONFIG = "http://192.168.4.1"

    const val HTTP = "http://"

    const val SIZE_KEY = 10

    const val BUNDLE_WEB_URL = "BUNDLE_WEB_URL"

    const val BASE_URL_WEATHER = "http://api.openweathermap.org/data/2.5/"

    const val API_KEY_WEATHER = "755e0b1311fb0c86d66bbabf8201a5c1"

    const val OFF_VOLUME = 20
    const val ON_VOLUME = 21
    const val INCREASE_VOLUME = 22
    const val REDUCTION_VOLUME = 23

    const val MONTHLY_VIEW = 1
    const val YEARLY_VIEW = 2
    const val EVENTS_LIST_VIEW = 3
    const val WEEKLY_VIEW = 4
    const val DAILY_VIEW = 5
    const val LAST_VIEW = 6
    const val DAY = 86400
    const val WEEK = 604800
    const val MONTH = 2592001    // exact value not taken into account, Joda is used for adding months and years
    const val YEAR = 31536000
    const val REMINDER_NOTIFICATION = 0
    const val REGULAR_EVENT_TYPE_ID = 1L
    const val REPEAT_ORDER_WEEKDAY_USE_LAST = 2
    const val REPEAT_ORDER_WEEKDAY = 4
    const val REPEAT_LAST_DAY = 3
    const val REPEAT_SAME_DAY = 1

    const val HOUR_MINUTES = 60
    const val DAY_MINUTES = 24 * HOUR_MINUTES
    const val WEEK_MINUTES = DAY_MINUTES * 7
    const val MONTH_MINUTES = DAY_MINUTES * 30
    const val YEAR_MINUTES = DAY_MINUTES * 365

    const val MINUTE_SECONDS = 60
    const val HOUR_SECONDS = HOUR_MINUTES * 60
    const val DAY_SECONDS = DAY_MINUTES * 60
    const val WEEK_SECONDS = WEEK_MINUTES * 60
    const val MONTH_SECONDS = MONTH_MINUTES * 60
    const val YEAR_SECONDS = YEAR_MINUTES * 60

    // special event flags
    const val FLAG_ALL_DAY = 1
    const val FLAG_IS_PAST_EVENT = 2
    const val REMINDER_OFF = -1


    const val WEEK_START_DATE_TIME = "week_start_date_time"
    const val PREFS_KEY = "Prefs"
    const val WEEK_NUMBERS = "week_numbers"
    const val START_WEEKLY_AT = "start_weekly_at"
    const val END_WEEKLY_AT = "end_weekly_at"
    const val VIBRATE = "vibrate"
    const val REMINDER_SOUND_URI = "reminder_sound_uri"
    const val REMINDER_SOUND_TITLE = "reminder_sound_title"
    const val VIEW = "view"
    const val LAST_EVENT_REMINDER_MINUTES = "reminder_minutes"
    const val LAST_EVENT_REMINDER_MINUTES_2 = "reminder_minutes_2"
    const val LAST_EVENT_REMINDER_MINUTES_3 = "reminder_minutes_3"
    const val DISPLAY_EVENT_TYPES = "display_event_types"
    const val FONT_SIZE = "font_size"
    const val LIST_WIDGET_VIEW_TO_OPEN = "list_widget_view_to_open"
    const val CALDAV_SYNC = "caldav_sync"
    const val CALDAV_SYNCED_CALENDAR_IDS = "caldav_synced_calendar_ids"
    const val LAST_USED_CALDAV_CALENDAR = "last_used_caldav_calendar"
    const val LAST_USED_LOCAL_EVENT_TYPE_ID = "last_used_local_event_type_id"
    const val DISPLAY_PAST_EVENTS = "display_past_events"
    const val REPLACE_DESCRIPTION = "replace_description"
    const val SHOW_GRID = "show_grid"
    const val LOOP_REMINDERS = "loop_reminders"
    const val DIM_PAST_EVENTS = "dim_past_events"
    const val LAST_SOUND_URI = "last_sound_uri"
    const val LAST_REMINDER_CHANNEL_ID = "last_reminder_channel_ID"
    const val REMINDER_AUDIO_STREAM = "reminder_audio_stream"
    const val USE_PREVIOUS_EVENT_REMINDERS = "use_previous_event_reminders"
    const val DEFAULT_REMINDER_1 = "default_reminder_1"
    const val DEFAULT_REMINDER_2 = "default_reminder_2"
    const val DEFAULT_REMINDER_3 = "default_reminder_3"
    const val PULL_TO_REFRESH = "pull_to_refresh"
    const val LAST_VIBRATE_ON_REMINDER = "last_vibrate_on_reminder"
    const val DEFAULT_START_TIME = "default_start_time"
    const val DEFAULT_DURATION = "default_duration"
    const val DEFAULT_EVENT_TYPE_ID = "default_event_type_id"
    const val USE_24_HOUR_FORMAT = "use_24_hour_format"
    const val SUNDAY_FIRST = "sunday_first"
    const val WEEK_START_TIMESTAMP = "week_start_timestamp"
    const val SOURCE_SIMPLE_CALENDAR = "simple-calendar"
    const val CALDAV = "Caldav"

    var itemDefault: ArrayList<String> = arrayListOf(
        "Hạt nêm",
        "Dao",
        "Kéo",
        "Muối",
        "Mỳ chính",
        "Nước mắm",
        "Dầu ăn",
        "Đũa bát",
        "Riệu",
        "Mật ong",
        "Tương",
        "Tương ớt",
        "Mù tạt",
        "Nước dừa",
        "Nghệ",
        "sả",
        "riềng",
        "gừng",
        "tỏi",
        "hành tây",
        "củ niễng",
        "hành củ",
        "nghệ",
        "củ kiệu",
        "bột đao",
        "mắm tôm",
        "mắm tép",
        "mắm tôm chua",
        "mắm rươi",
        "mắm cáy",
        "mắm cua đồng",
        "mắm bò hóc",
        "mắm ba khía",
        "mắm nêm",
        "nguyệt quế",
        "hành hoa",
        "rau răm",
        "hẹ"
    )

    fun getInstalledApps(context: Context): List<AppList> {
        val res = ArrayList<AppList>()
        val packs = context.packageManager.getInstalledPackages(0)
        for (i in packs.indices) {
            val p = packs[i]
            if (isSystemPackage(p) === false) {
                val appName = p.applicationInfo.loadLabel(context.packageManager).toString()
                val icon = p.applicationInfo.loadIcon(context.packageManager)
                Log.e("getInstalledApps", "-------> " + p.packageName)
                res.add(AppList(appName, icon, p.packageName))
            }
        }
        return res
    }

    fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM !== 0
    }

    fun openApp(context: Context, pkname: String) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(pkname)
        context.startActivity(launchIntent)
    }


    fun getDate(): String {
        val cal = Calendar.getInstance()
        val current = cal.get(Calendar.DAY_OF_WEEK)
        var day_name = ""
        var day = cal.get(Calendar.DAY_OF_MONTH).toString()
        var month = (cal.get(Calendar.MONDAY) + 1).toString()
        if (cal.get(Calendar.DAY_OF_MONTH) < 10) day = "0${cal.get(Calendar.DAY_OF_MONTH)}"
        if (cal.get(Calendar.MONDAY) + 1 < 10) month = "0${cal.get(Calendar.MONDAY) + 1}"

        Log.e("current", "------>" + current)
        when (current) {
            1 -> day_name = "Chủ nhật"
            2 -> day_name = "Thứ hai"
            3 -> day_name = "Thứ ba"
            4 -> day_name = "Thứ tư"
            5 -> day_name = "Thứ năm"
            6 -> day_name = "Thứ sáu"
            7 -> day_name = "Thứ bảy"
        }

        return "$day_name, $day tháng $month"
    }

    fun verifyMail(mail: String?): String {
        return if (mail == null || mail.trim { it <= ' ' }.isEmpty()) {
            "Email không được để trống."
        } else {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(mail.trim { it <= ' ' }).matches()) {
                ""
            } else {
                "Email không hợp lệ."
            }
        }
    }

    fun validCellPhone(number: String): String {
        return if (number == null || number.trim { it <= ' ' }.isEmpty()) {
            "Số điện thoại không được để trống."
        } else {
            if (Pattern.compile("^([0-9]{10,13}$)").matcher(number).matches()) {
                ""
            } else {
                "Số điện thoại không hợp lệ."
            }
        }
    }

    fun showSoftKeyboard(view: View, context: Context) {
        if (view.requestFocus()) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideKeyboard(activity: FragmentActivity) {
        val view = activity.currentFocus
        if (view != null) {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


    fun getScreenInch(context: FragmentActivity): Double {
        val dm = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        val wi = width.toDouble() / dm.xdpi
        val hi = height.toDouble() / dm.ydpi
        val x = Math.pow(wi, 2.0)
        val y = Math.pow(hi, 2.0)
        return Math.sqrt(x + y)
    }

    fun getNowSeconds() = System.currentTimeMillis() / 1000L

    fun handleConfig(data: ConfigModule, word: String): String {
        var result = word
        for (i in data.all.indices) {
            for (j in data.all[i].models.indices) {
                if (result.toUpperCase().contains(data.all[i].models[j].toUpperCase())) {
                    result = result.toUpperCase()
                        .replace(data.all[i].models[j].toUpperCase(), data.all[i].name.toUpperCase())
                }
            }
        }
        Log.e("Constant", "handleConfig--- $result")
        return result
    }

     fun createOpenObservable(
        application: MyApplication,
        item: ArrayList<String>,
        headIP: String,
        lastIP: String
    ): List<Observable<*>> {
        val result = ArrayList<Observable<*>>()
        for (i in item.indices) {
            result.add(
                application.retrofitHelper()
                    .getNetworkService("$HTTP$headIP".plus(lastIP.toInt() + item[i].toInt()))
                    .openWindow()
            )
        }
        return result
    }

     fun createCloseObservable(
        application: MyApplication,
        item: ArrayList<String>,
        headIP: String,
        lastIP: String
    ): List<Observable<*>> {
        val result = ArrayList<Observable<*>>()
        for (i in item.indices) {
            result.add(
                application.retrofitHelper()
                    .getNetworkService("$HTTP$headIP".plus(lastIP.toInt() + item[i].toInt()))
                    .closeWindow()
            )
        }
        return result
    }

}