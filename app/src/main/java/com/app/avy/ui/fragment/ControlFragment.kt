package com.app.avy.ui.fragment

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
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
import com.app.avy.module.LightRequest
import com.app.avy.network.NetworkService
import com.app.avy.network.RetrofitHelper
import com.app.avy.ui.adapter.ControlAdapter
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

class ControlFragment : BaseFragment(), View.OnClickListener {

    lateinit var mListener: OnChildItemClickListener
    lateinit var mHotkeyViewModel: HokeyViewModle
    lateinit var mAdapter: ControlAdapter
    lateinit var mNetworkService: NetworkService
    var mColorPicker: ColorPickerView? = null
    var preIndex = 0
    var preOpacity = 0
    lateinit var mCollageView: CollageView

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
        val d = ContextCompat.getDrawable(context!!, R.drawable.ic_13)
        val h = d!!.intrinsicHeight
        val w = d.intrinsicWidth

        val icon = BitmapFactory.decodeResource(
            context!!.resources,
            R.drawable.eva_3
        )

        Log.e("ControlFragment", "------>" + icon.width + "---" + icon.height)

        //mCollageView.setBackgroundResource(R.drawable.ic_13)


        // mCollageView.setWidthAndHeigh(convertPixelsToDp(2332.0f).toInt(), convertPixelsToDp(1379.0f).toInt())
        img_bg_kitchen.post {
            Log.e(
                "ControlFragment",
                "------ ${convertPixelsToDp(img_bg_kitchen.width.toFloat())}  ${convertPixelsToDp(img_bg_kitchen.height.toFloat())}"
            )
        }
        mCollageView.setLayoutStyle(parsePathFromXml("ic_svg_lady_01.xml"))
        mCollageView.show()


    }

    fun convertPixelsToDp(px: Float): Float {
        return px / (context!!.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
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
        val a = Color.alpha(color)
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

    fun loadSvg() {
        parsePathFromXml("ic_square_1.xml")
    }

    @Throws(IOException::class, XmlPullParserException::class)
    fun parsePathFromXml(filePath: String): SVGItem? {
        val inputStream = context!!.assets.open(filePath)
        Flog.d("FILEPATH $filePath")
        var item = SVGPathUtils.getSVGItem(inputStream)
        inputStream.close()
        return item
    }
}