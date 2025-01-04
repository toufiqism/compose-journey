package com.toufiq.firebasetrackerapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.toufiq.firebasetrackerapp.service.DataCollectorService
import com.toufiq.firebasetrackerapp.ui.components.AnimatedCounter
import com.toufiq.firebasetrackerapp.ui.components.CollectingAnimation
import com.toufiq.firebasetrackerapp.ui.theme.FirebaseTrackerAppTheme
import com.toufiq.firebasetrackerapp.ui.theme.BrightBlue
import com.toufiq.firebasetrackerapp.ui.theme.BrightGreen
import com.toufiq.firebasetrackerapp.ui.theme.BrightPink
import com.toufiq.firebasetrackerapp.ui.theme.BrightPurple
import com.toufiq.firebasetrackerapp.ui.theme.BrightYellow

class MainActivity : ComponentActivity() {
    companion object {
        private const val FOREGROUND_SERVICE_REQUEST_CODE = 100
        private const val PERMISSION_FOREGROUND_SERVICE_LOCATION = "android.permission.FOREGROUND_SERVICE_LOCATION"
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                if (Build.VERSION.SDK_INT >= 34) {
                    foregroundServicePermissionRequest.launch(PERMISSION_FOREGROUND_SERVICE_LOCATION)
                } else {
                    startDataCollectorService()
                }
            }
            else -> {
                // Handle permission denial
            }
        }
    }

    private val foregroundServicePermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startDataCollectorService()
        }
    }

    private fun requestForegroundServicePermission() {
        if (Build.VERSION.SDK_INT >= 34) {
            foregroundServicePermissionRequest.launch(PERMISSION_FOREGROUND_SERVICE_LOCATION)
        }
    }

    private fun startDataCollectorService() {
        val serviceIntent = Intent(this, DataCollectorService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Request permissions based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissions = mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
            
            locationPermissionRequest.launch(permissions.toTypedArray())
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        setContent {
            FirebaseTrackerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrackerScreen(
                        onStartTracking = { interval ->
                            startService(Intent(this, DataCollectorService::class.java).apply {
                                putExtra("interval", interval * 1000L) // Convert to milliseconds
                            })
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(onStartTracking: (Int) -> Unit) {
    var interval by remember { mutableStateOf("60") }
    var isTracking by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Data Collection Adventure!",
            style = MaterialTheme.typography.headlineMedium,
            color = BrightBlue
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrightYellow.copy(alpha = 0.1f))
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = interval,
                onValueChange = { interval = it },
                label = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AnimatedCounter(interval.toIntOrNull() ?: 60)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = BrightBlue,
                    unfocusedBorderColor = BrightBlue.copy(alpha = 0.5f)
                )
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                isTracking = !isTracking
                if (isTracking) {
                    onStartTracking(interval.toIntOrNull() ?: 60)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isTracking) BrightPink else BrightGreen
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                if (isTracking) "Stop the Adventure!" else "Start Collecting!",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = isTracking,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CollectingAnimation(isTracking)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Collecting data...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = BrightPurple
                )
            }
        }
    }
}