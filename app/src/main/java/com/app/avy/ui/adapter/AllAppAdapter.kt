package com.app.avy.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.module.AppList
import com.app.avy.utils.Constant
import kotlinx.android.synthetic.main.item_all_app.view.*

class AllAppAdapter : RecyclerView.Adapter<AllAppAdapter.ViewHolder>() {

    var mList: List<AppList> = ArrayList<AppList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_all_app, parent, false))
    }

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.img_icon_app.setImageDrawable(mList[position].icon)
        holder.tv_name_app.text = mList[position].name
        holder.itemView.setOnClickListener {
            Constant.openApp(holder.itemView.context, mList[position].pkname)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_name_app = itemView.tv_name_app
        var img_icon_app = itemView.img_icon_app
    }

    fun setData(list: List<AppList>) {
        mList = list
    }


}