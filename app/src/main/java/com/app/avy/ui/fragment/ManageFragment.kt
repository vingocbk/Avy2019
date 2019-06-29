package com.app.avy.ui.fragment

import android.annotation.SuppressLint
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
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
import com.app.avy.module.ConfigData
import com.app.avy.network.MyObserver
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ManageFragment : BaseFragment(), OnItemClickListener, ManageAdapter.OnItemSpinnerClickListener,
    ItemDialogFragment.HandleViewListenner, View.OnClickListener, View.OnLongClickListener {

    val TAG: String = ManageFragment::class.java.simpleName

    lateinit var mWordViewModel: WordViewModel
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ManageAdapter
    lateinit var mPref: SharedPreferencesManager
    var mList: ArrayList<ManageModule> = ArrayList<ManageModule>()
    var mCount: Int = 0
    lateinit var data: ArrayList<Word>
    var mHeadIP: String? = null
    var mLastIP: String? = null
    var mScreenInch: Double = 0.0

    override fun getID() = R.layout.fragment_manage

    @SuppressLint("CheckResult")
    override fun onViewReady() {
        mPref = SharedPreferencesManager.getInstance(activity!!)
        mHeadIP = mPref.getStringFromSharePreferen(SharedPreferencesManager.HEADER_IP)
        mLastIP = mPref.getStringFromSharePreferen(SharedPreferencesManager.LASST_IP)

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

        item_1.setOnLongClickListener(this)
        item_2.setOnLongClickListener(this)
        item_3.setOnLongClickListener(this)

        edt_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Constant.hideKeyboard(activity!!)
                searchItem((edt_search.text!!.toString().trim()))
            }
            true
        }
    }

    override fun onItemSpinnerClick(type: Int) {
        handleItem(type.toString())
    }

    override fun handleView(type: String) {

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

    override fun onLongClick(v: View?): Boolean {
        var type: String = " "
        when (v!!.id) {
            R.id.item_1 -> {
                type = 1.toString()
            }
            R.id.item_2 -> {
                type = 2.toString()
            }
            R.id.item_3 -> {
                type = 3.toString()
            }
        }
        FullItemDialogFragment.getInstance(type).show(childFragmentManager, "dialog_full_item")
        return true
    }

    private fun initRecyclerView() {
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = GridLayoutManager(context, 4)
        mAdapter = ManageAdapter(this, this)
        mAdapter.setData(mList)
        mRecyclerView.adapter = mAdapter
    }

    private fun handleItem(type: String) {
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

    private fun handleWithDevice() {
        val start: Int
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

    private fun searchItem(word: String) {
        val item: ArrayList<String> = ArrayList()
        ConfigData.getConfig()?.let {
            val result = Constant.handleConfig(it, word).toLowerCase()
            Log.e(TAG, "-------> $result")
            mWordViewModel.seachItem(result.toUpperCase(), true).observe(this, Observer { it ->
                if (it.isNotEmpty()) {
                    for (i in it.indices) {
                        if (item.isEmpty()) {
                            item.add(it[i])
                        } else if (!item.contains(it[i])) {
                            item.add(it[i])
                        }
                    }
                    Observable.merge(
                        Constant.createOpenObservable(
                            activity!!.application as MyApplication,
                            item,
                            mHeadIP!!,
                            mLastIP!!
                        )
                    )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(MyObserver(activity!!))

                    mAdapter.setItem(item)

                    if (item.contains("1")) {
                        item_1.setBackgroundResource(R.drawable.ic_border_press)
                        img_circle_1.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_circle_press))

                        tv_view_1.setTextColor(ContextCompat.getColor(activity!!, R.color.color_control))
                        tv_count_1.setTextColor(ContextCompat.getColor(activity!!, R.color.md_grey_white))

                    }

                    if (item.contains("2")) {
                        item_2.setBackgroundResource(R.drawable.ic_border_press)
                        img_circle_2.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_circle_press))

                        tv_view_2.setTextColor(ContextCompat.getColor(activity!!, R.color.color_control))
                        tv_count_2.setTextColor(ContextCompat.getColor(activity!!, R.color.md_grey_white))

                    }

                    if (item.contains("3")) {
                        item_3.setBackgroundResource(R.drawable.ic_border_press)
                        img_circle_3.setImageDrawable(ContextCompat.getDrawable(activity!!, R.drawable.ic_circle_press))

                        tv_view_3.setTextColor(ContextCompat.getColor(activity!!, R.color.color_control))
                        tv_count_3.setTextColor(ContextCompat.getColor(activity!!, R.color.md_grey_white))

                    }
                } else {
                    Toasty.info(activity!!, "Không tìm thấy kết quả.", Toasty.LENGTH_SHORT).show()
                }
            })
        }
    }
}