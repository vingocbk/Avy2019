package com.app.avy.ui.fragment.setting

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.app.avy.BaseFragment
import com.app.avy.MyApplication
import com.app.avy.R
import com.app.avy.module.ConfigDevice
import com.app.avy.module.SlowModule
import com.app.avy.module.SpeedModule
import com.app.avy.module.TimeModule
import com.app.avy.network.MyObserver
import com.app.avy.utils.Constant
import com.app.avy.utils.SharedPreferencesManager
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_advance_setting.*

class AdvancedSettingFragment : BaseFragment(), TextWatcher, View.OnClickListener, View.OnTouchListener {

    val TAG = AdvancedSettingFragment::class.java.simpleName
    lateinit var mSensity: AppCompatEditText
    lateinit var mTvSave: TextView
    lateinit var mTvSend: TextView
    lateinit var mTvSendSpeed: TextView
    lateinit var mTvSlow: TextView
    lateinit var mTvDistant: TextView
    lateinit var mTvReturn: TextView
    lateinit var mPref: SharedPreferencesManager
    lateinit var mMyApplication: MyApplication

    var mSpeedSlect: String? = null
    var mSpeedSlect1: String? = null
    var mSlowSlect: String? = null
    var mDistantSlect: String? = null
    var mTimeSlect: String? = null
    var mHeadeIP: String? = null
    var mLastIP: String? = null

    override fun getID() = R.layout.fragment_advance_setting


