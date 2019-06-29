package com.app.avy.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.database.hotkey.Hotkey
import com.app.avy.listenner.OnItemHotkeyClickListener
import kotlinx.android.synthetic.main.item_control.view.*

class ControlAdapter(var listener: OnItemHotkeyClickListener) : RecyclerView.Adapter<ControlAdapter.ViewHolder>() {
    lateinit var mContext: Context
    var mData: List<Hotkey> = ArrayList<Hotkey>()
    var isStatus = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_control, parent, false))

    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mContext = holder.itemView.context
        holder.tv_hotkey.text = mData[position].hotkey
        if (mData[position].hotkey != mContext.getString(R.string.txt_hotkey)) {
            holder.tv_hotkey.background = ContextCompat.getDrawable(mContext, R.drawable.ic_border)
            holder.tv_hotkey.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_white))
        } else {
            holder.tv_hotkey.background = ContextCompat.getDrawable(mContext, R.drawable.bg_border)
            holder.tv_hotkey.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_black))
        }

        holder.itemView.setOnClickListener {
            if (mData[position].view != "View") {
                if (!isStatus) {
                    holder.tv_hotkey.background = ContextCompat.getDrawable(mContext, R.drawable.ic_border_press)
                    holder.tv_hotkey.setTextColor(ContextCompat.getColor(mContext, R.color.color_control))
                } else {
                    holder.tv_hotkey.background = ContextCompat.getDrawable(mContext, R.drawable.ic_border)
                    holder.tv_hotkey.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_white))
                }
                isStatus = !isStatus
                listener.onItemHotketClick(isStatus, mData[position].view)

            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_hotkey = itemView.tv_hotkey
    }

    fun setData(list: List<Hotkey>) {
        mData = list
    }
}