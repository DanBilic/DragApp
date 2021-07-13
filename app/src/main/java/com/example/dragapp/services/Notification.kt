package com.example.dragapp.services

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dragapp.DashboardActivity
import com.example.dragapp.R
import com.example.dragapp.utils.Constants

class Notification : BroadcastReceiver() {
    lateinit var builder: NotificationCompat.Builder
    lateinit var context: Context

    override fun onReceive(mcontext: Context?, intent: Intent?) {
        if (mcontext != null) {
            showNotification(mcontext, 0)
        }
    }

    fun showNotification(context: Context, notificationId: Int) {
        val intent = Intent(context, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("DRAG")
            .setContentText("Your screen time is a bit long" + "\n" + "Do some other activities to relax! :D")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}