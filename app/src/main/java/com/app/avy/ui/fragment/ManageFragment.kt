package com.app.avy.ui.fragment

import android.view.View
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.BaseFragment
import com.app.avy.R
import com.app.avy.listenner.OnItemClickListener
import com.app.avy.module.ManageModule
import com.app.avy.ui.adapter.ManageAdapter
import com.app.avy.ui.dialog.FullItemDialogFragment
import com.app.avy.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.fragment_manage.*

class ManageFragment : BaseFragment(), OnItemClickListener {
    lateinit var ft: FragmentManager
    lateinit var newFragment: DialogFragment

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ManageAdapter
    var mList: ArrayList<ManageModule> = ArrayList<ManageModule>()

    override fun getID() = R.layout.fragment_manage

    override fun onViewReady() {
        // sample
        val count = SharedPreferencesManager.getInstance(context!!)
            .getIntFromSharePreferen(SharedPreferencesManager.CABINET_NUMBER_DEFAULT)
        for (i in 4..count!!) {
            mList.add(ManageModule(i, null))
        }

        mRecyclerView = recycler_View
        initRecyclerView()
    }

    override fun onItemClick(id: Int) {
        FullItemDialogFragment().show(childFragmentManager, "dialog_advanced")

    }

    fun initRecyclerView() {
        mRecyclerView.layoutManager = GridLayoutManager(context, 4)
        mAdapter = ManageAdapter(this)
        mAdapter.setData(mList)
        mRecyclerView.adapter = mAdapter
    }

}