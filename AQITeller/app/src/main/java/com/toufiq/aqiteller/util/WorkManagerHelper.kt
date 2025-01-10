package com.toufiq.aqiteller.util

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.toufiq.aqiteller.worker.AqiWorker
import com.toufiq.aqiteller.data.repository.SettingsRepository

object WorkManagerHelper {
    private const val AQI_WORK_NAME = "aqi_periodic_work"
    private const val MIN_BACKOFF_MILLIS = 60000L // 1 minute minimum backoff

    fun setupPeriodicAqiCheck(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Get initial interval from settings
        val intervalMinutes = runBlocking {
            SettingsRepository(context).settings.first().notificationInterval
        }

        // Ensure minimum interval is 15 minutes (WorkManager requirement)
        val finalInterval = maxOf(intervalMinutes, 15)

        val workRequest = PeriodicWorkRequestBuilder<AqiWorker>(
            finalInterval.toLong(), TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                AQI_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
    }

    fun updatePeriodicAqiCheck(context: Context, intervalMinutes: Int) {
        // Ensure minimum interval is 15 minutes
        val finalInterval = maxOf(intervalMinutes, 15)
        
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<AqiWorker>(
            finalInterval.toLong(), TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                AQI_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
    }

    fun cancelPeriodicAqiCheck(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(AQI_WORK_NAME)
    }
} 