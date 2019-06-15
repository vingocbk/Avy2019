package com.app.avy.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.listenner.OnItemClickListener
import com.app.avy.module.ManageModule
import kotlinx.android.synthetic.main.item_cabinet.view.*

class ManageAdapter(var listener: OnItemClickListener) : RecyclerView.Adapter<ManageAdapter.ViewHolder>() {
    lateinit var spinnerAdapter: ManageSpinnerAdapter

    var mData: List<ManageModule> = ArrayList<ManageModule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_cabinet,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_count.text = mData[position].count.toString()

        spinnerAdapter =
            ManageSpinnerAdapter(holder.itemView.context, R.layout.item_manage_spinner, mData[position].items)
        holder.spinner_key.setAdapter(spinnerAdapter)


        holder.itemView.setOnClickListener {
            listener.onItemClick(holder.tv_count.id)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_count = itemView.tv_count
        var spinner_key = itemView.spinner_key
    }

    fun setData(list: ArrayList<ManageModule>) {
        mData = list
    }
}