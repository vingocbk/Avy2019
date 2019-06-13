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
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.app.avy.snowboysupport.SnowboySupport
import java.util.*


class MainActivity : BaseActivity(), View.OnClickListener, OnItemClickListener, OnChildItemClickListener,
    SnowboySupport.OnSpeechListenner, RecognitionListener {

    val TAG = MainActivity::class.java.simpleName

    var count: Int = 0
    var mTvCount: AppCompatTextView? = null

    private var mPermissions = arrayOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )

    override fun getId() = R.layout.activity_main
    override fun onViewReady() {
        mTvCount = tv_count
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
                mSnownoySupport = SnowboySupport(this, this)
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
        initCheckPermission()
        supportFragmentManager.beginTransaction().replace(R.id.container_main, HomeFragment.newInstance(this))
            .addToBackStack(HomeFragment::class.java.simpleName)
            .commit()
        onEvenClick()
    }

    private var mSnownoySupport: SnowboySupport? = null
    private var speechRecognizer: SpeechRecognizer? = null

    override fun onActive() {
        count++
        mSnownoySupport?.stopRecording()
        showDialog()
        listen()
        mTvCount?.text = count.toString()
    }

    override fun onReadyForSpeech(params: Bundle?) {

    }

    override fun onRmsChanged(rmsdB: Float) {

    }

    override fun onBufferReceived(buffer: ByteArray?) {
        
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val data = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val word = data?.get(data.size - 1) as String
        Log.e(TAG, "onPartialResults $word ")
        (application as MyApplication).bus().send(word.trim())
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
    }

    override fun onBeginningOfSpeech() {
    }

    override fun onEndOfSpeech() {
    }

    override fun onError(error: Int) {
        Log.e(TAG, "onError ")
        val errorMessage = mSnownoySupport?.getErrorText(error)
        dismiss()
        mSnownoySupport?.startRecording()
        (application as MyApplication).bus().send(errorMessage)
    }

    override fun onResults(results: Bundle?) {
        dismiss()
        val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)!!
        val str = data[0].toString()
        mSnownoySupport?.startRecording()
        Log.e(TAG, "onResults ")
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
                mSnownoySupport?.stopRecording()
                showDialog()
                listen()
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain =
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                            permissions[i]
                        )
                }
                if (allSuccess) {
                    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
                    mSnownoySupport = SnowboySupport(this, this)
                }
            }
        }
    }


    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container_main)
        when (fragment) {
            is HomeFragment -> {
                finish()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun onDestroy() {
        mSnownoySupport?.restoreVolume()
        mSnownoySupport?.stopRecording()
        if (speechRecognizer != null) {
            speechRecognizer!!.destroy()
        }
        super.onDestroy()
    }

    private fun listen() {
        // start record google
        //if (isPause) return;
        speechRecognizer?.setRecognitionListener(this@MainActivity)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, Locale.US)
        speechRecognizer?.startListening(intent)
    }

    private fun initCheckPermission() {
        if (SDK_INT >= M) {
            if (checkPermission(mPermissions)) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
                mSnownoySupport = SnowboySupport(this, this)
            } else {
                requestPermissions(mPermissions, 1)
            }
        }
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


}
