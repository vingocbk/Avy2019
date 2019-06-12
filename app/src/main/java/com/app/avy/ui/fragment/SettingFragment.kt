package com.app.avy.ui.fragment

import android.view.View
import androidx.core.content.ContextCompat
import com.app.avy.BaseFragment
import com.app.avy.R
import com.app.avy.listenner.OnItemClickListener
import com.app.avy.ui.fragment.setting.*
import kotlinx.android.synthetic.main.fragmemt_setting.*

class SettingFragment : BaseFragment(), View.OnClickListener, OnItemClickListener {

    override fun getID() = R.layout.fragmemt_setting

    override fun onViewReady() {
        setSelect(R.id.layout_info)
        childFragmentManager.beginTransaction().replace(R.id.container_setting, ProfileFragment())
            .addToBackStack(ProfileFragment::class.java.simpleName)
            .commit()
        layout_info.setOnClickListener(this)
        layout_device.setOnClickListener(this)
        layout_manage_cupboard.setOnClickListener(this)
        layout_setting_window.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.layout_info -> {
                setSelect(R.id.layout_info)
                childFragmentManager.beginTransaction().replace(R.id.container_setting, ProfileFragment())
                    .addToBackStack(ProfileFragment::class.java.simpleName)
                    .commit()
            }
            R.id.layout_device -> {
                setSelect(R.id.layout_device)
                childFragmentManager.beginTransaction().replace(R.id.container_setting, OtherDeviceFragment())
                    .addToBackStack(OtherDeviceFragment::class.java.simpleName)
                    .commit()
            }
            R.id.layout_manage_cupboard -> {
                setSelect(R.id.layout_manage_cupboard)
                childFragmentManager.beginTransaction().replace(R.id.container_setting, ManageCabinetsFragment())
                    .addToBackStack(ManageCabinetsFragment::class.java.simpleName)
                    .commit()
            }
            R.id.layout_setting_window -> {
                setSelect(R.id.layout_setting_window)
                childFragmentManager.beginTransaction()
                    .replace(R.id.container_setting, SettingCabinetFragment.getInstance(this))
                    .addToBackStack(SettingCabinetFragment::class.java.simpleName)
                    .commit()
            }
        }
    }

    override fun onItemClick(id: Int) {
        //setSelect(R.id.layout_setting_window)
        childFragmentManager.beginTransaction().replace(R.id.container_setting, AdvancedSettingFragment())
            .addToBackStack(AdvancedSettingFragment::class.java.simpleName)
            .commit()
    }

    fun setSelect(id: Int) {
        when (id) {
            R.id.layout_info -> {
                layout_info.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black_10))
                tv_info.setTextColor(ContextCompat.getColor(context!!, R.color.color_control))

                layout_device.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_device.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))
                layout_manage_cupboard.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_manage_cupboard.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))
                layout_setting_window.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_setting_window.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))

            }
            R.id.layout_device -> {
                layout_device.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black_10))
                tv_device.setTextColor(ContextCompat.getColor(context!!, R.color.color_control))

                layout_info.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_info.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))
                layout_manage_cupboard.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_manage_cupboard.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))
                layout_setting_window.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_setting_window.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))

            }

            R.id.layout_manage_cupboard -> {
                layout_manage_cupboard.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black_10))
                tv_manage_cupboard.setTextColor(ContextCompat.getColor(context!!, R.color.color_control))

                layout_info.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_info.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))
                layout_device.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_device.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))

                layout_setting_window.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_setting_window.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))
            }

            R.id.layout_setting_window -> {
                layout_setting_window.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black_10))
                tv_setting_window.setTextColor(ContextCompat.getColor(context!!, R.color.color_control))

                layout_info.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_info.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))
                layout_device.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_device.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))
                layout_manage_cupboard.setBackgroundColor(ContextCompat.getColor(context!!, R.color.color_black))
                tv_manage_cupboard.setTextColor(ContextCompat.getColor(context!!, R.color.md_grey_white))
            }
        }
    }
}

