package com.app.avy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.app.avy.listenner.OnChildItemClickListener
import com.app.avy.listenner.OnItemClickListener
import com.app.avy.ui.activity.WebViewActivity
import com.app.avy.ui.dialog.AllAppDialogFragment
import com.app.avy.ui.dialog.SpeechDialogFragment
import com.app.avy.ui.fragment.*
import com.app.avy.utils.Constant
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity(), View.OnClickListener, OnItemClickListener, OnChildItemClickListener {
    private var mPermissions = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                ),
                1
            )
        }

        supportFragmentManager.beginTransaction().replace(R.id.container_main, HomeFragment.newInstance(this))
            .addToBackStack(HomeFragment::class.java.simpleName)
            .commit()
        onEvenClick()
    }


    private fun checkPermission(mPermissions: Array<String>): Boolean {
        var allSuccess = true
        for (i in mPermissions.indices) {
            if (PermissionChecker.checkCallingOrSelfPermission(
                    this,
                    mPermissions[i]
                ) == PackageManager.PERMISSION_DENIED
            ) {
                allSuccess = false
            }
        }
        return allSuccess
    }


    fun onEvenClick() {
        img_back!!.setOnClickListener(this)
        img_speech!!.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {

            }
            R.id.img_speech -> {
                SpeechDialogFragment().show(supportFragmentManager, "dialog_speech")
            }
            R.id.img_noti -> {

            }
        }
    }

    override fun onItemClick(id: Int) {
        var intent = Intent(this, WebViewActivity::class.java)
        when (id) {
            R.id.layout_control -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_main, ControlFragment.newInstance(this))
                    .addToBackStack(ControlFragment::class.java.simpleName)
                    .commit()
            }
            R.id.layout_setting -> {
                supportFragmentManager.beginTransaction().replace(R.id.container_main, SettingFragment())
                    .addToBackStack(SettingFragment::class.java.simpleName)
                    .commit()
            }
            R.id.layout_all_app -> {
                AllAppDialogFragment().show(supportFragmentManager, "dialog")
            }
            R.id.layout_natrition -> {
                intent.putExtra(Constant.BUNDLE_WEB_URL, "https://www.cooky.vn/")
                startActivity(intent)
            }

            R.id.layout_mart -> {
                intent.putExtra(Constant.BUNDLE_WEB_URL, "https://avymart.sweb-demo.info/")
                startActivity(intent)
            }

            R.id.layout_manager -> {
                supportFragmentManager.beginTransaction().replace(R.id.container_main, ManageFragment())
                    .addToBackStack(ManageFragment::class.java.simpleName)
                    .commit()
            }

        }
    }

    override fun inChildItemClick(id: Int) {
        when (id) {
            R.id.layout_setup -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_main, SetupHotkeyFragment())
                    .addToBackStack(SetupHotkeyFragment::class.java.simpleName)
                    .commit()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            527 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return
            }
        }
    }


    override fun onBackPressed() {
        var fragment = supportFragmentManager.findFragmentById(R.id.container_main)
        when (fragment) {
            is HomeFragment -> {
                finish()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }


}
