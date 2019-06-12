package com.app.avy.ui.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.app.avy.BaseFragment
import com.app.avy.R
import com.app.avy.database.hotkey.HokeyViewModle
import com.app.avy.listenner.OnChildItemClickListener
import com.app.avy.ui.adapter.ControlAdapter
import kotlinx.android.synthetic.main.fragment_control.*

class ControlFragment : BaseFragment(), View.OnClickListener {

    lateinit var mListener: OnChildItemClickListener
    lateinit var mHotkeyViewModel: HokeyViewModle
    lateinit var mAdapter: ControlAdapter

    companion object {
        fun newInstance(listener: OnChildItemClickListener): ControlFragment {
            var home = ControlFragment()
            home.mListener = listener
            return home
        }
    }

    override fun getID() = R.layout.fragment_control
    override fun onViewReady() {
        initRecyclerView()
        mHotkeyViewModel = ViewModelProviders.of(this).get(HokeyViewModle::class.java)
        mHotkeyViewModel.getAllWords().observe(this, Observer {
            mAdapter.setData(it)
            mAdapter.notifyDataSetChanged()
        })
        layout_setup.setOnClickListener(this)
        progress.progressChangedCallback = {
            tv_progress.text = (it * 100).toInt().toString() + "%"
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.layout_setup -> {
                mListener.let {
                    it.inChildItemClick(R.id.layout_setup)
                }
            }
        }
    }

    fun initRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(context, 5)
        mAdapter = ControlAdapter()
        recyclerView.adapter = mAdapter
    }
}