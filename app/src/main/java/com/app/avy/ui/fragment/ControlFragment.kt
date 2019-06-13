package com.app.avy.ui.fragment

import android.graphics.Color
import android.view.View
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_control.*
import top.defaults.colorpicker.ColorPickerView

class ControlFragment : BaseFragment(), View.OnClickListener {

    lateinit var mListener: OnChildItemClickListener
    lateinit var mHotkeyViewModel: HokeyViewModle
    lateinit var mAdapter: ControlAdapter
    lateinit var mNetworkService: NetworkService
    var mColorPicker: ColorPickerView? = null
    var preIndex = 0
    var preOpacity = 0

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
        mColorPicker = colorPicker
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
}