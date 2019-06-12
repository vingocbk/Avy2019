package com.app.avy.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.module.AppList
import com.app.avy.ui.adapter.AllAppAdapter
import com.app.avy.utils.Constant
import kotlinx.android.synthetic.main.fragment_dialog_all_app.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.avy.MainActivity
import androidx.recyclerview.widget.DividerItemDecoration


class AllAppDialogFragment : DialogFragment() {
    var mList: List<AppList> = ArrayList<AppList>()

    lateinit var mAdapter: AllAppAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_dialog_all_app, container, false)
        val recyclerView = v.recyclerView
        mList = Constant.getInstalledApps(context!!)
        initRecyclerView(recyclerView)
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

    fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
        mAdapter = AllAppAdapter()
        mAdapter.setData(mList)
        recyclerView.adapter = mAdapter
    }

}