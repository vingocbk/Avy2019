package com.app.avy.ui.fragment

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.avy.BaseFragment
import com.app.avy.R
import kotlinx.android.synthetic.main.fragment_hot_key.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.database.cabinet.Cabinet
import com.app.avy.database.cabinet.CabinetViewModle
import com.app.avy.database.hotkey.HokeyViewModle
import com.app.avy.database.hotkey.Hotkey
import com.app.avy.listenner.OnItemSaveKeyListenner
import com.app.avy.module.HotkeyModule
import com.app.avy.ui.adapter.HotKeyAdapter
import es.dmoral.toasty.Toasty

class SetupHotkeyFragment : BaseFragment(), OnItemSaveKeyListenner, View.OnClickListener {
    val TAG = SetupHotkeyFragment::class.java.simpleName
    lateinit var mHotkeyViewModel: HokeyViewModle
    lateinit var mCabinetViewModle: CabinetViewModle
    lateinit var mAdapter: HotKeyAdapter
    var isSave: Boolean = false

    var mListKey: List<Hotkey> = ArrayList<Hotkey>()
    var mListCabinet: List<Cabinet> = ArrayList<Cabinet>()

    var mData: List<HotkeyModule> = ArrayList<HotkeyModule>()

    override fun getID() = R.layout.fragment_hot_key

    override fun onViewReady() {
        mHotkeyViewModel = ViewModelProviders.of(this).get(HokeyViewModle::class.java)
        mCabinetViewModle = ViewModelProviders.of(this).get(CabinetViewModle::class.java)
        setDefaultHotkey()
        initRecyclerView()
        save.setOnClickListener(this)
        delete.setOnClickListener(this)
    }

    override fun onItemSaveKey(isSave: Boolean, listKey: List<HotkeyModule>) {
        this.isSave = isSave
        mData = listKey
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.save -> {
                if (isSave) {
                    updateData(true, mData)
                    activity!!.supportFragmentManager.popBackStack()
                } else {
                    context?.let {
                        Toasty.info(it, "Hãy ấn tích để sãn sàng lưu thông tin.", Toast.LENGTH_SHORT, true).show()
                    }
                }
            }

            R.id.delete -> {
                if (isSave) {
                    updateData(false, mData)
                    activity!!.supportFragmentManager.popBackStack()
                } else {
                    context?.let {
                        Toasty.info(it, "Hãy ấn tích để sãn sàng xóa thông tin.", Toast.LENGTH_SHORT, true).show()
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        mAdapter = HotKeyAdapter(this)
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = mAdapter
    }

    fun setDefaultHotkey() {
        mHotkeyViewModel.getAllWords().observe(this, Observer {
            if (it.isNotEmpty()) {
                mListKey = it
            }
        })

        mCabinetViewModle.getAllCabinet().observe(this, Observer {
            if (it.isNotEmpty()) {
                mListCabinet = it
                mAdapter.setData(mapData())
                mAdapter.notifyDataSetChanged()
            }
        })
    }

    fun mapData(): ArrayList<HotkeyModule> {
        val mData: ArrayList<HotkeyModule> = ArrayList<HotkeyModule>()
        var temp: ArrayList<Cabinet>
        if (mListKey.isNotEmpty() && mListCabinet.isNotEmpty()) {
            for (i in mListKey.indices) {
                temp = ArrayList<Cabinet>()
                for (j in mListCabinet.indices) {
                    if ((i + 1).toString() == mListCabinet[j].type) {
                        temp.add(mListCabinet[j])
                    }
                }
                mData.add(
                    HotkeyModule(
                        mListKey[i].id,
                        mListKey[i].hotkey,
                        mListKey[i].view,
                        mListKey[i].isSave,
                        temp
                    )
                )
                Log.e(TAG, "--->" + mData[i])
            }
        }
        return mData
    }

    fun updateData(isSave: Boolean, data: List<HotkeyModule>) {
        var cabinet: Cabinet

        for (i in data.indices) {
            if (data[i].isSave) {
                if (isSave) {
                    mHotkeyViewModel.updateHotkey(Hotkey(data[i].id, data[i].key, data[i].view, false))
                } else {
                    mHotkeyViewModel.updateHotkey(
                        Hotkey(
                            data[i].id,
                            getString(R.string.txt_hotkey),
                            getString(R.string.txt_view),
                            false
                        )
                    )
                }
                for (j in data[i].listCabinet.indices) {
                    cabinet = data[i].listCabinet[j]
                    if (cabinet.select) {
                        if (isSave) {
                            mCabinetViewModle.updateCabinet(cabinet)
                        } else {
                            mCabinetViewModle.updateCabinet(
                                Cabinet(
                                    cabinet.id,
                                    cabinet.type,
                                    "Tủ ${j.toString()}",
                                    false
                                )
                            )
                        }

                    }
                }
            }
        }
    }
}