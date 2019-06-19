package com.app.avy.ui.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.BaseFragment
import com.app.avy.R
import com.app.avy.database.word.Word
import com.app.avy.database.word.WordViewModel
import com.app.avy.listenner.OnItemClickListener
import com.app.avy.module.ManageModule
import com.app.avy.ui.adapter.ManageAdapter
import com.app.avy.ui.dialog.FullItemDialogFragment
import com.app.avy.ui.dialog.ItemDialogFragment
import com.app.avy.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.fragment_manage.*
import com.app.avy.MyApplication
import com.app.avy.module.AddItem
import com.app.avy.utils.Constant
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.DefaultItemAnimator


class ManageFragment : BaseFragment(), OnItemClickListener, ManageAdapter.OnItemSpinnerClickListener,
    ItemDialogFragment.HandleViewListenner, View.OnClickListener {
    val TAG = ManageFragment::class.java.simpleName

    lateinit var ft: FragmentManager
    lateinit var newFragment: DialogFragment
    lateinit var mWordViewModel: WordViewModel
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ManageAdapter
    var mList: ArrayList<ManageModule> = ArrayList<ManageModule>()
    var mCount: Int = 0
    lateinit var data: ArrayList<Word>

    var mScreenInch: Double = 0.0

    override fun getID() = R.layout.fragment_manage

    @SuppressLint("CheckResult")
    override fun onViewReady() {
        mScreenInch = Constant.getScreenInch(activity!!)
        // sample
        mCount = SharedPreferencesManager.getInstance(context!!)
            .getIntFromSharePreferen(SharedPreferencesManager.CABINET_NUMBER_DEFAULT)!!

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)
        mWordViewModel.getAllWords().observe(this, Observer {
            // mapData(it)
        })

        handleWithDevice()
        mRecyclerView = recycler_View
        initRecyclerView()
        item_1.setOnClickListener(this)
        item_2.setOnClickListener(this)
        item_3.setOnClickListener(this)

    }

    override fun onItemSpinnerClick(type: Int) {
        handleItem(type.toString())
    }

    override fun handleView(type: String) {
        // FullItemDialogFragment.getInstance(type.toString()).show(childFragmentManager, "dialog_full_item")
    }

    override fun onItemClick(id: Int) {
        FullItemDialogFragment.getInstance(id.toString()).show(childFragmentManager, "dialog_full_item")
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.item_1 -> {
                handleItem(1.toString())
            }
            R.id.item_2 -> {
                handleItem(2.toString())
            }
            R.id.item_3 -> {
                handleItem(3.toString())
            }
        }
    }

    fun initRecyclerView() {
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = GridLayoutManager(context, 4)
        mAdapter = ManageAdapter(this, this)
        mAdapter.setData(mList)
        mRecyclerView.adapter = mAdapter
    }

    fun handleItem(type: String) {
        data = ArrayList<Word>()
        mWordViewModel.getWordsWithId(type.toString()).observe(this, Observer { it ->
            for (i in it.indices) {
                if (it[i].select) {
                    data.add(Word(it[i].id, it[i].type, it[i].mWord, it[i].select))
                }
            }
        })
        val handler = Handler()
        handler.postDelayed({
            // Do something after 5s = 5000ms
            Log.e(TAG, "------->2")
            if (data.size == 0) {
                FullItemDialogFragment.getInstance(type.toString()).show(childFragmentManager, "dialog_full_item")
            } else {
                ItemDialogFragment.getInstance(type.toInt(), this).show(childFragmentManager, "dialog_item")
            }
        }, 100)
    }

    fun handleWithDevice() {
        var start: Int
        if (9 < mScreenInch && mScreenInch < 11) {
            layout_view_2.visibility = View.GONE
            layout_item_view.visibility = View.VISIBLE
            start = 4
        } else if (11 < mScreenInch && mScreenInch < 16) {
            layout_view_2.visibility = View.GONE
            layout_item_view.visibility = View.VISIBLE
            start = 4
        } else {
            start = 1
            layout_view_2.visibility = View.VISIBLE
            layout_item_view.visibility = View.GONE
        }

        for (i in start..mCount) {
            mList.add(ManageModule(i))
        }
    }
}