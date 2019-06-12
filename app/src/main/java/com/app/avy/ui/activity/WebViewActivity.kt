package com.app.avy.ui.activity

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.AppCompatTextView
import com.app.avy.BaseActivity
import com.app.avy.R
import com.app.avy.utils.Constant
import kotlinx.android.synthetic.main.activity_natrition.*

class WebViewActivity : BaseActivity() {

    lateinit var url_web: String

    override fun getId() = R.layout.activity_natrition

    override fun onViewReady() {
        url_web = intent.getStringExtra(Constant.BUNDLE_WEB_URL)
        web_view.webViewClient = VideoWebViewClient(tv_link_web)
        web_view.settings.javaScriptEnabled = true
        web_view.settings.builtInZoomControls = true
        web_view.settings.displayZoomControls = false
        web_view.loadUrl(url_web)

    }

    class VideoWebViewClient(var textView: AppCompatTextView) : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            if (request != null) {
                view!!.loadUrl(request.url.toString())
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            textView.text = url
        }
    }

    override fun onBackPressed() {
        if (web_view.canGoBack()) {
            web_view.goBack()
        } else {
            super.onBackPressed()
        }
    }
}