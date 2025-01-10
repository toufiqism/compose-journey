package com.toufiq.factsninja.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.toufiq.factsninja.FactsNinjaApp.Companion.NOTIFICATION_CHANNEL_ID
import com.toufiq.factsninja.R
import com.toufiq.factsninja.di.DatabaseModule
import com.toufiq.factsninja.di.NetworkModule
import com.toufiq.factsninja.data.repository.FactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FactsNotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val repository = FactsRepository(
                NetworkModule.factsApi,
                DatabaseModule.getDatabase(context).factsDao()
            )

            repository.fetchNewFact()
                .onSuccess { fact ->
                    showNotification(fact.fact)
                }
                .onFailure {
                    return@withContext Result.failure()
                }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(factText: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_fact)
            .setContentTitle("New Fact!")
            .setContentText(factText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
} 