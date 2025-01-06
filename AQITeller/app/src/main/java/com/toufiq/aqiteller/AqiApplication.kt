package com.toufiq.aqiteller

import WorkManagerHelper
import android.app.Application
import androidx.work.Configuration

class AqiApplication : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        // Initialize work manager
        WorkManagerHelper.setupPeriodicAqiCheck(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
} 