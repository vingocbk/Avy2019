package com.app.avy.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
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
import com.app.avy.ui.adapter.FullItemAdapter
import kotlinx.android.synthetic.main.fragment_dialog_full_item.view.*


class FullItemDialogFragment : DialogFragment(), TextWatcher {

    val TAG = FullItemDialogFragment::class.java.simpleName

    companion object {
        const val BUNDLE_TYPE = "BUNDLE_TYPE"
        fun getInstance(type: String): FullItemDialogFragment {
            val df = FullItemDialogFragment()
            val bundle = Bundle()
            bundle.putString(BUNDLE_TYPE, type)
            df.arguments = bundle
            return df
        }
    }

    private var mType = ""
    private lateinit var mAdapter: FullItemAdapter
    lateinit var mWordViewModel: WordViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_dialog_full_item, container, false)
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)
        val recyclerView = v.recycler_View
        val edtSearch = v.edt_search
        arguments?.let {
            mType = it.getString(BUNDLE_TYPE)
            mWordViewModel.getWordsWithId(mType).observe(this, Observer {
                initRecyclerView(recyclerView, it)
            })
        }

        v.tv_update.setOnClickListener {
            for (i in mAdapter.getData().indices) {
                val word = mAdapter.getData()[i]
                mWordViewModel.updateWord(Word(word.id, word.type, word.mWord, word.select))
            }
            dismiss()
        }

        edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

            }
            true
        }
        edtSearch.addTextChangedListener(this)
        return v
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.e(TAG, "------> $s $mType")
        if (s!!.trim().toString().isNotEmpty()) {
            mWordViewModel.searchItemInCabinet("%${s.trim().toString().toUpperCase()}%", mType).observe(this, Observer {
                Log.e(TAG, "result--------> $it")
                mAdapter.setData(it)
                mAdapter.notifyDataSetChanged()
            })
        } else {
            mWordViewModel.getWordsWithId(mType).observe(this, Observer {
                mAdapter.setData(it)
                mAdapter.notifyDataSetChanged()
            })
        }
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        dialog.window.setLayout(width / 2, height / 2)
    }

    private fun initRecyclerView(recyclerView: RecyclerView, list: List<Word>) {
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))
        mAdapter = FullItemAdapter()
        mAdapter.setData(list)
        recyclerView.adapter = mAdapter
    }
}