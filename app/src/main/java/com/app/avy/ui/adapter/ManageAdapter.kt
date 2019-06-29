package com.app.avy.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.database.word.WordViewModel
import com.app.avy.listenner.OnItemClickListener
import com.app.avy.module.ManageModule
import com.app.avy.ui.view.spinner.MaterialBetterSpinner
import kotlinx.android.synthetic.main.item_cabinet.view.*
import kotlin.collections.ArrayList
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService


class ManageAdapter(var listener: OnItemClickListener, var listener1: OnItemSpinnerClickListener) :
    RecyclerView.Adapter<ManageAdapter.ViewHolder>() {
    var mData: List<ManageModule> = ArrayList<ManageModule>()
    lateinit var mWordViewModel: WordViewModel
    var mListItemSelect: List<String> = ArrayList<String>()

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_count.text = mData[position].count.toString()

        holder.itemView.setOnClickListener {
            listener1.onItemSpinnerClick(mData[position].count)
        }

        holder.itemView.setOnLongClickListener {
            listener.onItemClick(mData[position].count)
            return@setOnLongClickListener true
        }

        for (i in mListItemSelect.indices) {
            if (mData[position].count == mListItemSelect[i].toInt()) {
                holder.root_view.setBackgroundResource(R.drawable.ic_border_press)
                holder.img_circle.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.ic_circle_press
                    )
                )

                holder.tv_view.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.color_control))
                holder.tv_count.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.md_grey_white))


            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_count = itemView.tv_count
        val root_view = itemView.root_view
        val img_circle = itemView.img_circle
        val tv_view = itemView.tv_view
    }

    fun setData(list: ArrayList<ManageModule>) {
        mData = list
    }

    interface OnItemSpinnerClickListener {
        fun onItemSpinnerClick(type: Int)
    }

    fun setItem(list: ArrayList<String>) {
        mListItemSelect = list
    }

}