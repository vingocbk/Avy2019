package com.app.avy.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.module.FullItemModule
import kotlinx.android.synthetic.main.item_full.view.*

class FullItemAdapter : RecyclerView.Adapter<FullItemAdapter.ViewHolder>() {
    var mData: ArrayList<FullItemModule> = ArrayList<FullItemModule>()

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
        holder.tv_value.text = mData[position].value
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_value = itemView.tv_value
        var img_chose = itemView.img_chose

    }

    fun setData(list: ArrayList<FullItemModule>) {
        mData = list
    }
}