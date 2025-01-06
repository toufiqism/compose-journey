import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object WorkManagerHelper {
    private const val AQI_WORK_NAME = "aqi_periodic_work"

    fun setupPeriodicAqiCheck(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Get initial interval from settings
        val intervalMinutes = runBlocking {
            SettingsRepository(context).settings.first().notificationInterval
        }

        val workRequest = PeriodicWorkRequestBuilder<AqiWorker>(
            intervalMinutes.toLong(), TimeUnit.MINUTES,
            5, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                AQI_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                workRequest
            )
    }

    fun updatePeriodicAqiCheck(context: Context, intervalMinutes: Int) {
        // Cancel existing work
        WorkManager.getInstance(context).cancelUniqueWork(AQI_WORK_NAME)
        
        // Setup new work with updated interval
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<AqiWorker>(
            intervalMinutes.toLong(), TimeUnit.MINUTES,
            5, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
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