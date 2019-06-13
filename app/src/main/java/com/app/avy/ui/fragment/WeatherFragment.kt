package com.app.avy.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import com.app.avy.BaseFragment
import com.app.avy.R
import com.app.avy.module.CurrentWeather
import com.app.avy.module.WeatherData
import com.app.avy.network.NetworkService
import com.app.avy.network.RetrofitHelper
import com.app.avy.utils.Constant
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.android.synthetic.main.fragment_weather.view.*
import java.lang.Exception
import java.util.*


class WeatherFragment : BaseFragment(), LocationListener {

    val TAG = WeatherFragment::class.java.simpleName
    lateinit var mNetworkService: NetworkService
    lateinit var mLocationManager: LocationManager

    private var mPermissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var mHasGps = false
    private var mHasNetWork = false
    private var mLocationGPS: Location? = null
    private var mLocationNetWork: Location? = null
    private lateinit var mAddress: Address

    lateinit var tv_temperature: AppCompatTextView
    lateinit var img_weather_status: AppCompatImageView
    lateinit var layout_weather: RelativeLayout
    lateinit var tv_weathre_status: AppCompatTextView
    lateinit var tv_humidity_temperature: AppCompatTextView
    lateinit var tv_location: AppCompatTextView

    private val PERMISSION_REQUEST = 10
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 0F
    private val MIN_TIME_BW_UPDATES: Long = 1000 * 5 * 1
    private val mLat = 21.03
    private val mLon = 105.85

    override fun getID() = R.layout.fragment_weather

    override fun onViewReady() {
        tv_temperature = view!!.tv_temperature
        img_weather_status = view!!.img_weather_status
        layout_weather = view!!.layout_weather
        tv_weathre_status = view!!.tv_weathre_status
        tv_humidity_temperature = view!!.tv_humidity_temperature
        tv_location = view!!.tv_location
        mNetworkService = RetrofitHelper.getInstance().getNetworkService(Constant.BASE_URL_WEATHER)
        initCheckPermission()
        tv_date.text = Constant.getDate()
    }

