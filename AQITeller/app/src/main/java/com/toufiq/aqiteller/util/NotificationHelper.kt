package com.toufiq.aqiteller.util

import AqiResponse
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.toufiq.aqiteller.R
import com.toufiq.aqiteller.getAqiAdvice

object NotificationHelper {
    fun showAqiNotification(context: Context, aqiResponse: AqiResponse) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val notification = NotificationCompat.Builder(context, "aqi_channel")
            .setContentTitle("Current Air Quality")
            .setContentText("AQI: ${aqiResponse.overall_aqi} - ${getAqiAdvice(aqiResponse.overall_aqi)}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }
} 