package com.app.avy.ui.adapter

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.avy.R
import com.app.avy.database.cabinet.Cabinet
import com.app.avy.listenner.OnItemSaveKeyListenner
import com.app.avy.listenner.OnItemSpinnerClickListenner
import com.app.avy.module.HotkeyModule
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.item_set_hotkey.view.*
import java.util.*
import kotlin.collections.ArrayList

class HotKeyAdapter(var listenner: OnItemSaveKeyListenner) : RecyclerView.Adapter<HotKeyAdapter.ViewHolder>(),
    OnItemSpinnerClickListenner {

    val TAG = HotKeyAdapter::class.java.simpleName

    var mListKey: ArrayList<HotkeyModule> = ArrayList<HotkeyModule>()
    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_set_hotkey, parent, false))
    }

    override fun getItemCount() = mListKey.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mContext = holder.itemView.context
        holder.tv_select.text = mContext.getString(R.string.txt_select)
        holder.checkbox.isChecked = mListKey[position].isSave
        holder.tv_view.text =
            if (mListKey[position].view.isEmpty() || mListKey[position].view == mContext.getString(R.string.txt_view)) {
                mContext.getString(R.string.txt_view)
            } else {
                if (mListKey[position].view.contains("Tủ")) {
                    mListKey[position].view
                } else {
                    "Tủ-${mListKey[position].view}"
                }
            }

        Log.e(HotKeyAdapter::class.java.simpleName, "------> " + mListKey[position].key)
        if (mListKey[position].key != "HotKey" && mListKey[position].key.isNotEmpty()) {
            holder.edit_key.setText(mListKey[position].key)
            holder.edit_key.background = ContextCompat.getDrawable(mContext, R.drawable.ic_border)
            holder.edit_key.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_white))
        } else {
            holder.edit_key.background = ContextCompat.getDrawable(mContext, R.drawable.bg_border)
            holder.edit_key.setHintTextColor(ContextCompat.getColor(mContext, R.color.md_grey))
            holder.edit_key.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_black))
        }
        val spinnerAdapter = CustomSpinnerAdapter(mContext, mListKey[position].listCabinet, this)
        holder.nice_spinner.setAdapter(spinnerAdapter)

        holder.checkbox.setOnClickListener {
            if (validateHotKey(mContext, mListKey[position].key, mListKey[position].view)) {
                holder.checkbox.isChecked = !mListKey[position].isSave
                mListKey[position].isSave = !mListKey[position].isSave
                listenner.onItemSaveKey(mListKey[position].isSave, mListKey)
            } else {
                holder.checkbox.isChecked = false
            }
        }

        holder.edit_key.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == 1) {
                    holder.edit_key.background = ContextCompat.getDrawable(mContext, R.drawable.ic_border)
                    holder.edit_key.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_white))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mListKey[position].key = s.toString()
                if (s!!.isEmpty()) {
                    holder.edit_key.background = ContextCompat.getDrawable(mContext, R.drawable.bg_border)
                    holder.edit_key.setHintTextColor(ContextCompat.getColor(mContext, R.color.md_grey))
                    holder.edit_key.setTextColor(ContextCompat.getColor(mContext, R.color.md_grey_black))
                }
            }
        })
    }

    override fun onItemSpinnerClick(cabinet: Cabinet) {
        updateData(cabinet)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val edit_key = itemView.edit_key
        val tv_select = itemView.tv_select
        val tv_view = itemView.tv_view
        val checkbox = itemView.checkbox
        val nice_spinner = itemView.nice_spinner
    }

    fun setData(listKey: ArrayList<HotkeyModule>) {
        mListKey = listKey
    }

    fun updateData(cabinet: Cabinet) {
        var hotkeyModule: HotkeyModule
        var temp: ArrayList<Cabinet>
        var str = ""
        var isSelect = false
        for (i in mListKey.indices) {
            hotkeyModule = mListKey[i]
            if (cabinet.type == mListKey[i].id) {
                temp = ArrayList<Cabinet>()
                for (j in mListKey[i].listCabinet.indices) {
                    isSelect = if (cabinet.id == mListKey[i].listCabinet[j].id) {
                        cabinet.select
                    } else {
                        mListKey[i].listCabinet[j].select
                    }
                    temp.add(
                        Cabinet(
                            mListKey[i].listCabinet[j].id,
                            hotkeyModule.id,
                            mListKey[i].listCabinet[j].cabinet,
                            isSelect
                        )
                    )
                }
                str = if (cabinet.select) {
                    if (mListKey[i].view == mContext.getString(R.string.txt_view)) {
                        cabinet.id
                    } else {
                        if (mListKey[i].view.isEmpty()) {
                            cabinet.id
                        } else {
                            mListKey[i].view + "-" + cabinet.id
                        }
                    }
                } else {
                    val result = delete(mListKey[i].view, cabinet.id)!!
                    if (result.isEmpty()) {
                        mContext.getString(R.string.txt_view)
                    } else {
                        result
                    }
                }
                mListKey[i] = HotkeyModule(hotkeyModule.id, hotkeyModule.key, str, hotkeyModule.isSave, temp)
            }
        }
        notifyItemChanged(cabinet.type.toInt() - 1)
    }

    private fun delete(result: String, item: String): String? {
        val a = result.split("\\-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val list = LinkedList(Arrays.asList<String>(*a))
        for (j in list.indices) {
            if (list[j] == item) {
                list.removeAt(j)
                return TextUtils.join("-", list)
            }
        }
        return null
    }

    fun validateHotKey(context: Context, key: String, views: String): Boolean {
        return if ((key == "HotKey" || key.isEmpty()) || views == "View") {
            Toasty.info(context, "Bạn chưa nhập đủ thông tin.", Toast.LENGTH_SHORT, true).show()
            false
        } else {
            true
        }
    }
}