package com.app.avy.ui.dialog

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.database.word.Word
import com.app.avy.database.word.WordViewModel
import com.app.avy.ui.adapter.ItemAdapter
import kotlinx.android.synthetic.main.fragment_dialog_item.view.*

class ItemDialogFragment : DialogFragment() {

    companion object {
        val BUNDLE_DATA = "BUNDLE_DATA"
        fun getInstance(type: Int, listener: HandleViewListenner): ItemDialogFragment {
            var df = ItemDialogFragment()
            val bundle = Bundle()
            bundle.putInt(BUNDLE_DATA, type)
            df.mListener = listener
            df.arguments = bundle
            return df
        }
    }

    var mData: ArrayList<Word> = ArrayList<Word>()
    lateinit var mAdapter: ItemAdapter
    var mType: Int = 0
    lateinit var mWordViewModel: WordViewModel
    lateinit var mListener: HandleViewListenner


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_dialog_item, container, false)
        var recyclerView = v.recycler_view
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)

        arguments?.let {
            mType = it.getInt(BUNDLE_DATA)
            var data: ArrayList<Word> = ArrayList<Word>()
            mWordViewModel.getWordsWithId(mType.toString()).observe(this, Observer { it ->
                for (i in it.indices) {
                    if (it[i].select) {
                        data.add(Word(it[i].id, it[i].type, it[i].mWord, it[i].select))
                    }
                }

                if (data.size > 0) {
                    initRecyclerView(recyclerView, data)
                } else {
                    mListener.handleView(mType.toString())
                }
            })
        }
        return v
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        dialog.window.setLayout(width / 2, height / 2)
    }

    fun initRecyclerView(recyclerView: RecyclerView, list: List<Word>) {
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
        mAdapter = ItemAdapter()
        mAdapter.setData(list)
        recyclerView.adapter = mAdapter
    }

    interface HandleViewListenner {
        fun handleView(type: String)
    }
}