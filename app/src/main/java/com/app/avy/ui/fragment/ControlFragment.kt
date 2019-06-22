package com.app.avy.ui.fragment

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RawRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.app.avy.BaseFragment
import com.app.avy.R
import com.app.avy.database.hotkey.HokeyViewModle
import com.app.avy.listenner.OnChildItemClickListener
import com.app.avy.module.ColorRequest
import com.app.avy.module.Coordinates
import com.app.avy.module.LightRequest
import com.app.avy.network.NetworkService
import com.app.avy.network.RetrofitHelper
import com.app.avy.ui.adapter.ControlAdapter
import com.app.avy.utils.Constant
import com.lib.collageview.CollageView
import com.lib.collageview.helpers.Flog
import com.lib.collageview.helpers.svg.SVGItem
import com.lib.collageview.helpers.svg.SVGPathUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_control.*
import org.xmlpull.v1.XmlPullParserException
import top.defaults.colorpicker.ColorPickerView
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class ControlFragment : BaseFragment(), View.OnClickListener {

    var TAG = ControlFragment::class.java.simpleName

    lateinit var mListener: OnChildItemClickListener
    lateinit var mHotkeyViewModel: HokeyViewModle
    lateinit var mAdapter: ControlAdapter
    lateinit var mNetworkService: NetworkService
    var mColorPicker: ColorPickerView? = null
    var preIndex = 0
    var preOpacity = 0
    lateinit var mCollageView: CollageView
    var mScreenInch: Double = 0.0
    lateinit var mSvg: SVGItem
    var mListCoordinates: ArrayList<Coordinates> = ArrayList()

    companion object {
        fun newInstance(listener: OnChildItemClickListener): ControlFragment {
            var home = ControlFragment()
            home.mListener = listener
            return home
        }
    }

    override fun getID() = R.layout.fragment_control
    override fun onViewReady() {
        init()
        pickColor()
        initRecyclerView()
        mHotkeyViewModel = ViewModelProviders.of(this).get(HokeyViewModle::class.java)
        mHotkeyViewModel.getAllWords().observe(this, Observer {
            mAdapter.setData(it)
            mAdapter.notifyDataSetChanged()
        })
        layout_setup.setOnClickListener(this)
        progress.progressChangedCallback = {
            tv_progress.text = (it * 100).toInt().toString() + "%"
            if ((it * 100).toInt() != preOpacity) {
                RetrofitHelper.getInstance().getNetworkService("http://3.3.0.161")
                    .setOpacity(LightRequest((it * 100).toInt().toString()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : io.reactivex.Observer<String> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(t: String) {
                        }

                        override fun onError(e: Throwable) {
                        }
                    })
            }
            preOpacity = (it * 100).toInt()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.layout_setup -> {
                mListener.let {
                    it.inChildItemClick(R.id.layout_setup)
                }
            }
        }
    }

    fun init() {
        mCollageView = collage_view
        mColorPicker = colorPicker

        val icon = BitmapFactory.decodeResource(
            context!!.resources,
            R.drawable.eva_3
        )

        Log.e(
            "ControlFragment",
            "1------>" + convertPixelsToDp(icon.width.toFloat()) + "---" + convertPixelsToDp(icon.height.toFloat())
        )
        Log.e(
            "ControlFragment",
            "1------>" + img_bg_kitchen.width + "---" + img_bg_kitchen.height
        )
        layout_kitchen.post {
            Log.e(
                "ControlFragment",
                "2------ ${layout_kitchen.width}  ${layout_kitchen.height}"
            )


        }

        /*mCollageView.setScreenSizes(
            convertPixelsToDp(icon.width.toFloat()).toInt(),
            convertPixelsToDp(icon.height.toFloat()).toInt()
        )*/

        mSvg = parsePathFromXml("ic_svg_lady_01.xml")!!
        mListCoordinates = getMinCoordinates(getFirstBox(mSvg))
        mCollageView.setScreenSizes(icon.width, icon.height)


        mCollageView.post {
            var width = (layout_kitchen.width - mCollageView.width).toFloat() / 2
            var height = (layout_kitchen.height - mCollageView.height) / 2

            var x1 = mListCoordinates[0].x
            var x2 = icon.width.toFloat() - mListCoordinates[mListCoordinates.size - 1].x

            var y1 = convertPixelsToDp(icon.height.toFloat()) - mListCoordinates[mListCoordinates.size - 1].y

           /* if (x2 > x1) {
                mCollageView.translationX = x2 - x1

            } else {
                mCollageView.translationX = x1 - x2
            }*/

            Log.e("ControlFragment", "3------>" + "---" + mCollageView.matrix)

            Log.e(TAG, "------> ${mCollageView.width}  ${img_bg_kitchen.width}")

            Log.e("ControlFragment", "3------>" + x1 + "---" + width + "--" + (width + x1) / width)

            var scalex = (width + x1) / width
        }

        mCollageView.setLayoutStyle(mSvg)
        mCollageView.show()

        if (9 < mScreenInch && mScreenInch < 11) {
            img_bg_kitchen.setImageBitmap(icon)
        } else if (11 < mScreenInch && mScreenInch < 16) {

        } else {
            img_bg_kitchen.setImageBitmap(icon)
        }
    }

    fun convertPixelsToDp(px: Float): Float {
        return px / (context!!.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertDpToPixel(dp: Float): Float {
        return dp * (context!!.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun pickColor() {
        mColorPicker?.subscribe { color, _, _ ->
            colorHex(color)
        }
    }

    fun initRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(context, 5)
        mAdapter = ControlAdapter()
        recyclerView.adapter = mAdapter
    }

    private fun colorHex(color: Int) {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)

        if (b != preIndex) {
            RetrofitHelper.getInstance().getNetworkService("http://3.3.0.161")
                .chanegColor(ColorRequest("change", r.toString(), g.toString(), b.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : io.reactivex.Observer<String> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: String) {
                    }

                    override fun onError(e: Throwable) {
                    }

                })
        }
        preIndex = b
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun parsePathFromXml(filePath: String): SVGItem? {
        val inputStream = context!!.assets.open(filePath)
        Flog.d("FILEPATH $filePath")
        var item = SVGPathUtils.getSVGItem(inputStream)
        inputStream.close()
        return item
    }

    fun getFirstBox(svg: SVGItem): ArrayList<Coordinates> {
        var mCoordinates: ArrayList<Coordinates> = ArrayList<Coordinates>()
        for (i in svg.pathData.indices) {
            var x = svg.pathData[i].substring(svg.pathData[i].indexOf("M") + 1, svg.pathData[i].indexOf(","))
            var y = svg.pathData[i].substring(svg.pathData[i].indexOf(",") + 1, svg.pathData[i].indexOf("l"))
            Log.e(TAG, "getFirstBox $x  $y")
            mCoordinates.add(Coordinates(x.toFloat(), y.toFloat()))
        }

        return mCoordinates
    }

    fun getMinCoordinates(list: ArrayList<Coordinates>): ArrayList<Coordinates> {
        list.sort()
        for (item in list) {
            Log.e(TAG, "getMinCoordinates  ${item.x}   ${item.y}")
        }
        return list
    }


}