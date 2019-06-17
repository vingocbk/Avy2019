package com.app.avy.ui.fragment

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
import com.app.avy.ui.adapter.ManageSpinnerAdapter
import com.app.avy.ui.dialog.FullItemDialogFragment
import com.app.avy.ui.view.spinner.MaterialBetterSpinner
import com.app.avy.utils.SharedPreferencesManager
import kotlinx.android.synthetic.main.fragment_manage.*

class ManageFragment : BaseFragment(), OnItemClickListener, ManageAdapter.OnItemSpinnerClickListener {

    lateinit var ft: FragmentManager
    lateinit var newFragment: DialogFragment
    lateinit var mWordViewModel: WordViewModel

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ManageAdapter
    var mList: ArrayList<ManageModule> = ArrayList<ManageModule>()
    var mCount: Int = 0

    override fun getID() = R.layout.fragment_manage

    override fun onViewReady() {
        // sample
        mCount = SharedPreferencesManager.getInstance(context!!)
            .getIntFromSharePreferen(SharedPreferencesManager.CABINET_NUMBER_DEFAULT)!!

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)
        mWordViewModel.getAllWords().observe(this, Observer {
            // mapData(it)
        })

        for (i in 4..mCount) {
            mList.add(ManageModule(i))
        }

        mRecyclerView = recycler_View
        initRecyclerView()
    }

    override fun onItemSpinnerClick(type: Int, spinner: MaterialBetterSpinner) {
        var data: ArrayList<Word> = ArrayList<Word>()
        mWordViewModel.getWordsWithId(type.toString()).observe(this, Observer {
            for (i in it.indices) {
                if (it[i].select) {
                    data.add(Word(it[i].id, it[i].type, it[i].mWord, it[i].select))
                }
            }
        })

        val spinnerAdapter = ManageSpinnerAdapter(context, data)
        spinner.setAdapter(spinnerAdapter)
        if (spinner.isPopup) {
            spinner.dismissDropDown()
            spinner.isPopup = false
        } else {
            spinner.requestFocus()
            spinner.showDropDown()
            spinner.isPopup = true
        }

    }

    override fun onItemClick(id: Int) {
        FullItemDialogFragment.getInstance(id.toString()).show(childFragmentManager, "dialog_advanced")
    }

    fun initRecyclerView() {
        mRecyclerView.layoutManager = GridLayoutManager(context, 4)
        mAdapter = ManageAdapter(this, this)
        mAdapter.setData(mList)
        mRecyclerView.adapter = mAdapter
    }

    /*fun mapData(list: List<Word>) {
        var temp: ArrayList<Word>
        for (i in 4..mCount) {
            temp = ArrayList<Word>()
            for (j in list.indices) {
                if (list[j].type.toInt() == i) {
                    temp.add(Word(list[j].id, list[j].type, list[j].mWord, list[j].select))
                }
            }
            mList.add(ManageModule(i, temp))
        }

        mAdapter.setData(mList)
        mAdapter.notifyDataSetChanged()

    }*/

}