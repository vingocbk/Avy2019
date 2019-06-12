package com.app.avy.ui.adapter

import android.view.View
import android.view.View.inflate
import android.view.ViewGroup

import android.widget.BaseAdapter
import com.app.avy.R
import kotlinx.android.synthetic.main.spinner_items.view.*


class SpinnerAdapter : BaseAdapter() {
    var mList: List<String> = ArrayList<String>()

    var countryNames = arrayOf(1, 2, 3, 4, 5, 10)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflate(parent!!.context, R.layout.spinner_items, null)
        return view
    }

    override fun getItem(position: Int): Any {
        return countryNames[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount() = countryNames.size
}