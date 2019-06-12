package com.app.avy.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.database.word.Word
import kotlinx.android.synthetic.main.item_word.view.*

class ManageCabinetsAdapter : RecyclerView.Adapter<ManageCabinetsAdapter.ViewHolder>() {
    var mList: List<Word> = ArrayList<Word>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false))
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_word.text = mList[position].mWord
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_word = itemView.tv_word

    }

    fun setData(list: List<Word>) {
        mList = list
    }


}