package com.crash.blazecrash.appcrash.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.crash.blazecrash.appcrash.sharepref.Save

class UseCaseWebViewClient(val webViewClientImpl: WebViewClientImpl) : WebViewClient() {


    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        if (Uri.parse(url).host.equals("google.com")) {
            (view?.context as Activity).requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            (view?.context as Activity).requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        webViewClientImpl.onProgressStart()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        webViewClientImpl.onProgressFinish()
    }

    override fun onReceivedError(
        view: WebView?, request: WebResourceRequest?, error: WebResourceError?
    ) {
        if (request!!.isForMainFrame) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                when (error?.errorCode) {
                    ERROR_HOST_LOOKUP -> {
                        webViewClientImpl.onProgressError(Save.ERROR_HOST_LOOKUP)
                    }
                    ERROR_CONNECT -> {
                        webViewClientImpl.onProgressError(Save.ERROR_CONNECT)
                    }
                    ERROR_TIMEOUT -> {
                        webViewClientImpl.onProgressError(Save.ERROR_TIMEOUT)
                    }
                }
            }
        }
        super.onReceivedError(view, request, error)
    }


    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        return if ((request?.url?.scheme.equals("market")) || (request?.url?.host.equals("play.google.com"))) {
            request?.let { (view as Activity).openLinkMarket(it) }
            view?.requestFocus()
            true
        } else
            false
    }

    fun Context.openLinkMarket(request: WebResourceRequest) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(request.url.toString())))
    }
}