package com.app.avy.ui.fragment

import com.app.avy.BaseFragment
import com.app.avy.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.avy.ui.view.autoscroll.InfinitePagerAdapter
import kotlinx.android.synthetic.main.fragment_header.*
import kotlinx.android.synthetic.main.item_slide_banner.view.*


class HeaderFragment : BaseFragment() {

    override fun getID() = R.layout.fragment_header

    lateinit var mAdapter: FragmentSlideImageNew

    var data: ArrayList<Int> = arrayListOf(R.drawable.banner_1, R.drawable.banner_2)

    override fun onViewReady() {

        if (viewpager_collection.adapter == null) {
            mAdapter = FragmentSlideImageNew(context!!, data)
            mAdapter.setDatas(data)
            viewpager_collection.adapter = mAdapter
            indicator.setupWithViewPager(viewpager_collection, data.size)
        } else {
            mAdapter = viewpager_collection.adapter as FragmentSlideImageNew
            mAdapter.setDatas(data)
            viewpager_collection.adapter = mAdapter
            indicator.setupWithViewPager(viewpager_collection, data.size)
        }

        if (data.size > 1) {
            viewpager_collection.setSlideDuration(10000)
            viewpager_collection.startAutoScroll()
        }

    }

    class FragmentSlideImageNew(val mContext: Context, listObject: ArrayList<Int>) : InfinitePagerAdapter() {

        override fun getItemView(position: Int, convertView: View?, container: ViewGroup?): View {
            val view = LayoutInflater.from(mContext).inflate(R.layout.item_slide_banner, container, false)
            val mSimpleDraweeView = view.img_slide_banner
            mSimpleDraweeView.background = null
            mSimpleDraweeView.setImageResource(mlistObject[position])
            return view
        }

        var mlistObject: ArrayList<Int> = ArrayList<Int>()

        init {
            mlistObject = listObject
        }

        override fun getItemCount(): Int {
            return mlistObject.size
        }

        fun setDatas(listObject: ArrayList<Int>) {
            mlistObject = listObject
            notifyDataSetChanged()
        }
    }
}