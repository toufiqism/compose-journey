data class AqiResponse(
    val overall_aqi: Int,
    val CO: AqiData?,
    val PM10: AqiData?,
    val SO2: AqiData?,
    val PM2_5: AqiData?,
    val O3: AqiData?,
    val NO2: AqiData?
)

data class AqiData(
    val concentration: Double,
    val aqi: Int
) 