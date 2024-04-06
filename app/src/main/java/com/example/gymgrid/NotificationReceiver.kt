package com.example.gymgrid

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { ctx ->
            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("my_channel_id", "Recordatorio entrenamiento", NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }

            val notificationBuilder = NotificationCompat.Builder(ctx, "my_channel_id")
                .setSmallIcon(R.drawable.ic_launcher_app_background)
                .setContentTitle("Dia de entrenamiento")
                .setContentText("¡¡Hoy toca entrenar!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}
