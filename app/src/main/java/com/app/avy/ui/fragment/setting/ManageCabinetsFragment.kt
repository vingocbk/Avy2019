package com.app.avy.ui.fragment.setting

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

class ManageCabinetsFragment : BaseFragment(), View.OnClickListener {

    lateinit var mWordViewModel: WordViewModel
    lateinit var mAdapter: ManageCabinetsAdapter
    var mList: ArrayList<Word> = ArrayList<Word>()

    override fun getID() = R.layout.fragmemt_cabinets

    override fun onViewReady() {
        onEventClick()
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)
        initRecyclerView()
        // data result
        mWordViewModel.getAllWords().observe(this,
            Observer<List<Word>> {
                for (i in it.indices) {
                    mAdapter.setData(it)
                    mAdapter.notifyDataSetChanged()
                }
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
}