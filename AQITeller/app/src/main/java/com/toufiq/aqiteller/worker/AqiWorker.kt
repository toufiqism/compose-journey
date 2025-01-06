import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
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
            if (!settings.notificationsEnabled) {
                return Result.success()
            }

            val location = locationHelper.getCurrentLocation()
            if (location == null) {
                return Result.retry()
            }

            repository.getAirQuality(location.latitude, location.longitude)
                .onSuccess { response ->
                    // Check if AQI is above threshold when high AQI notifications are enabled
                    if (!settings.notifyOnHighAqi || 
                        (response.overall_aqi > settings.highAqiThreshold)
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