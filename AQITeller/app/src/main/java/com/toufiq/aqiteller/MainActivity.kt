package com.toufiq.aqiteller

import AqiData
import AqiRepository
import AqiResponse
import AqiUiState
import AqiViewModel
import com.toufiq.aqiteller.util.LocationHelper
import com.toufiq.aqiteller.di.NetworkModule
import com.toufiq.aqiteller.util.NotificationHelper
import PollutantDetails
import com.toufiq.aqiteller.data.repository.SettingsRepository
import SettingsScreen
import com.toufiq.aqiteller.ui.viewmodel.SettingsViewModel
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.toufiq.aqiteller.ui.theme.AQITellerTheme
import com.toufiq.aqiteller.ui.theme.aqi_good
import com.toufiq.aqiteller.ui.theme.aqi_hazardous
import com.toufiq.aqiteller.ui.theme.aqi_moderate
import com.toufiq.aqiteller.ui.theme.aqi_unhealthy
import com.toufiq.aqiteller.ui.theme.aqi_unhealthy_sensitive
import com.toufiq.aqiteller.ui.theme.aqi_very_unhealthy
import com.toufiq.aqiteller.util.NavigationAnimations

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
        
        setContent {
            AQITellerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing)
                    ) {
                        MainScreen()
                    }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "aqi_channel",
                "AQI Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Air Quality Index Updates"
            }
            
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = { NavigationAnimations.enterTransition() },
        exitTransition = { NavigationAnimations.exitTransition() },
        popEnterTransition = { NavigationAnimations.popEnterTransition() },
        popExitTransition = { NavigationAnimations.popExitTransition() }
    ) {
        composable("home") {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "AQI Teller",
                                modifier = Modifier.animateContentSize()
                            )
                        },
                        actions = {
                            IconButton(
                                onClick = { navController.navigate("settings") },
                                modifier = Modifier.scale((1f))
                            ) {
                                Icon(Icons.Default.Settings, "Settings")
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    AqiScreen(context)
                }
            }
        }
        composable("settings") {
            val settingsViewModel = viewModel<SettingsViewModel>(
                factory = SettingsViewModel.Factory
            )
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun AqiScreen(
    context: Context,
    viewModel: AqiViewModel = viewModel { 
        AqiViewModel(
            repository = AqiRepository(NetworkModule.aqiApiService),
            locationHelper = LocationHelper(context),
            settingsRepository = SettingsRepository(context)
        ) 
    }
) {
    val aqiState by viewModel.aqiState.collectAsState()
    
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions.entries.all { it.value }
        if (locationGranted) {
            // Location permissions granted, ViewModel will handle the updates
        }
    }
    
    DisposableEffect(Unit) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        
        if (permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }) {
            // Permissions already granted, ViewModel will handle updates
        } else {
            permissionLauncher.launch(permissions)
        }
        
        onDispose { }
    }

    LaunchedEffect(aqiState) {
        if (aqiState is AqiUiState.Success) {
            NotificationHelper.showAqiNotification(
                context,
                (aqiState as AqiUiState.Success).data
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        AnimatedContent(
            targetState = aqiState,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) { state ->
            when (state) {
                is AqiUiState.Loading -> {
                    LoadingIndicator()
                }
                is AqiUiState.Success -> {
                    AqiContent(state.data)
                }
                is AqiUiState.Error -> {
                    ErrorContent(state.message)
                }
                else -> {
                    // Initial state
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ErrorContent(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
fun AqiContent(data: AqiResponse) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            AqiCard(data.overall_aqi)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Pollutant Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
            ) {
                data.CO?.let { AqiDetailItem("CO", it) }
                data.PM10?.let { AqiDetailItem("PM10", it) }
                data.SO2?.let { AqiDetailItem("SO2", it) }
                data.PM2_5?.let { AqiDetailItem("PM2.5", it) }
                data.O3?.let { AqiDetailItem("O3", it) }
                data.NO2?.let { AqiDetailItem("NO2", it) }
            }
        }
    }
}

@Composable
fun AqiCard(aqi: Int) {
    val aqiColor = when {
        aqi <= 50 -> aqi_good
        aqi <= 100 -> aqi_moderate
        aqi <= 150 -> aqi_unhealthy_sensitive
        aqi <= 200 -> aqi_unhealthy
        aqi <= 300 -> aqi_very_unhealthy
        else -> aqi_hazardous
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = aqiColor.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current Air Quality",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = aqi.toString(),
                style = MaterialTheme.typography.displayLarge,
                color = aqiColor
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = getAqiAdvice(aqi),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AqiDetailItem(name: String, data: AqiData) {
    var expanded by remember { mutableStateOf(false) }
    val pollutantInfo = when (name) {
        "CO" -> PollutantDetails.CO
        "NO2" -> PollutantDetails.NO2
        "O3" -> PollutantDetails.O3
        "SO2" -> PollutantDetails.SO2
        "PM2.5" -> PollutantDetails.PM25
        "PM10" -> PollutantDetails.PM10
        else -> null
    }
    
    val elevation by animateDpAsState(
        targetValue = if (expanded) 8.dp else 1.dp,
        label = "Card Elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = pollutantInfo?.fullName ?: name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "AQI: ${data.aqi} (${String.format("%.2f", data.concentration)})",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Show less" else "Show more"
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    pollutantInfo?.let { info ->
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = info.description,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Health Effects",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = info.healthEffects,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Common Sources",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = info.sources,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

fun getAqiAdvice(aqi: Int): String {
    return when {
        aqi <= 50 -> "Air quality is good. Perfect for outdoor activities!"
        aqi <= 100 -> "Air quality is moderate. Sensitive individuals should limit prolonged outdoor exposure."
        aqi <= 150 -> "Air quality is unhealthy for sensitive groups. Limit outdoor activities."
        aqi <= 200 -> "Air quality is unhealthy. Everyone should reduce outdoor activities."
        aqi <= 300 -> "Air quality is very unhealthy. Avoid outdoor activities."
        else -> "Air quality is hazardous. Stay indoors!"
    }
}