package com.app.avy.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.database.word.Word
import kotlinx.android.synthetic.main.item_full.view.*

class FullItemAdapter : RecyclerView.Adapter<FullItemAdapter.ViewHolder>() {
    var mData: List<Word> = ArrayList<Word>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_full,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_value.text = mData[position].mWord
        holder.img_chose.visibility = if (mData.get(position).select) View.VISIBLE else View.INVISIBLE
        holder.root_item_full.setOnClickListener {
            holder.img_chose.visibility = if (!mData[position].select) View.VISIBLE else View.INVISIBLE
            mData[position].select = !mData[position].select
            Log.e("FullItemAdapter", "-------->" + mData[position].select)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_value = itemView.tv_value
        var img_chose = itemView.img_chose
        var root_item_full = itemView.root_item_full

    }

    fun setData(list: List<Word>) {
        mData = list
    }

    fun getData(): List<Word> {
        return mData
    }
}