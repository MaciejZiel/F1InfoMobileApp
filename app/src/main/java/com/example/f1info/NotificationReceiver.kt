package com.example.f1info

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val raceName = intent.getStringExtra("race_name") ?: "Wyścig F1"

        val builder = NotificationCompat.Builder(context, "f1_channel")
            .setSmallIcon(R.drawable.ic_notification) // 👉 własna ikona, patrz niżej
            .setContentTitle("Nadchodzący wyścig!")
            .setContentText("Już jutro: $raceName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false) // 👈 niech zostaje widoczne
            .setOngoing(false)    // 👈 nie jako „ciągłe”
            .setDefaults(NotificationCompat.DEFAULT_ALL) // dźwięk + wibracja

        if (androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            with(NotificationManagerCompat.from(context)) {
                notify(1001, builder.build())
            }
        } else {
            android.util.Log.w("NotificationReceiver", "Brak uprawnień POST_NOTIFICATIONS")
        }

    }
}
