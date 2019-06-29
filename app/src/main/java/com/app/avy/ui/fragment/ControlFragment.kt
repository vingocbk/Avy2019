package com.app.avy.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.BaseFragment
import com.app.avy.MyApplication
import com.app.avy.R
import com.app.avy.database.hotkey.HokeyViewModle
import com.app.avy.listenner.OnChildItemClickListener
import com.app.avy.listenner.OnItemHotkeyClickListener
import com.app.avy.module.Coordinates
import com.app.avy.module.LightModule
import com.app.avy.module.LightRequest
import com.app.avy.network.HttpGetRequest
import com.app.avy.network.MyObserver
import com.app.avy.network.NetworkService
import com.app.avy.ui.adapter.ControlAdapter
import com.app.avy.utils.Constant
import com.app.avy.utils.SharedPreferencesManager
import com.lib.collageview.CollageView
import com.lib.collageview.helpers.Flog
import com.lib.collageview.helpers.Utils
import com.lib.collageview.helpers.svg.SVGItem
import com.lib.collageview.helpers.svg.SVGPathUtils
import com.lib.collageview.interfaces.CollageViewListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_control.*
import kotlinx.android.synthetic.main.fragment_control.recyclerView
import org.xmlpull.v1.XmlPullParserException
import top.defaults.colorpicker.ColorPickerView
import java.io.IOException
import kotlin.collections.ArrayList

class ControlFragment : BaseFragment(), View.OnClickListener, OnItemHotkeyClickListener, CollageViewListener {


    var TAG = ControlFragment::class.java.simpleName
    private val INITIAL_COLOR = -0x8000


    lateinit var mListener: OnChildItemClickListener
    lateinit var mHotkeyViewModel: HokeyViewModle
    lateinit var mAdapter: ControlAdapter
    lateinit var mPref: SharedPreferencesManager
    var mColorPicker: ColorPickerView? = null
    var preIndex = 0
    var preOpacity = 0
    var mOpacity = 0
    var mColor: Int = INITIAL_COLOR
    lateinit var mCollageView: CollageView
    var mScreenInch: Double = 0.0
    lateinit var mSvg: SVGItem
    var mListCoordinates: ArrayList<Coordinates> = ArrayList()
    var mHeadIP: String? = null
    var mLastIP: String? = null
    var isStatus: Boolean = false


    companion object {
        fun newInstance(listener: OnChildItemClickListener): ControlFragment {
            val home = ControlFragment()
            home.mListener = listener
            return home
        }
    }

