package com.toufiq.aqiteller.util

import AqiResponse
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.toufiq.aqiteller.MainActivity
import com.toufiq.aqiteller.R

object NotificationHelper {
    private const val NOTIFICATION_ID = 1

    fun showAqiNotification(context: Context, aqiResponse: AqiResponse) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create an intent that opens the app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, "aqi_channel")
            .setContentTitle("Air Quality Update")
            .setContentText("AQI: ${aqiResponse.overall_aqi} - ${getAqiAdvice(aqiResponse.overall_aqi)}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun getAqiAdvice(aqi: Int): String {
        return when {
            aqi <= 50 -> "Air quality is good"
            aqi <= 100 -> "Moderate air quality"
            aqi <= 150 -> "Unhealthy for sensitive groups"
            aqi <= 200 -> "Unhealthy air quality"
            aqi <= 300 -> "Very unhealthy air quality"
            else -> "Hazardous air quality"
        }
    }
} 