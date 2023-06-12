package com.uoc.condo.mismapasdeprueba

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANNEL_ID ="NOTIFICATION_CHANNEL"
const val CHANNEL_NAME="com.uoc.condo.mismapasdeprueba"

class MyFirebaseMessagingService: FirebaseMessagingService() {

    private fun generatenotification(title: String, message: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var pendingIntent: PendingIntent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        //Channel id, channel name
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoveView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

        }
        notificationManager.notify(0, builder.build() )

    }

    @SuppressLint("RemoveViewLayout")
    private fun getRemoveView(title: String, message: String): RemoteViews? {
        val remoteView = RemoteViews("com.uoc.condo.mismapasdeprueba", R.drawable.ic_notifications)
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.message, message)
        remoteView.setImageViewResource(R.id.image, R.drawable.ic_notifications)

        return remoteView
    }
//https://www.youtube.com/watch?v=zmBbhxIrneM notifications
    override fun onMessageReceived(message: RemoteMessage) {
        //super.onMessageReceived(message)
        if (message.notification != null){
            generatenotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }
}