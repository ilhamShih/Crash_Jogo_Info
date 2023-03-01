package com.crash.blazecrash.appcrash.main

import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetrica.enableActivityAutoTracking
import com.yandex.metrica.YandexMetricaConfig
import com.yandex.metrica.push.YandexMetricaPush

class ActivityApplication : Application() {

    val config = YandexMetricaConfig
        .newConfigBuilder("6d5bceea-bb45-4d3e-b532-e6ec22fdce28")
        .build()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (packageName != getProcessName()) {
                WebView.setDataDirectorySuffix(getProcessName())
            }
        }
        YandexMetrica.activate(this, config).apply {
            enableActivityAutoTracking(this@ActivityApplication)
        }
        YandexMetricaPush.init(this)
    }
}