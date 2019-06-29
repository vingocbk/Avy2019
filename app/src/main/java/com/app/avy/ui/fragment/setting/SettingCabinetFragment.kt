package com.app.avy.ui.fragment.setting

import android.content.Context
import android.media.AudioManager
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SeekBar
import com.app.avy.BaseFragment
import com.app.avy.R
import com.app.avy.listenner.OnItemClickListener
import com.app.avy.ui.dialog.AdvancedDialogFragment
import com.app.avy.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.fragment_setting_cabinet.*

class SettingCabinetFragment : BaseFragment(), View.OnClickListener, SeekBar.OnSeekBarChangeListener,
    View.OnTouchListener {
    lateinit var listener: OnItemClickListener

    private var audioManager: AudioManager? = null

    var type: ArrayList<String> = arrayListOf("Lady", "B", "C")
    var language: ArrayList<String> = arrayListOf("Tiếng việt", "Tiếng Anh")
    var listCount: ArrayList<Int> = ArrayList<Int>()


    companion object {
        fun getInstance(listener: OnItemClickListener): SettingCabinetFragment {
            var fm = SettingCabinetFragment()
            fm.listener = listener
            return fm
        }
    }

    override fun getID() = R.layout.fragment_setting_cabinet


    override fun onViewReady() {
        tv_setting_advanced.setOnClickListener(this)
        root_setting_cabin.setOnTouchListener(this)
        setDataSpinner()

        /*audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        seek_bar_volume.setOnSeekBarChangeListener(this)
        seek_bar_volume.max = audioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        seek_bar_volume.progress = audioManager!!.getStreamVolume(AudioManager.STREAM_MUSIC)*/

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_setting_advanced -> {
                AdvancedDialogFragment(listener).show(childFragmentManager, "dialog_advanced")
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?) = true

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        audioManager!!.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            progress, 0
        )
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }


    fun setDataSpinner() {
        val count = SharedPreferencesManager.getInstance(context!!)
            .getIntFromSharePreferen(SharedPreferencesManager.CABINET_NUMBER_DEFAULT)

        for (i in 1..count!!) {
            listCount.add(i)
        }

        val adapterCount = ArrayAdapter<Int>(context, android.R.layout.simple_dropdown_item_1line, listCount)

        val adapterType = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, type)

        val adapterLanguage = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, language)

        spinner_type.setAdapter(adapterType)
        spinner_count_cabinet.setAdapter(adapterCount)
        spinner_count_control.setAdapter(adapterCount)
        spinner_language.setAdapter(adapterLanguage)

    }

}
