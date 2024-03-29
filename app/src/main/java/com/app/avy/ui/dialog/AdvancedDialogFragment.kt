package com.app.avy.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.app.avy.R
import com.app.avy.listenner.OnItemClickListener
import com.app.avy.utils.SharedPreferencesManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_dialog_advance.view.*
import android.widget.EditText


class AdvancedDialogFragment(var listener: OnItemClickListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_dialog_advance, container, false)
        val tvValidatePass = v.tv_validate_pass


        val tvPass = v.edt_pass

        tvPass.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == IME_ACTION_DONE) {
                validatePass(tvPass)
            }
            true
        }

        tvValidatePass.setOnClickListener {
            validatePass(tvPass)
        }
        return v
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        dialog.window.setLayout(2 * width / 5, 2 * height / 5)
    }

    fun validatePass(edt_pass: EditText) {
        edt_pass.text.trim().isEmpty().let {
            if (edt_pass.text.toString() == SharedPreferencesManager.getInstance(context!!).getStringFromSharePreferen(
                    SharedPreferencesManager.ADVANCED_PASS
                )
            ) {
                dismiss()
                listener.onItemClick(R.id.tv_validate_pass)
            } else {
                Toasty.info(context!!, "Bạn nhập password chưa đúng. Vui lòng thử lại", Toast.LENGTH_SHORT, true)
                    .show()
            }
        }
    }
}