import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class LocationScheduler {
    fun scheduleLocationWork() {
        val locationWorkRequest = PeriodicWorkRequest.Builder(LocationWorker::class.java, 15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "LocationWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            locationWorkRequest
        )
    }
}