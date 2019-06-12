package com.app.avy.ui.fragment.setting

import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.app.avy.BaseFragment
import com.app.avy.R
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_profile.*
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.content.Context.INPUT_METHOD_SERVICE
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NavUtils
import androidx.core.content.ContextCompat.getSystemService
import com.app.avy.module.UserInfo
import com.app.avy.ui.view.editext.MaskedWatcher
import com.app.avy.ui.view.editext.MaskedFormatter
import com.app.avy.utils.Constant
import com.app.avy.utils.SharedPreferencesManager
import com.google.gson.Gson


class ProfileFragment : BaseFragment(), View.OnTouchListener, TextWatcher {
    val TAG = ProfileFragment::class.java.simpleName

    private var formatter: MaskedFormatter? = null
    var userInfo: UserInfo? = null
    var temp: String? = null

    override fun getID() = R.layout.fragment_profile

    override fun onViewReady() {
        temp = SharedPreferencesManager.getInstance(context!!)
            .getStringFromSharePreferen(SharedPreferencesManager.USER_INFO_KEY)

        temp?.let {
            userInfo = Gson().fromJson(temp, UserInfo::class.java)
            userInfo?.let {
                edt_user_name.setText(it.fullName)
                edt_birthday.setText(it.birthday)
                edt_phone.setText(it.phone)
                edt_mail.setText(it.mail)
                edt_address.setText(it.address)
            }
        }

        root_profile.setOnTouchListener(this)
        //setMask("##/##/####")
        tv_update.setOnClickListener {
            if (edt_user_name.text.isEmpty()) {
                Toasty.info(context!!, "Bạn chưa nhập họ và tên.", Toast.LENGTH_SHORT, true).show()
                return@setOnClickListener
            }

            if (edt_birthday.text!!.isEmpty()) {
                Toasty.info(context!!, "Bạn chưa nhập ngày sinh.", Toast.LENGTH_SHORT, true).show()
                return@setOnClickListener
            }

            Log.e(TAG, "-----> " + edt_phone.text.toString())

            if (Constant.validCellPhone(edt_phone.text.toString()).isNotEmpty()) {
                Toasty.info(context!!, Constant.validCellPhone(edt_phone.text.toString()), Toast.LENGTH_SHORT, true)
                    .show()
                return@setOnClickListener
            }

            if (Constant.verifyMail(edt_mail.text.toString()).isNotEmpty()) {
                Toasty.info(context!!, Constant.verifyMail(edt_mail.text.toString()), Toast.LENGTH_SHORT, true).show()
                return@setOnClickListener
            }

            if (edt_address.text.isEmpty()) {
                Toasty.info(context!!, "Bạn chưa nhập địa chỉ.", Toast.LENGTH_SHORT, true).show()
                return@setOnClickListener
            }

            if (edt_user_name.text.isNotEmpty() && edt_phone.text.isNotEmpty()
                && Constant.validCellPhone(edt_phone.text.toString()).isEmpty()
                && Constant.verifyMail(edt_mail.text.toString()).isEmpty()
                && edt_birthday.text!!.isNotEmpty()
                && edt_address.text.isNotEmpty()
            ) {
                // update user
                var userInfo = UserInfo(
                    "${edt_user_name.text}",
                    "${edt_birthday.text}",
                    "${edt_phone.text}",
                    "${edt_mail.text}",
                    "${edt_address.text}"
                )
                SharedPreferencesManager.getInstance(context!!)
                    .storeStringInSharePreferen(SharedPreferencesManager.USER_INFO_KEY, Gson().toJson(userInfo))
                Toasty.success(context!!, "Bạn đã cập nhật thông tin thành công.", Toast.LENGTH_SHORT, true).show()
                tv_update.text = "Chỉnh Sửa"
            }
        }

        edt_phone.addTextChangedListener(this)
        edt_mail.addTextChangedListener(this)
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

    private fun setMask(mask: String) {
        formatter = MaskedFormatter(mask)
        edt_birthday.addTextChangedListener(MaskedWatcher(formatter, edt_birthday))
        val s = formatter!!.formatString(edt_birthday.getText().toString()).getUnMaskedString()
    }
}