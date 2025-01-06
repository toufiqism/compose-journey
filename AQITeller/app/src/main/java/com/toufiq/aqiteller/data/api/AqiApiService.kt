import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface AqiApiService {
    @Headers("X-Api-Key: nfbK0fQv/Ikx91nSQ0OLIw==3e33TZv6bpGu97R2")
    @GET("v1/airquality")
    suspend fun getAirQuality(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): AqiResponse
} 