package com.crash.blazecrash.appcrash.push

import com.crash.blazecrash.appcrash.sharepref.Save.tokenSavePref
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yandex.metrica.push.firebase.MetricaMessagingService


class FirebaseService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        MetricaMessagingService().processPush(this, remoteMessage)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if(tokenSavePref != token){
            tokenSavePref = token
        }
        MetricaMessagingService().processToken(this, token)
        MetricaMessagingService().onNewToken(token)
    }
}