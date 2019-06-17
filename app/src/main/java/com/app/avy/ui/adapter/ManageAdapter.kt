package com.app.avy.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.database.word.WordViewModel
import com.app.avy.listenner.OnItemClickListener
import com.app.avy.module.ManageModule
import com.app.avy.ui.view.spinner.MaterialBetterSpinner
import kotlinx.android.synthetic.main.item_cabinet.view.*
import java.util.*
import kotlin.collections.ArrayList

class ManageAdapter(var listener: OnItemClickListener, var listener1: OnItemSpinnerClickListener) :
    RecyclerView.Adapter<ManageAdapter.ViewHolder>() {
    lateinit var spinnerAdapter: ManageSpinnerAdapter
    var mData: List<ManageModule> = ArrayList<ManageModule>()
    lateinit var mWordViewModel: WordViewModel
    var startClickTime: Long = 0
    private val MAX_CLICK_DURATION = 200


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

        /* holder.spinner_key.setOnClickListener {

             *//* holder.spinner_key.requestFocus()
             holder.spinner_key.showDropDown()*//*
        }*/

        holder.spinner_key.setOnTouchListener { v, event ->
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    startClickTime = Calendar.getInstance().timeInMillis
                }
                MotionEvent.ACTION_UP -> {
                    var clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                    if (clickDuration < MAX_CLICK_DURATION) {
                        if (holder.spinner_key.isPopup) {
                            //  holder.spinner_key.dismissDropDown()
                            //  holder.spinner_key.isPopup = false
                        } else {
                            //  holder.spinner_key.requestFocus()
                            //  holder.spinner_key.showDropDown()
                            //  holder.spinner_key.isPopup = true

                        }
                    }

                    listener1.onItemSpinnerClick(mData[position].count, holder.spinner_key)
                }
            }
            true
        }


        holder.spinner_key.setOnLongClickListener {
            listener.onItemClick(mData[position].count)
            return@setOnLongClickListener true
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_count = itemView.tv_count
        var spinner_key = itemView.spinner_key
    }

    fun setData(list: ArrayList<ManageModule>) {
        mData = list
    }

    interface OnItemSpinnerClickListener {
        fun onItemSpinnerClick(type: Int, spinner: MaterialBetterSpinner)
    }
}