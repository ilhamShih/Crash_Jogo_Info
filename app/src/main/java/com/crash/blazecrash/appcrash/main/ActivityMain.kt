package com.crash.blazecrash.appcrash.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.crash.blazecrash.appcrash.ActivityDefault
import com.crash.blazecrash.appcrash.Helper.ceratePChanel
import com.crash.blazecrash.appcrash.databinding.ActivityMainBinding
import com.crash.blazecrash.appcrash.sharepref.Save.KEY_CHENAL
import com.crash.blazecrash.appcrash.sharepref.Save.gaidSavePref
import com.crash.blazecrash.appcrash.sharepref.Save.notifSavePref
import com.crash.blazecrash.appcrash.sharepref.Save.tokenSavePref
import com.crash.blazecrash.appcrash.sharepref.Save.yandexSavePref
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.yandex.metrica.AppMetricaDeviceIDListener
import com.yandex.metrica.YandexMetrica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ActivityMain : AppCompatActivity(), OnCompleteListener<String>, AppMetricaDeviceIDListener {
    lateinit var binder: ActivityMainBinding

    val yandexIDListener = MutableLiveData<String?>()
    var procent = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)
        progress()

        if (!notifSavePref) {
            lifecycleScope.launch(Dispatchers.IO) {
                ceratePChanel()
                Firebase.messaging.token.addOnCompleteListener(this@ActivityMain)
                Firebase.messaging.subscribeToTopic(KEY_CHENAL)
                notifSavePref = true
            }
        }

        if (yandexSavePref.isBlank()) {
            lifecycleScope.launch(Dispatchers.IO) {
                YandexMetrica.requestAppMetricaDeviceID(this@ActivityMain)
            }

            yandexIDListener.observe(this@ActivityMain) {
                if (it?.isBlank() == true) {
                    yandexSavePref = ""
                    defaultActivity()
                } else {
                    yandexSavePref = it ?: ""
                    defaultActivity()
                }
            }
        } else {
            defaultActivity()
        }
        if (gaidSavePref.isBlank()) {
            lifecycleScope.launch(Dispatchers.IO) {
                gaidAds()
            }
        }
    }

    fun defaultActivity() {
        procent = 50
        lifecycleScope.launch {
            delay(3000)
            startActivity(Intent(this@ActivityMain, ActivityDefault::class.java))
            finish()
        }
    }

    fun gaidAds() {
        MobileAds.initialize(this@ActivityMain)
        gaidSavePref = try {
            AdvertisingIdClient.getAdvertisingIdInfo(this@ActivityMain).id ?: ""
        } catch (except: Exception) {
            ""
        }
    }


    fun progress() {
        lifecycleScope.launch(Dispatchers.IO) {
            while (procent < 99) {
                delay(500)
                procent += (50 / 6)
                binder.progressindicator.setProgressCompat(procent, true)
                binder.progresProcent.post {
                    binder.progresProcent.text = "$procent%"
                }
            }
        }
    }

    override fun onComplete(p0: Task<String>) {
        tokenSavePref = if (!p0.isSuccessful) {
            ""
        } else {
            p0.result

        }
    }

    override fun onLoaded(p0: String?) {
        if (p0 != null) {
            yandexIDListener.postValue(p0)
        }
    }

    override fun onError(p0: AppMetricaDeviceIDListener.Reason) {
        yandexIDListener.postValue("")
    }

}