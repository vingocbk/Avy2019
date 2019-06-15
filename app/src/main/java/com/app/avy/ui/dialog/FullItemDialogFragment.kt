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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.module.FullItemModule
import com.app.avy.ui.adapter.FullItemAdapter
import kotlinx.android.synthetic.main.fragment_dialog_full_item.view.*

class FullItemDialogFragment : DialogFragment() {
    lateinit var mAdapter: FullItemAdapter
    var mList: List<FullItemModule> = ArrayList<FullItemModule>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_dialog_full_item, container, false)
        val recyclerView = v.recycler_View
        initRecyclerView(recyclerView)
        return v
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        dialog.window.setLayout( width / 2,  height / 2)
    }

    fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
        mAdapter = FullItemAdapter()
        mAdapter.setData(mList as ArrayList<FullItemModule>)
        recyclerView.adapter = mAdapter
    }

}