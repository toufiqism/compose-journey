package com.toufiq.firebasetrackerapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.database.FirebaseDatabase
import com.toufiq.firebasetrackerapp.MainActivity
import com.toufiq.firebasetrackerapp.R
import com.toufiq.firebasetrackerapp.data.DeviceData
import com.toufiq.firebasetrackerapp.data.LocalDataStore
import com.toufiq.firebasetrackerapp.utils.NetworkUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await

class DataCollectorService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    private val database = FirebaseDatabase.getInstance().getReference("data")
    private var intervalMillis = 60000L
    private lateinit var localDataStore: LocalDataStore

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "DataCollectorChannel"
        const val ACTION_STOP_SERVICE = "com.toufiq.firebasetrackerapp.STOP_SERVICE"
    }

    override fun onCreate() {
        super.onCreate()
        localDataStore = LocalDataStore(this)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        observeNetworkChanges()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP_SERVICE -> {
                stopSelf()
                return START_NOT_STICKY
            }
        }
        
        intervalMillis = intent?.getLongExtra("interval", 60000L) ?: 60000L
        startDataCollection()
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Data Collector Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Collecting device data"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        // Create an intent for the stop action
        val stopIntent = Intent(this, DataCollectorService::class.java).apply {
            action = ACTION_STOP_SERVICE
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Data Collector Active")
            .setContentText("Collecting device data")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .addAction(
                android.R.drawable.ic_media_pause,
                "Stop Collection",
                stopPendingIntent
            )
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun observeNetworkChanges() {
        serviceScope.launch {
            NetworkUtils.observeNetworkState(this@DataCollectorService).collect { isConnected ->
                if (isConnected) {
                    uploadPendingData()
                }
            }
        }
    }

    private suspend fun uploadPendingData() {
        try {
            val pendingData = localDataStore.getAllPendingData()
            pendingData.forEach { (key, data) ->
                try {
                    database.child("data").push().setValue(data).await()
                    localDataStore.removeData(key)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startDataCollection() {
        serviceScope.launch {
            while (true) {
                try {
                    val deviceData = collectData()
                    if (NetworkUtils.isInternetAvailable(this@DataCollectorService)) {
                        try {
                            database.child("data").push().setValue(deviceData).await()
                        } catch (e: Exception) {
                            localDataStore.saveData(deviceData)
                        }
                    } else {
                        localDataStore.saveData(deviceData)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(intervalMillis)
            }
        }
    }

    private suspend fun collectData(): DeviceData {
        val location = try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
        } catch (e: Exception) {
            null
        }

        val batteryStatus = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val batteryLevel = batteryStatus?.let { intent ->
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale
        } ?: 0

        val isCharging = batteryStatus?.let { intent ->
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            status == BatteryManager.BATTERY_STATUS_CHARGING
        } ?: false

        return DeviceData(
            latitude = location?.latitude ?: 0.0,
            longitude = location?.longitude ?: 0.0,
            batteryLevel = batteryLevel,
            isCharging = isCharging,
            deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        )
    }

    override fun onBind(intent: Intent?): IBinder? = null
} 