data class Settings(
    val notificationInterval: Int = 30, // minutes
    val notifyOnHighAqi: Boolean = true,
    val highAqiThreshold: Int = 100,
    val notificationsEnabled: Boolean = true
) 