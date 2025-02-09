package com.sol.bluetoothlifecycleapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.sol.bluetoothlifecycleapp.R
import com.sol.bluetoothlifecycleapp.data.model.BluetoothState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BluetoothService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val binder = BluetoothBinder()
    private var currentDevice: BluetoothDevice? = null
    
    private val _connectionState = MutableStateFlow<BluetoothState>(BluetoothState.Idle)
    val connectionState: StateFlow<BluetoothState> = _connectionState

    inner class BluetoothBinder : Binder() {
        fun getService(): BluetoothService = this@BluetoothService
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        return START_STICKY
    }

    fun connectToDevice(device: BluetoothDevice) {
        serviceScope.launch {
            try {
                _connectionState.value = BluetoothState.Connecting
                currentDevice = device
                // Implement actual connection logic here
                _connectionState.value = BluetoothState.Connected(device.name ?: "Unknown Device")
                updateNotification(device.name ?: "Unknown Device")
            } catch (e: Exception) {
                _connectionState.value = BluetoothState.Error("Failed to connect: ${e.message}")
            }
        }
    }

    fun disconnect() {
        serviceScope.launch {
            try {
                currentDevice = null
                _connectionState.value = BluetoothState.Idle
                stopForeground(true)
                stopSelf()
            } catch (e: Exception) {
                _connectionState.value = BluetoothState.Error("Failed to disconnect: ${e.message}")
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Bluetooth Connection",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Bluetooth Connection")
        .setContentText("Maintaining connection...")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .build()

    private fun updateNotification(deviceName: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Connected to Device")
            .setContentText("Connected to $deviceName")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val CHANNEL_ID = "bluetooth_service_channel"
        private const val NOTIFICATION_ID = 1
    }
} 