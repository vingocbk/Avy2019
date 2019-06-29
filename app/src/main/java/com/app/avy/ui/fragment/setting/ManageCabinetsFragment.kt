package com.app.avy.ui.fragment.setting

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.app.avy.BaseFragment
import com.app.avy.R
import com.app.avy.database.word.WordViewModel
import androidx.lifecycle.ViewModelProviders
import com.app.avy.database.word.Word
import com.app.avy.ui.adapter.ManageCabinetsAdapter
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragmemt_cabinets.*
import android.view.View

class ManageCabinetsFragment : BaseFragment(), View.OnClickListener, TextWatcher {

    lateinit var mWordViewModel: WordViewModel
    lateinit var mAdapter: ManageCabinetsAdapter
    var mList: ArrayList<Word> = ArrayList<Word>()

    override fun getID() = R.layout.fragmemt_cabinets

    override fun onViewReady() {
        edt_search.addTextChangedListener(this)
        onEventClick()
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)
        initRecyclerView()
        // data result
        mWordViewModel.getWordsWithId(1.toString()).observe(this,
            Observer<List<Word>> {
                mAdapter.setData(it)
                mAdapter.notifyDataSetChanged()
            })
    }

    fun initRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        mAdapter = ManageCabinetsAdapter()
        mAdapter.setData(mList)
        recyclerView.adapter = mAdapter
    }

    fun onEventClick() {
        fab.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fab -> {

            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s!!.trim().toString().isNotEmpty()) {
            mWordViewModel.searchItemInCabinet("%${s.trim().toString().toUpperCase()}%", 1.toString())
                .observe(this, Observer {
                    mAdapter.setData(it)
                    mAdapter.notifyDataSetChanged()
                })
        } else {
            mWordViewModel.getWordsWithId(1.toString()).observe(this, Observer {
                mAdapter.setData(it)
                mAdapter.notifyDataSetChanged()
            })
        }
    }


}