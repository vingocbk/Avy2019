package com.app.avy.ui.fragment.setting

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.app.avy.BaseFragment
import com.app.avy.MainActivity
import com.app.avy.R
import com.app.avy.utils.SharedPreferencesManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_advance_setting.*

class AdvancedSettingFragment : BaseFragment(), TextWatcher {
    lateinit var mSensity: AppCompatEditText
    lateinit var mTvUpdate: TextView

    override fun getID() = R.layout.fragment_advance_setting

    override fun onViewReady() {
        mSensity = edt_sensity
        mTvUpdate = tv_update

        mTvUpdate.setOnClickListener {
            Log.e("AdvancedSettingFragment","----->" + mSensity.text!!.trim().toString())
            if (mSensity.text!!.trim().isNotEmpty()) {
                SharedPreferencesManager.getInstance(context!!)
                    .storeStringInSharePreferen(SharedPreferencesManager.SENCITY, mSensity.text!!.trim().toString())
                startActivity(Intent(context, MainActivity::class.java))
            } else {
                Toasty.info(context!!, "Bạn chưa nhập thông số sencity.", Toast.LENGTH_SHORT, true).show()

            }
        }
        mSensity.addTextChangedListener(this)


        val count = SharedPreferencesManager.getInstance(context!!)
            .getIntFromSharePreferen(SharedPreferencesManager.CABINET_NUMBER_DEFAULT)
        val categories = ArrayList<Int>()
        for (i in 1..count!!) {
            categories.add(i)
        }

        var adapter = ArrayAdapter<Int>(
            context,
            android.R.layout.simple_dropdown_item_1line, categories
        )

        spinner_slow.setAdapter(adapter)
        spinner_speed_1.setAdapter(adapter)
        spinner_speed.setAdapter(adapter)
        spinner_return.setAdapter(adapter)
        spinner_distant.setAdapter(adapter)

    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }
}