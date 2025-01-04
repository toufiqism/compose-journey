package com.toufiq.firebasetrackerapp.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DeviceData(
    val timestamp: Long = System.currentTimeMillis(),
    val formattedTimestamp: String = formatTimestamp(System.currentTimeMillis()),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val manufacturer: String = android.os.Build.MANUFACTURER,
    val model: String = android.os.Build.MODEL,
    val androidVersion: String = android.os.Build.VERSION.RELEASE,
    val batteryLevel: Int = 0,
    val isCharging: Boolean = false,
    val networkType: String = "",
    val deviceId: String = ""
) {
    companion object {
        private fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
} 