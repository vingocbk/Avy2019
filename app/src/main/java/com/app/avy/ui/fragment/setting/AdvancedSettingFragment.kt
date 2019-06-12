package com.app.avy.ui.fragment.setting

import android.widget.ArrayAdapter
import com.app.avy.BaseFragment
import com.app.avy.R
import com.app.avy.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.fragment_advance_setting.*

class AdvancedSettingFragment : BaseFragment() {
    override fun getID() = R.layout.fragment_advance_setting

    override fun onViewReady() {

        val count = SharedPreferencesManager.getInstance(context!!)
            .getIntFromSharePreferen(SharedPreferencesManager.CABINET_NUMBER_DEFAULT)
        val categories = ArrayList<Int>()
        for (i in 1..count!!) {
            categories.add(i)
        }


        var adapter = ArrayAdapter<Int>(
            context,
            android.R.layout.simple_dropdown_item_1line, categories
        )

        spinner_slow.setAdapter(adapter)
        spinner_speed_1.setAdapter(adapter)
        spinner_speed.setAdapter(adapter)
        spinner_return.setAdapter(adapter)
        spinner_distant.setAdapter(adapter)

    }

}