    override fun getID() = R.layout.fragment_control
    override fun onViewReady() {
        mPref = SharedPreferencesManager.getInstance(activity!!)
        init()
        pickColor()
        initRecyclerView()
        mHotkeyViewModel = ViewModelProviders.of(this).get(HokeyViewModle::class.java)
        mHotkeyViewModel.getAllWords().observe(this, Observer {
            mAdapter.setData(it)
            mAdapter.notifyDataSetChanged()
        })
        mColorPicker!!.setInitialColor(mColor)
        layout_setup.setOnClickListener(this)
        tv_turn_light.setOnClickListener(this)
        progress.progressChangedCallback = {
            mOpacity = (it * 100).toInt()
            tv_progress.text = (it * 100).toInt().toString() + "%"
            if ((it * 100).toInt() != preOpacity) {
                val request = LightRequest("${(it * 100).toInt()}")
                val data = "{\"intensityLight\" : \"${(it * 100).toInt()}\"}"
                HttpGetRequest().execute(
                    "${Constant.HTTP}$mHeadIP".plus(mLastIP!!.toInt() + 11) + "/control_intensity",
                    data
                )
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
            R.id.tv_turn_light -> {
                val request: LightModule
                if (isStatus) {
                    tv_turn_light.text = "Turn Off"
                    mPref.storeBooleanInSharePreferen(SharedPreferencesManager.TURN_LIGHT, false)
                    request = LightModule("off", "255", "255", "255")
                } else {
                    tv_turn_light.text = "Turn On"
                    mPref.storeBooleanInSharePreferen(SharedPreferencesManager.TURN_LIGHT, true)
                    request = LightModule("on", "255", "255", "255")
                }

                (activity!!.application as MyApplication).retrofitHelper()
                    .getNetworkService("${Constant.HTTP}$mHeadIP".plus(mLastIP!!.toInt() + 11))
                    .changeLight(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(MyObserver(activity!!))

                isStatus = !isStatus
                mPref.storeBooleanInSharePreferen(SharedPreferencesManager.TURN_LIGHT, isStatus)
            }
        }
    }


    override fun onItemHotketClick(isStatus: Boolean, view: String) {
        val item = view.split("\\-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val list = item.toCollection(ArrayList())

        if (isStatus) {
            Observable.merge(
                Constant.createOpenObservable(
                    activity!!.application as MyApplication,
                    list,
                    mHeadIP!!,
                    mLastIP!!
                )
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(MyObserver(activity!!))
        } else {
            Observable.merge(
                Constant.createCloseObservable(
                    activity!!.application as MyApplication,
                    list,
                    mHeadIP!!,
                    mLastIP!!
                )
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(MyObserver(activity!!))
        }
    }

    override fun showedOnScreen() {
    }

    override fun onFocusedView(focusType: Int) {
        val curIdx = mCollageView.photoViewList.currentIndex

        Log.e(TAG, "onFocusedView $curIdx")

        Log.e(TAG, "photoViewList---> ${mCollageView.photoViewList[curIdx].focus}")

        mCollageView.setRectF(curIdx, mCollageView.photoViewList[curIdx].rectF)

        if (!mHeadIP.isNullOrEmpty() && !mLastIP.isNullOrEmpty()) {

            if (mCollageView.photoViewList[curIdx].focus) {
                (activity!!.application as MyApplication).retrofitHelper()
                    .getNetworkService("${Constant.HTTP}$mHeadIP".plus(mLastIP!!.toInt() + Utils.mapIndex(curIdx)))
                    .openWindow()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(MyObserver(activity!!))
            } else {
                (activity!!.application as MyApplication).retrofitHelper()
                    .getNetworkService("${Constant.HTTP}$mHeadIP".plus(mLastIP!!.toInt() + Utils.mapIndex(curIdx)))
                    .closeWindow()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(MyObserver(activity!!))
            }
        }
    }

    override fun onPhotosLoadDone() {

    }

    override fun onPhotoviewActionUp(idx: Int) {
        Log.e(TAG, "onPhotoviewActionUp $idx")

    }

    override fun onDestroyView() {
        Log.e(TAG, "--------onDestroyView ${mColorPicker!!.color}")
        mPref.storeIntInSharePreferen(SharedPreferencesManager.OPACITY_COLOR, mOpacity)
        mPref.storeIntInSharePreferen(SharedPreferencesManager.COLOR_HEX, mColorPicker!!.color)
        super.onDestroyView()
    }


    @SuppressLint("SetTextI18n")
    fun init() {
        mHeadIP = mPref.getStringFromSharePreferen(SharedPreferencesManager.HEADER_IP)
        mLastIP = mPref.getStringFromSharePreferen(SharedPreferencesManager.LASST_IP)
        isStatus = mPref.getBooleanInSharePreferen(SharedPreferencesManager.TURN_LIGHT)
        mOpacity = mPref.getIntFromSharePreferen(SharedPreferencesManager.OPACITY_COLOR)!!
        mColor = mPref.getIntFromSharePreferen(SharedPreferencesManager.COLOR_HEX)!!
        mCollageView = collage_view
        mColorPicker = colorPicker

        if (mColor == 0) {
            mColor = INITIAL_COLOR
        }

        Log.e(TAG, "--------init $mColor")

        progress.setProgress(mOpacity.toFloat() / 100)
        tv_progress.text = " $mOpacity %"


        if (isStatus) {
            tv_turn_light.text = "Turn On"
        } else {
            tv_turn_light.text = "Turn Off"
        }
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

        mSvg = parsePathFromXml("ic_svg_lady_01.xml")!!
        mListCoordinates = getMinCoordinates(getFirstBox(mSvg))
        mCollageView.setScreenSizes(icon.width, icon.height)


        mCollageView.post {
            var width = (layout_kitchen.width - mCollageView.width).toFloat() / 2
            var height = (layout_kitchen.height - mCollageView.height) / 2

            var x1 = mListCoordinates[0].x

            var x2 = icon.width.toFloat() - mListCoordinates[mListCoordinates.size - 1].x

            var y1 = convertPixelsToDp(icon.height.toFloat()) - mListCoordinates[mListCoordinates.size - 1].y

            Log.e("ControlFragment", "3------>" + "---" + mCollageView.matrix)

            Log.e(TAG, "------> ${mCollageView.width}  ${img_bg_kitchen.width}")

            Log.e("ControlFragment", "3------>" + x1 + "---" + width + "--" + (width + x1) / width)

            var scalex = (width + x1) / width
        }
        mCollageView.setLayoutStyle(mSvg)

        mCollageView.setCollageViewListener(this)

        mCollageView.show()

        if (9 < mScreenInch && mScreenInch < 11) {
        } else if (11 < mScreenInch && mScreenInch < 16) {

        } else {
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
        recyclerView.layoutManager = GridLayoutManager(context, 5) as RecyclerView.LayoutManager?
        mAdapter = ControlAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun colorHex(color: Int) {
        Log.e("colorHex", "----->$color")
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)

        if (b != preIndex) {

            val request = LightModule("change", r.toString(), g.toString(), b.toString())

            val data =
                ("{\"power\":\"change\",\"red\":\"" + r.toString() + "\",\"green\":\"" + g.toString()
                        + "\",\"blue\":\"" + b.toString() + "\"}")


            if (!mHeadIP.isNullOrEmpty() && !mLastIP.isNullOrEmpty()) {
                HttpGetRequest().execute(
                    "${Constant.HTTP}$mHeadIP".plus(mLastIP!!.toInt() + 11) + "/control_led_color",
                    data
                )
            }
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

    private fun handleFocused() {

    }


}