import com.toufiq.aqiteller.data.api.AqiApiService

class AqiRepository(private val apiService: AqiApiService) {
    suspend fun getAirQuality(latitude: Double, longitude: Double): Result<AqiResponse> {
        return try {
            val response = apiService.getAirQuality(latitude, longitude)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 