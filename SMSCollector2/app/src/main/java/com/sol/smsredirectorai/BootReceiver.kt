package com.sol.smsredirectorai

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            Log.d("BootReceiver", "Boot completed, scheduling SMS service work")

            val workRequest = OneTimeWorkRequestBuilder<ServiceStarterWorker>().build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}

class ServiceStarterWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val serviceIntent = Intent(applicationContext, SMSService::class.java)

        // Check the Android version and start the service appropriately
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("ServiceStarterWorker", "Starting foreground service")
            applicationContext.startForegroundService(serviceIntent)
        } else {
            Log.d("ServiceStarterWorker", "Starting background service")
            applicationContext.startService(serviceIntent)
        }

        return Result.success()
    }
}