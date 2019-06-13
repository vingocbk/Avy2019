package com.app.avy

import android.os.Bundle
import android.os.Handler
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
                    newFragment.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 1000)
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

}