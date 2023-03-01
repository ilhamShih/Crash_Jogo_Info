package com.crash.blazecrash.appcrash.data

import android.net.Uri
import android.webkit.ValueCallback

interface WebViewClientImpl {

    fun onProgressStart()
    fun onProgressFinish()
    fun onProgressError(error : String)
    fun onProgress(progress : Int)
    fun onShowFileChooser(filePathCallback : ValueCallback<Array<Uri>>?)
    fun onPermission(boolean: Boolean)

}