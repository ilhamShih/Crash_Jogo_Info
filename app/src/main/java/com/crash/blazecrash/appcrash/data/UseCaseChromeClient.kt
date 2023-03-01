package com.crash.blazecrash.appcrash.data

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

class UseCaseChromeClient(val webViewClientImpl: WebViewClientImpl) : WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        webViewClientImpl.onProgress(newProgress)
        if (newProgress == 100) {
            webViewClientImpl.onProgressFinish()
        }
    }



    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        webViewClientImpl.onShowFileChooser(filePathCallback)
        webViewClientImpl.onPermission(true)
        return true
    }


}