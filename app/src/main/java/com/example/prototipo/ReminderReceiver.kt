package com.example.projeto.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.prototipo.MainActivity
import com.example.prototipo.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("reminder_channel", "Reminder Notifications", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Channel for Reminder Notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Iniciar som do alarme
        MediaPlayerSingleton.start(context, R.raw.alarm_sound)

        val stopIntent = Intent(context, StopAlarmReceiver::class.java)
        val stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(intent.getStringExtra("title"))
            .setContentText(intent.getStringExtra("message"))
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_stop, "Parar", stopPendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(intent.getIntExtra("notificationId", 0), notification)
    }
}
