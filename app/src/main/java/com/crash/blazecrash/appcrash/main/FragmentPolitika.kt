package com.crash.blazecrash.appcrash.main

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

import com.crash.blazecrash.appcrash.databinding.FragmentPoliticaBinding
import com.crash.blazecrash.appcrash.sharepref.Save.POLIT_URL
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FragmentPolitika : BottomSheetDialogFragment() {

    var binding: FragmentPoliticaBinding? = null
    val bind get() = binding ?: throw RuntimeException(" Error FragmentPoliticaBinding = null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPoliticaBinding.inflate(inflater, container, false)
        return bind.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.contentWebView.apply {
            loadUrl(POLIT_URL)
            settings.javaScriptEnabled = true
            webViewClient = webViewClient()
            webChromeClient = webViewChromeClient()
        }

    }

    private fun webViewChromeClient(): WebChromeClient{
        return object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                bind.progress.setProgressCompat(newProgress, true)
                if (newProgress == 100) {
                    bind.progress.setProgressCompat(100, true)

                }
            }
        }
    }

    private fun webViewClient(): WebViewClient{
        return object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                bind.progress.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                bind.progress.visibility = View.GONE
            }
        }
    }
    companion object {
        const val TAG = "FragmentPolitika"
    }


}