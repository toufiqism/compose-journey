package com.toufiq.aqiteller.util

import android.app.Application

class WorkManagerUtil(private val application: Application) {
    fun setupNotifications(enabled: Boolean) {
        if (enabled) {
            WorkManagerHelper.setupPeriodicAqiCheck(application)
        } else {
            WorkManagerHelper.cancelPeriodicAqiCheck(application)
        }
    }

    fun updateInterval(minutes: Int) {
        WorkManagerHelper.updatePeriodicAqiCheck(application, minutes)
    }
} 