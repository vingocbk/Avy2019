package com.app.avy.ui.fragment.setting

import com.app.avy.BaseFragment
import com.app.avy.R
import kotlinx.android.synthetic.main.fragmemt_device.*

class OtherDeviceFragment : BaseFragment() {
    override fun getID() = R.layout.fragmemt_device

    override fun onViewReady() {
        switch_button.isChecked = true
        switch_button.isChecked
        switch_button.toggle()     //switch state
        switch_button.toggle(true)//switch without animation
        switch_button.setShadowEffect(true)//disable shadow effect
        switch_button.isEnabled = true//disable button
        switch_button.setEnableEffect(true)//disable the switch animation
        switch_button.setOnCheckedChangeListener { view, isChecked ->
            //TODO do your job
        }
    }
}