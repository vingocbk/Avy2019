package com.app.avy.ui.fragment

import android.view.View
import com.app.avy.BaseFragment
import com.app.avy.R
import com.app.avy.listenner.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), View.OnClickListener {
    lateinit var mListener: OnItemClickListener

    companion object {
        fun newInstance(listener: OnItemClickListener): HomeFragment {
            var home = HomeFragment()
            home.mListener = listener
            return home
        }
    }

    override fun getID() = R.layout.fragment_home

    override fun onViewReady() {
        ///
        childFragmentManager.beginTransaction().add(R.id.container_weather, WeatherFragment())
            .addToBackStack(WeatherFragment::class.java.simpleName)
            .commit()
        ///
        childFragmentManager.beginTransaction().replace(R.id.container_header, HeaderFragment())
            .addToBackStack(HeaderFragment::class.java.simpleName)
            .commit()

        ///
        childFragmentManager.beginTransaction().replace(R.id.container_music, MusicFragment())
            .addToBackStack(MusicFragment::class.java.simpleName)
            .commit()
        onEvenClick()
    }

    private fun onEvenClick() {
        layout_manager!!.setOnClickListener(this)
        layout_all_app!!.setOnClickListener(this)
        layout_mart!!.setOnClickListener(this)
        layout_control!!.setOnClickListener(this)
        layout_setting!!.setOnClickListener(this)
        layout_natrition!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.layout_manager -> {
                mListener.let {
                    mListener.onItemClick(R.id.layout_manager)
                }
            }
            R.id.layout_all_app -> {
                mListener.let {
                    mListener.onItemClick(R.id.layout_all_app)
                }
            }
            R.id.layout_mart -> {
                mListener.let {
                    mListener.onItemClick(R.id.layout_mart)
                }
            }
            R.id.layout_control -> {
                mListener.let {
                    mListener.onItemClick(R.id.layout_control)
                }

            }
            R.id.layout_setting -> {
                mListener.let {
                    mListener.onItemClick(R.id.layout_setting)
                }

            }
            R.id.layout_natrition -> {
                mListener.let {
                    mListener.onItemClick(R.id.layout_natrition)
                }
            }
        }
    }

}