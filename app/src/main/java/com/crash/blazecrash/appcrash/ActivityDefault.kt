package com.crash.blazecrash.appcrash

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.ValueCallback
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import com.crash.blazecrash.appcrash.Helper.arrayOfUris
import com.crash.blazecrash.appcrash.Helper.createCR
import com.crash.blazecrash.appcrash.data.UseCaseChromeClient
import com.crash.blazecrash.appcrash.data.UseCaseWebViewClient
import com.crash.blazecrash.appcrash.data.WebViewClientImpl
import com.crash.blazecrash.appcrash.databinding.ActivityDefaultBinding
import com.crash.blazecrash.appcrash.main.FragmentPolitika
import com.crash.blazecrash.appcrash.sharepref.Save.BASE_URL
import com.crash.blazecrash.appcrash.sharepref.Save.EMAIL
import com.crash.blazecrash.appcrash.sharepref.Save.fichaSavePref
import com.crash.blazecrash.appcrash.sharepref.Save.gaidSavePref
import com.crash.blazecrash.appcrash.sharepref.Save.tokenSavePref
import com.crash.blazecrash.appcrash.sharepref.Save.yandexSavePref
import com.google.android.material.card.MaterialCardView


class ActivityDefault : AppCompatActivity(), WebViewClientImpl, OnMenuItemClickListener,
    View.OnLayoutChangeListener {
    private var boolean = false
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private lateinit var view: View
    private lateinit var closeDialog: MaterialCardView
    private var valueCallback: ValueCallback<Array<Uri>>? = null
    private var launcherString: ActivityResultLauncher<String>? = null
    private var launcherIntent: ActivityResultLauncher<Intent>? = null
    private lateinit var binding: ActivityDefaultBinding
    private var timExit = 0L

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDefaultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!fichaSavePref) {
            customDialog()
        }
        binding.contentWebView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportMultipleWindows(false)
            }
            loadUrl("$BASE_URL$gaidSavePref&device_id=$yandexSavePref&token=$tokenSavePref")
            webViewClient = UseCaseWebViewClient(this@ActivityDefault)
            webChromeClient = UseCaseChromeClient(this@ActivityDefault)
        }
        binding.reset.setOnClickListener {
            binding.contentWebView.reload()
            binding.contentWebView.visibility = VISIBLE
            binding.linearLayout.visibility = GONE
        }

        launcherString = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when (it) {
                true -> {
                    launcherIntent?.launch(createCR())
                }
                else -> {
                    try {
                        valueCallback?.onReceiveValue(arrayOf())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        launcherIntent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode != 0)
                    valueCallback?.onReceiveValue(arrayOfUris(result))
                else
                    valueCallback?.onReceiveValue(arrayOf())
            }
    }

    override fun onStart() {
        super.onStart()
        binding.bottomAppBar.title = getString(R.string.app_name)
        binding.bottomAppBar.setOnMenuItemClickListener(this)
        binding.bottomAppBar.addOnLayoutChangeListener(this)

    }


    override fun onProgressStart() {
        binding.progress.show()
        binding.progress.progress = 0
    }

    override fun onProgress(progress: Int) {
        binding.progress.setProgressCompat(progress, true)
    }

    override fun onProgressFinish() {
        binding.progress.hide()
    }

    override fun onProgressError(error: String) {
        binding.contentWebView.visibility = GONE
        binding.linearLayout.visibility = VISIBLE
        binding.textError.text = error
    }

    override fun onShowFileChooser(filePathCallback: ValueCallback<Array<Uri>>?) {
        valueCallback = filePathCallback
    }

    override fun onPermission(boolean: Boolean) {
        launcherString?.launch(READ_EXTERNAL_STORAGE)
    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK && binding.contentWebView.canGoBack()) {
            binding.contentWebView.goBack()
        } else {
            if(System.currentTimeMillis() - timExit > 1000){
                Toast.makeText(this, getString(R.string.exit_app) , Toast.LENGTH_SHORT).show()
                timExit = System.currentTimeMillis()
            }else{
                finish()
            }

        }
        return true
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        when (p0?.itemId) {
            R.id.view_politica -> {
                val modalBottomSheet = FragmentPolitika()
                modalBottomSheet.show(supportFragmentManager, FragmentPolitika.TAG)
                return true
            }
            R.id.start_email -> {
                val mess = Intent(Intent.ACTION_SENDTO)
                mess.data = Uri.parse(EMAIL)
                try {
                    startActivity(Intent.createChooser(mess, "E-Mail"))
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(this, R.string.email_client_not, Toast.LENGTH_SHORT).show()
                }
                return true
            }
        }
        return false
    }

    override fun onLayoutChange(
        p0: View?, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int
    ) {
        if (!boolean) {
            binding.bottomAppBar.layoutParams.height = 1
            p0?.layoutParams = binding.bottomAppBar.layoutParams
            boolean = true
        } else {
            binding.bottomAppBar.layoutParams.height = 100
            p0?.layoutParams = binding.bottomAppBar.layoutParams
            boolean = false
        }
    }

    override fun onStop() {
        super.onStop()
        binding.bottomAppBar.removeOnLayoutChangeListener(this)
    }

    /**------ Custom dialogue -----*/
    private fun customDialog() {
        view = this.layoutInflater.inflate(R.layout.custom_dialog, null)
        closeDialog = view.findViewById<View>(R.id.close) as MaterialCardView
        builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(view)
        dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        closeDialog.setOnClickListener {
            dialog.cancel()
            fichaSavePref = true
        }
    }
}