    private fun initCheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(mPermissions)) {
                getLocation()
            } else {
                requestPermissions(mPermissions, PERMISSION_REQUEST)
            }
        }
    }

    private fun checkPermission(mPermissions: Array<String>): Boolean {
        var allSuccess = true
        for (i in mPermissions.indices) {
            if (checkCallingOrSelfPermission(context!!, mPermissions[i]) == PackageManager.PERMISSION_DENIED) {
                allSuccess = false
            }
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain =
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                            permissions[i]
                        )
                    if (requestAgain) {
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            context, "Go to settings and enable the permission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                if (allSuccess)
                    getLocation()
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
        try {
            if (mHasGps) {
                Log.d("GET_GPS_LOCATION", "hasGPS")
                if (location != null) {
                    mLocationGPS = location

                    val geocoder = Geocoder(context, Locale.getDefault())
                    val listAddress: MutableList<Address> = geocoder.getFromLocation(
                        mLocationGPS!!.latitude,
                        mLocationGPS!!.longitude, 1
                    )
                    mAddress = listAddress[0]
                    val currentAddress = mAddress.getAddressLine(0)
                    tv_location.text = currentAddress.split(",")[1]
                    if (WeatherData.getCurrentWeather() != null) {
                        updateView(WeatherData.getCurrentWeather()!!)
                    } else {
                        getDataWeather(mLocationGPS!!.latitude, mLocationGPS!!.longitude)
                    }
                    mLocationManager.removeUpdates(this)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {
    }

    fun getDataWeather(lat: Double, lon: Double) {
        mNetworkService.getCurrentWeather(lat, lon, "metric", Constant.API_KEY_WEATHER)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CurrentWeather> {
                override fun onComplete() {
                    // not handle
                    Log.e(TAG, "onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    // not handle
                }

                override fun onNext(t: CurrentWeather) {
                    // handle data
                    updateView(t)
                    WeatherData.setCurrentWeather(t)

                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError" + e)
                }
            })
    }

    fun updateView(currentWeather: CurrentWeather) {
        tv_humidity_temperature.text = currentWeather.main.humidity.toString() + "%"
        tv_temperature.text = currentWeather.main.temp.toString()
        when (currentWeather.weather[0].description) {
            "clear sky" -> {
                layout_weather.background = ContextCompat.getDrawable(context!!, R.drawable.ic_clear_sky)
                img_weather_status.background = ContextCompat.getDrawable(context!!, R.drawable.ic_sunny)
                tv_weathre_status.text = getString(R.string.clear_sky)
            }
            "few clouds" -> {
                layout_weather.background = ContextCompat.getDrawable(context!!, R.drawable.weather_few_clouds)
                img_weather_status.background = ContextCompat.getDrawable(context!!, R.drawable.ic_sunny_cloudy)
                tv_weathre_status.text = getString(R.string.few_clouds)
            }
            "scattered clouds" -> {
                layout_weather.background = ContextCompat.getDrawable(context!!, R.drawable.weather_few_clouds)
                img_weather_status.background = ContextCompat.getDrawable(context!!, R.drawable.ic_much_cloudy)
                tv_weathre_status.text = getString(R.string.scattered_clouds)
            }
            "broken clouds" -> {
                layout_weather.background = ContextCompat.getDrawable(context!!, R.drawable.weather_few_clouds)
                img_weather_status.background = ContextCompat.getDrawable(context!!, R.drawable.ic_much_cloudy)
                tv_weathre_status.text = getString(R.string.scattered_clouds)
            }
            "shower rain" -> {
                layout_weather.background = ContextCompat.getDrawable(context!!, R.drawable.weather_rain)
                img_weather_status.background = ContextCompat.getDrawable(context!!, R.drawable.ic_rain)
                tv_weathre_status.text = getString(R.string.shower_rain)
            }
            "rain" -> {
                layout_weather.background = ContextCompat.getDrawable(context!!, R.drawable.weather_rain)
                img_weather_status.background = ContextCompat.getDrawable(context!!, R.drawable.ic_rain)
                tv_weathre_status.text = getString(R.string.shower_rain)
            }
            "thunderstorm" -> {
                layout_weather.background = ContextCompat.getDrawable(context!!, R.drawable.ic_bg_thunderstorn)
                img_weather_status.background = ContextCompat.getDrawable(context!!, R.drawable.ic_thunderstorm)
                tv_weathre_status.text = getString(R.string.thunderstorm)
            }
            "snow" -> {
                layout_weather.background = ContextCompat.getDrawable(context!!, R.drawable.weather_snow)
                img_weather_status.background = ContextCompat.getDrawable(context!!, R.drawable.ic_snow)
                tv_weathre_status.text = getString(R.string.snow)
            }
            "mist" -> {
                layout_weather.background = ContextCompat.getDrawable(context!!, R.drawable.weather_mist)
                tv_weathre_status.text = getString(R.string.mist)
            }
            "drizzle" -> {
                layout_weather.background = ContextCompat.getDrawable(context!!, R.drawable.weather_rain)
                img_weather_status.background = ContextCompat.getDrawable(context!!, R.drawable.ic_rain)
                tv_weathre_status.text = getString(R.string.drizzle)
            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        mLocationManager = context!!.getSystemService(LOCATION_SERVICE) as LocationManager
        mHasGps = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        mHasNetWork = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (mHasGps) {
            if (mHasGps || mHasNetWork) {
                mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this
                )
                val localGpsLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null) {
                    mLocationGPS = localGpsLocation
                }
            }

            if (mHasNetWork) {
                mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    this
                )

                val localNetWorkLocation = mLocationManager.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER
                )
                if (localNetWorkLocation != null) {
                    mLocationNetWork = localNetWorkLocation
                }
            }
        } else {
            getDataWeather(mLat, mLon)
        }
    }


}