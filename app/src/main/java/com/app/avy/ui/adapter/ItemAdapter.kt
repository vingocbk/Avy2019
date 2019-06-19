package com.app.avy.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.database.word.Word
import kotlinx.android.synthetic.main.item_item.view.*

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    var mData: List<Word> = ArrayList<Word>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = mData.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_value.text = mData[position].mWord
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_value = itemView.tv_value
    }

    fun setData(list: List<Word>) {
        mData = list
    }
}