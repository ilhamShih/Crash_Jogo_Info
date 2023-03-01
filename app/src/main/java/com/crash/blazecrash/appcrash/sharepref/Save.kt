package com.crash.blazecrash.appcrash.sharepref

import android.content.Context

object Save {

    const val ERROR_HOST_LOOKUP = "Erro ao pesquisar o nome do host do servidor ou o servidor proxy"
    const val ERROR_CONNECT = "Falha ao conectar ao servidor"
    const val ERROR_TIMEOUT = "A conexão expirou"
    const val KEY_CHENAL = "crashjogo"
    const val YAN_ID = "00000000-0000-0000-00000-000000000"


    const val POLIT_URL =
        "https://policies.google.com/privacy?hl=ru&fg=1"
    const val EMAIL = "mailto:main@gmail.com"
    const val BASE_URL = "https://www.google.com/?gaid="

    var Context.gaidSavePref: String
        get() = getSharedPreferences(packageName, Context.MODE_PRIVATE).getString(
            "gaidSavePref",
            ""
        )
            .toString()
        set(value) {
            getSharedPreferences(packageName, Context.MODE_PRIVATE).edit()
                .putString("gaidSavePref", value)
                .apply()

        }

    var Context.notifSavePref: Boolean
        get() = getSharedPreferences(packageName, Context.MODE_PRIVATE).getBoolean(
            "notifSavePref",
            false
        )
        set(value) {
            getSharedPreferences(packageName, Context.MODE_PRIVATE).edit()
                .putBoolean("notifSavePref", value)
                .apply()

        }


    var Context.yandexSavePref: String
        get() = getSharedPreferences(packageName, Context.MODE_PRIVATE).getString(
            "yandexSavePref",
            ""
        )
            .toString()
        set(value) {
            getSharedPreferences(packageName, Context.MODE_PRIVATE).edit()
                .putString("yandexSavePref", value)
                .apply()

        }

    var Context.tokenSavePref: String
        get() = getSharedPreferences(packageName, Context.MODE_PRIVATE).getString(
            "tokenSavePref",
            ""
        )
            .toString()
        set(value) {
            getSharedPreferences(packageName, Context.MODE_PRIVATE).edit()
                .putString("tokenSavePref", value)
                .apply()

        }

    var Context.fichaSavePref: Boolean
        get() = getSharedPreferences(packageName, Context.MODE_PRIVATE).getBoolean(
            "fichaSavePref",
            false
        )
        set(value) {
            getSharedPreferences(packageName, Context.MODE_PRIVATE).edit()
                .putBoolean("fichaSavePref", value)
                .apply()

        }
}