    override fun onViewReady() {
        mMyApplication = activity!!.application as MyApplication
        mPref = SharedPreferencesManager.getInstance(activity!!)
        mSensity = edt_sensity
        mTvSave = tv_update
        mTvSendSpeed = tv_save_model_speed
        mTvSlow = tv_send_percent_slow
        mTvDistant = tv_send_reset_distant
        mTvReturn = tv_send_time_return
        mTvSend = tv_send
        init()

        mSensity.addTextChangedListener(this)
        edt_local_ip.addTextChangedListener(this)
        val count = mPref.getIntFromSharePreferen(SharedPreferencesManager.CABINET_NUMBER_DEFAULT)
        val categories = ArrayList<Int>()
        for (i in 1..count!!) {
            categories.add(i)
        }

        val adapter = ArrayAdapter<Int>(
            activity!!,
            android.R.layout.simple_dropdown_item_1line, categories
        )

        root_advan.setOnTouchListener(this)
        spinner_slow.setAdapter(adapter)
        spinner_speed_1.setAdapter(adapter)
        spinner_speed.setAdapter(adapter)
        spinner_return.setAdapter(adapter)
        spinner_distant.setAdapter(adapter)
        setSpinnerItemClick()
        //--
        mTvSave.setOnClickListener(this)
        mTvSend.setOnClickListener(this)
        mTvSendSpeed.setOnClickListener(this)
        mTvSlow.setOnClickListener(this)
        mTvDistant.setOnClickListener(this)
        mTvReturn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_update -> {
                if (edt_local_ip.text!!.trim().toString().isNotEmpty() && edt_sensity.text!!.trim().toString().isNotEmpty()) {
                    val localip = edt_local_ip.text!!.trim().toString()
                    mHeadeIP = localip.substring(0, localip.lastIndexOf(".") + 1)
                    mLastIP = localip.substring(localip.lastIndexOf(".") + 1, localip.length)
                    Log.e(TAG, "localip------> $mHeadeIP  $mLastIP")
                    mPref.storeStringInSharePreferen(SharedPreferencesManager.HEADER_IP, mHeadeIP!!)
                    mPref.storeStringInSharePreferen(SharedPreferencesManager.LASST_IP, mLastIP!!)
                    mPref.storeStringInSharePreferen(
                        SharedPreferencesManager.SENCITY, edt_sensity.text!!.trim().toString()
                    )

                    Toasty.success(activity!!, "Thay đổi thành công.", Toasty.LENGTH_SHORT).show()
                } else {
                    Toasty.info(activity!!, "Bạn chưa nhập đủ thông tin.", Toasty.LENGTH_SHORT).show()
                }

            }

            R.id.tv_save_model_speed -> {
                if (!mHeadeIP.isNullOrEmpty() && !mLastIP.isNullOrEmpty() && !mSpeedSlect.isNullOrEmpty() && !mSpeedSlect1.isNullOrEmpty()) {

                    mPref.storeStringInSharePreferen(SharedPreferencesManager.SPEED, mSpeedSlect!!)
                    mPref.storeStringInSharePreferen(SharedPreferencesManager.SPEED_1, mSpeedSlect1!!)

                    mMyApplication.retrofitHelper()
                        .getNetworkService("${Constant.HTTP}$mHeadeIP".plus(mLastIP!!.toInt() + mSpeedSlect!!.toInt()))
                        .chanegSpeed(SpeedModule(mSpeedSlect1!!))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(MyObserver(activity!!))
                } else {
                    Toasty.info(activity!!, "Bạn chưa nhập đủ thông tin.", Toasty.LENGTH_SHORT).show()
                }
            }
            R.id.tv_send_percent_slow -> {
                if (edt_in.text!!.trim().toString().isNotEmpty() && edt_out.text!!.trim().toString().isNotEmpty() && !mSlowSlect.isNullOrEmpty()) {
                    val request = SlowModule(edt_out.text!!.trim().toString(), edt_in.text!!.trim().toString())

                    mPref.storeStringInSharePreferen(SharedPreferencesManager.SLOW, mSlowSlect!!)
                    mPref.storeStringInSharePreferen(SharedPreferencesManager.SLOW_CONFIG, Gson().toJson(request))

                    mMyApplication.retrofitHelper()
                        .getNetworkService("${Constant.HTTP}$mHeadeIP".plus(mLastIP!!.toInt() + mSlowSlect!!.toInt()))
                        .changeSlow(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(MyObserver(activity!!))
                } else {
                    Toasty.info(activity!!, "Bạn chưa nhập đủ thông tin.", Toasty.LENGTH_SHORT).show()
                }
            }
            R.id.tv_send_reset_distant -> {
                if (!mDistantSlect.isNullOrEmpty()) {
                    mPref.storeStringInSharePreferen(SharedPreferencesManager.DISTANT, mDistantSlect!!)
                    mMyApplication.retrofitHelper()
                        .getNetworkService("${Constant.HTTP}$mHeadeIP".plus(mLastIP!!.toInt() + mDistantSlect!!.toInt()))
                        .changeDistant()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(MyObserver(activity!!))
                } else {
                    Toasty.info(activity!!, "Bạn chưa nhập đủ thông tin.", Toasty.LENGTH_SHORT).show()
                }
            }
            R.id.tv_send_time_return -> {
                if (edt_time.text!!.trim().toString().isNotEmpty() && !mTimeSlect.isNullOrEmpty()) {

                    val request = TimeModule(edt_time.text!!.trim().toString())

                    mPref.storeStringInSharePreferen(SharedPreferencesManager.TIME_SELECT, mTimeSlect!!)
                    mPref.storeStringInSharePreferen(SharedPreferencesManager.TIME_RETURN, Gson().toJson(request))

                    mMyApplication.retrofitHelper()
                        .getNetworkService("${Constant.HTTP}$mHeadeIP".plus(mLastIP!!.toInt() + mTimeSlect!!.toInt()))
                        .changeTime(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(MyObserver(activity!!))
                } else {
                    Toasty.info(activity!!, "Bạn chưa nhập đủ thông tin.", Toasty.LENGTH_SHORT).show()
                }
            }
            R.id.tv_send -> {
                if (edt_ssid.text!!.trim().toString().isNotEmpty() && edt_pass.text!!.trim().toString().isNotEmpty()
                    && edt_device_ip.text!!.trim().toString().isNotEmpty() && edt_ip_wifi.text!!.trim().toString().isNotEmpty()
                ) {
                    // do something
                    val request = ConfigDevice(
                        edt_ssid.text!!.trim().toString(),
                        edt_pass.text!!.trim().toString(),
                        edt_device_ip.text!!.trim().toString(),
                        edt_ip_wifi.text!!.trim().toString()
                    )

                    Log.e(TAG, "request------ ${Gson().toJson(request)}")
                    mPref.storeStringInSharePreferen(SharedPreferencesManager.CONFIG_DEVICE, Gson().toJson(request))

                    mMyApplication.retrofitHelper()
                        .getNetworkService(Constant.HTTP_CONFIG)
                        .configDevice(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(MyObserver(activity!!))

                } else {
                    Toasty.info(activity!!, "Bạn chưa nhập đủ thông tin.", Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        Constant.hideKeyboard(activity!!)
        return true
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    @SuppressLint("SetTextI18n")
    fun init() {
        mHeadeIP = mPref.getStringFromSharePreferen(SharedPreferencesManager.HEADER_IP)
        mLastIP = mPref.getStringFromSharePreferen(SharedPreferencesManager.LASST_IP)
        if (!mHeadeIP.isNullOrEmpty() && !mLastIP.isNullOrEmpty()) {
            edt_local_ip.setText("$mHeadeIP$mLastIP")
        }
        if (!mPref.getStringFromSharePreferen(SharedPreferencesManager.SPEED).isNullOrEmpty()) {
            mSpeedSlect = mPref.getStringFromSharePreferen(SharedPreferencesManager.SPEED)
            spinner_speed.setText(mSpeedSlect)
        }

        if (!mPref.getStringFromSharePreferen(SharedPreferencesManager.SPEED_1).isNullOrEmpty()) {
            mSpeedSlect1 = mPref.getStringFromSharePreferen(SharedPreferencesManager.SPEED_1)
            spinner_speed_1.setText(mSpeedSlect1)
        }
        Log.e(TAG, "request1 ------ ${mPref.getStringFromSharePreferen(SharedPreferencesManager.CONFIG_DEVICE)}")
        if (!mPref.getStringFromSharePreferen(SharedPreferencesManager.CONFIG_DEVICE).isNullOrEmpty()) {
            val configDevice = Gson().fromJson<ConfigDevice>(
                mPref.getStringFromSharePreferen(SharedPreferencesManager.CONFIG_DEVICE),
                ConfigDevice::class.java
            )
            /*edt_ssid.setText(configDevice.ssid)
            edt_pass.setText(configDevice.password)
            edt_device_ip.setText(configDevice.set_ip)
            edt_ip_wifi.setText(configDevice.ip_send)*/
        }

        if (!mPref.getStringFromSharePreferen(SharedPreferencesManager.SENCITY).isNullOrEmpty()) {
            edt_sensity.setText(mPref.getStringFromSharePreferen(SharedPreferencesManager.SENCITY))
        }

        if (!mPref.getStringFromSharePreferen(SharedPreferencesManager.SLOW).isNullOrEmpty()) {
            spinner_slow.setText(mPref.getStringFromSharePreferen(SharedPreferencesManager.SLOW))
        }

        if (!mPref.getStringFromSharePreferen(SharedPreferencesManager.SLOW_CONFIG).isNullOrEmpty()) {
            val slowModule = Gson().fromJson<SlowModule>(
                mPref.getStringFromSharePreferen(SharedPreferencesManager.SLOW_CONFIG),
                SlowModule::class.java
            )

            slowModule?.setpercentin?.let {
                edt_in.setText(slowModule.setpercentin)
                edt_out.setText(slowModule.setpercentout)
            }


        }

        if (!mPref.getStringFromSharePreferen(SharedPreferencesManager.DISTANT).isNullOrEmpty()) {
            spinner_distant.setText(mPref.getStringFromSharePreferen(SharedPreferencesManager.DISTANT))
        }

        if (!mPref.getStringFromSharePreferen(SharedPreferencesManager.TIME_SELECT).isNullOrEmpty()) {
            spinner_return.setText(mPref.getStringFromSharePreferen(SharedPreferencesManager.TIME_SELECT))
        }


        if (!mPref.getStringFromSharePreferen(SharedPreferencesManager.TIME_RETURN).isNullOrEmpty()) {
            val time = Gson().fromJson<TimeModule>(
                mPref.getStringFromSharePreferen(SharedPreferencesManager.TIME_RETURN),
                TimeModule::class.java
            )
            time?.timereturn?.let {
                edt_time.setText(it)
            }

        }


    }

    fun setSpinnerItemClick() {
        spinner_slow.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            mSlowSlect = parent.getItemAtPosition(position).toString()
        }

        spinner_speed_1.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            mSpeedSlect1 = parent.getItemAtPosition(position).toString()
        }

        spinner_speed.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            mSpeedSlect = parent.getItemAtPosition(position).toString()
        }

        spinner_distant.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            mDistantSlect = parent.getItemAtPosition(position).toString()
        }

        spinner_return.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            mTimeSlect = parent.getItemAtPosition(position).toString()
        }

    }

}