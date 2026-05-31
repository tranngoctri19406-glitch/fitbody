package com.example.fitbody.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.fitbody.R

object NotificationHelper {

    private const val CHANNEL_ID =
        "fitbody_channel"

    fun showNotification(context: Context) {

        val manager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

        // CHANNEL

        if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O
        ) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "FitBody Notification",
                NotificationManager.IMPORTANCE_HIGH
            )

            manager.createNotificationChannel(channel)
        }

        // NOTIFICATION

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID
            )

                .setSmallIcon(R.mipmap.ic_launcher)

                .setContentTitle("FitBody")

                .setContentText(
                    "Đến giờ tập ngực 💪"
                )

                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )

                .build()

        manager.notify(
            1,
            notification
        )
    }
}