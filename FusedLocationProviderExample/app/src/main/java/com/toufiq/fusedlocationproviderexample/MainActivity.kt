package com.toufiq.fusedlocationproviderexample

import LocationWorker
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.toufiq.fusedlocationproviderexample.R
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            val permissionName = it.key
            val isGranted = it.value
            if (isGranted) {
                // Permission is granted
            } else {
                // Permission is denied
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestLocationPermissions()

//        val locationWorkRequest = PeriodicWorkRequestBuilder<LocationWorker>(15, TimeUnit.MINUTES)
//            .build()

        val locationWorkRequest = OneTimeWorkRequestBuilder<LocationWorker>().build()
        WorkManager.getInstance(this).enqueue(locationWorkRequest)

//        WorkManager.getInstance(this)
//            .enqueueUniquePeriodicWork(
//                "LocationWork",
//                ExistingPeriodicWorkPolicy.UPDATE,
//                locationWorkRequest
//            )
    }

    private fun requestLocationPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the location API
            }
            else -> {
                // Request the permission
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                )
            }
        }
    }
}