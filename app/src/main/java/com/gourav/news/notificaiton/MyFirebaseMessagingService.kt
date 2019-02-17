package com.gourav.news.notificaiton

import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.gourav.news.R
import com.gourav.news.ui.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        if (remoteMessage!!.data.isNotEmpty()){
            val CHANNEL_ID = "4844"

            val notifyIntent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            notifyIntent.putExtra("Title", remoteMessage.data!!["title"])
            notifyIntent.putExtra("Data", remoteMessage.data!!["body"])
            val notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )

            val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(remoteMessage.data!!["title"])
                .setContentText(remoteMessage.data!!["body"])
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(notifyPendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0 /* ID of notification */, mBuilder.build())
        }
    }

    override fun onNewToken(token: String?) {
        val tokenMap = HashMap<String, Any>()
        tokenMap["token"] = token!!
        val db = FirebaseFirestore.getInstance()
        db.collection("Token").document().set(tokenMap)
    }
}
