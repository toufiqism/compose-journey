package com.toufiq.aqiteller.worker

import AqiRepository
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.toufiq.aqiteller.data.repository.SettingsRepository
import com.toufiq.aqiteller.di.NetworkModule
import com.toufiq.aqiteller.util.LocationHelper
import com.toufiq.aqiteller.util.NotificationHelper
import kotlinx.coroutines.flow.first

class AqiWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val locationHelper = LocationHelper(context)
    private val repository = AqiRepository(NetworkModule.aqiApiService)
    private val settingsRepository = SettingsRepository(context)

    override suspend fun doWork(): Result {
        try {
            val settings = settingsRepository.settings.first()
            
            // Don't proceed if notifications are disabled
            if (!settings.notificationsEnabled) {
                return Result.success()
            }

            val location = locationHelper.getCurrentLocation() ?: return Result.retry()

            repository.getAirQuality(location.latitude, location.longitude)
                .onSuccess { response ->
                    // Show notification if either:
                    // 1. High AQI notifications are disabled (show all updates)
                    // 2. High AQI notifications are enabled AND AQI is above threshold
                    if (!settings.notifyOnHighAqi || 
                        (settings.notifyOnHighAqi && response.overall_aqi > settings.highAqiThreshold)
                    ) {
                        NotificationHelper.showAqiNotification(context, response)
                    }
                    return Result.success()
                }
                .onFailure {
                    return Result.retry()
                }

            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
} 