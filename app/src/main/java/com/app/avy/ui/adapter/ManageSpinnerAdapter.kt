package com.app.avy.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatImageView

class ManageSpinnerAdapter(context: Context, res: Int, list: ArrayList<String>?) :
    ArrayAdapter<String>(context, res, list) {

    var resource: Int
    var mData: ArrayList<String>? = null
    var vi: LayoutInflater

    init {
        this.resource = res
        this.mData = list
        this.vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var holder: ViewHolder
        var retView: View? = null
        if (convertView == null) {
            retView = vi.inflate(resource, parent, false)
            retView.tag = ViewHolder.createViewHolder(retView)
        }
        holder = ViewHolder()

        return retView!!
    }


    class ViewHolder {
        lateinit var img_chose: AppCompatImageView
        companion object {
            fun createViewHolder(view: View): ViewHolder {
                val holder = ViewHolder()
                return holder
            }
        }
    }
}