package com.csr.beneficiary.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.csr.beneficiary.R
import com.csr.beneficiary.activity.MainActivity
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        val notification = remoteMessage.notification
        val data = remoteMessage.data
        Log.e("FROM", remoteMessage.from!!)
        sendNotification(notification, data)
    }

    private fun sendNotification(
        notification: RemoteMessage.Notification?,
        data: Map<String, String>
    ) {
        val icon = BitmapFactory.decodeResource(resources, R.drawable.logo)
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val requestID = System.currentTimeMillis().toInt()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val bundle = Bundle()
        bundle.putString("picture_url", data["picture_url"])
        intent.putExtras(bundle)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT
                    or PendingIntent.FLAG_ONE_SHOT
        )
        val CHANNEL_ID = "01"
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(notification!!.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
            .setContentIntent(pendingIntent)
            .setContentInfo("Hello")
            .setLargeIcon(icon)
            .setColor(resources.getColor(R.color.theme_orange))
            .setLights(Color.RED, 1000, 300)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.drawable.logo)
        try {
            val url = URL(data["picture_url"])
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            val bigPicture = BitmapFactory.decodeStream(inputStream)
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(
                    notification.body
                )
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = getString(R.string.channel_name);
            val name = "Channel_001"
            val description = "Channel Description"
            //String description = getString(R.string.channel_description);
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            //NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel)
        }

        //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build())
    }
}