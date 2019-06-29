package com.app.avy

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.app.avy.ui.dialog.SpeechDialogFragment

abstract class BaseActivity : AppCompatActivity() {

    lateinit var ft: FragmentManager
    lateinit var newFragment: DialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // finally change the color
        ft = supportFragmentManager
        newFragment = SpeechDialogFragment()
        // setupOnSystemVisibilityChangeListener()
        setContentView(getId())
        onViewReady()
    }

    abstract fun getId(): Int

    abstract fun onViewReady()

    fun dismiss() {
        val handler = Handler()
        handler.postDelayed({
            try {
                if (newFragment.isAdded) {
                    newFragment.dismissAllowingStateLoss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 500)
    }

    fun showDialog() {
        try {
            if (!newFragment.isAdded) {
                newFragment.show(ft, "dialog")
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun setupOnSystemVisibilityChangeListener() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return
        }
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                // The system bars are visible. Make any desired
                hideSystemUI()
            }
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            hideSystemUIKitKat()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            hideSystemUIJellyBean()
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun hideSystemUIKitKat() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun hideSystemUIJellyBean() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LOW_PROFILE)
    }

}