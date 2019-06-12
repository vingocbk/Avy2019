package com.app.avy.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.app.avy.module.AppList
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentActivity


object Constant {

    val SIZE_KEY = 10

    val BUNDLE_WEB_URL = "BUNDLE_WEB_URL"

    val BASE_URL_WEATHER = "http://api.openweathermap.org/data/2.5/"

    val API_KEY_WEATHER = "755e0b1311fb0c86d66bbabf8201a5c1"


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
}