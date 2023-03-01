package com.crash.blazecrash.appcrash

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import com.crash.blazecrash.appcrash.sharepref.Save.KEY_CHENAL

object Helper {


    fun arrayOfUris(result: ActivityResult): Array<Uri>? {
        return if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val intent = result.data
            var arrayOfUris: Array<Uri>? = null
            if (intent != null) {
                val uriString = intent.dataString
                arrayOfUris = arrayOf(Uri.parse(uriString))
                val clipData = intent.clipData
                if (clipData != null) {
                    arrayOfUris = Array(clipData.itemCount) { clipData.getItemAt(it).uri }
                }
                if (uriString != null) arrayOfUris = arrayOf(Uri.parse(uriString))
            }
            arrayOfUris
        } else {
            arrayOf()
        }
    }

    fun createCR(): Intent? {
        return Intent.createChooser(
            Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
                putExtra(
                    Intent.EXTRA_MIME_TYPES,
                    arrayOf("image/jpeg", "image/png")
                )
            }, "Image"

        )
    }

    fun Context.ceratePChanel() {
        val vibrate = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        val managerN = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            managerN.createNotificationChannel(
                NotificationChannel(
                    KEY_CHENAL, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableLights(true)
                    enableVibration(true)
                    vibrationPattern = vibrate
                })
        }
    }
}