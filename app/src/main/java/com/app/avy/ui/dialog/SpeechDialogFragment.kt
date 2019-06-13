package com.app.avy.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.app.avy.MyApplication
import com.app.avy.R
import com.bumptech.glide.Glide
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_dialog_speech.view.*

class SpeechDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_dialog_speech, container, false)
        Glide.with(this).load(R.raw.voice).into(v.img_speech)
        var tv_speech = v.tv_speech
        (activity!!.getApplication() as MyApplication)
            .bus()
            .toObservable()
            .subscribe(Consumer<Any> { `object` ->
                if (`object` is String) {
                    if (`object`.isNotEmpty())
                        tv_speech.text = `object`
                }
            })
        return v
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        dialog.window.setLayout(2 * width / 5, 2 * height / 5)
    }
}