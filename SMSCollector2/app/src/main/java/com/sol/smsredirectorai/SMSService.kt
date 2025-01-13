package com.sol.smsredirectorai

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.sol.smsredirectorai.data.AppDatabase
import com.sol.smsredirectorai.data.AppPreferences
import com.sol.smsredirectorai.data.SmsData
import com.sol.smsredirectorai.api.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class SMSService : Service() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var database: AppDatabase
    private lateinit var appPreferences: AppPreferences
    private lateinit var apiService: ApiService

    companion object {
        private const val CHANNEL_ID = "sms_service_channel"
        private const val NOTIFICATION_ID = 1
        const val ACTION_SEND_LAST_SMS = "com.sol.smsredirectorai.SEND_LAST_SMS"
    }

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        appPreferences = AppPreferences(this)
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).addInterceptor(logging).build()
        apiService = Retrofit.Builder()
            .baseUrl("https://placeholder.com/") // Base URL is not used as we use dynamic URLs
            .addConverterFactory(GsonConverterFactory.create())
            .client(client).build()
            .create(ApiService::class.java)

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())

        // Try to send any pending SMS
        scope.launch {
            sendPendingSms()
        }
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SEND_LAST_SMS -> {
                scope.launch {
                    val lastSms = database.smsDao().getLastSms()
                    lastSms?.let { sendSmsToApi(it) }
                }
            }

            else -> {
                val sender = intent?.getStringExtra("sender")
                val message = intent?.getStringExtra("message")
                val simSlot = intent?.getStringExtra("simSlot")

                if (sender != null && message != null) {
                    scope.launch {
                        handleNewSms(sender, message, simSlot ?: "undetected")
                    }
                }
            }
        }
        return START_STICKY
    }

    private suspend fun handleNewSms(sender: String, message: String, simSlot: String) {
        val smsData = SmsData.create(
            sender = sender,
            receiver = "", // Add receiver logic if needed
            body = message,
            simSlot = simSlot
        )

        try {
            sendSmsToApi(smsData)
        } catch (e: Exception) {
            Log.e("SMSService", "Failed to send SMS, saving to database", e)
            database.smsDao().insert(smsData)
        }
    }

    private suspend fun sendSmsToApi(smsData: SmsData) {
        val apiUrl = appPreferences.apiUrl.first()
        if (apiUrl.isEmpty()) {
            Log.e("SMSService", "API URL not configured")
            database.smsDao().insert(smsData)
            return
        }

        try {
            val response = apiService.sendSms(apiUrl, smsData)
            if (response.isSuccessful) {
                Log.d("SMSService", "Successfully sent SMS to API")
            } else {
                throw IOException("API call failed with code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("SMSService", "Failed to send SMS to API", e)
            database.smsDao().insert(smsData)
            throw e
        }
    }

    private suspend fun sendPendingSms() {
        val pendingSms = database.smsDao().getAllPendingSms()
        pendingSms.forEach { sms ->
            try {
                sendSmsToApi(sms)
                database.smsDao().delete(sms)
            } catch (e: Exception) {
                Log.e("SMSService", "Failed to send pending SMS", e)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "SMS Service Channel"
            val descriptionText = "Channel for SMS Redirector Service"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(lastMessage: String? = null): android.app.Notification {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SMS Redirector Active")
            .setContentText(lastMessage ?: "Monitoring for new SMS messages")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
} 