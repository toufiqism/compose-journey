import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.draw.scale
import com.toufiq.aqiteller.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Settings",
                        modifier = Modifier.animateContentSize()
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.scale(1f)
                    ) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Notifications Master Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Enable Notifications",
                    style = MaterialTheme.typography.titleMedium
                )
                Switch(
                    checked = settings.notificationsEnabled,
                    onCheckedChange = { viewModel.updateNotificationsEnabled(it) }
                )
            }

            AnimatedVisibility(
                visible = settings.notificationsEnabled,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Notification Interval
                    Column {
                        Text(
                            text = "Update Interval",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Slider(
                            value = settings.notificationInterval.toFloat(),
                            onValueChange = { viewModel.updateNotificationInterval(it.toInt()) },
                            valueRange = 15f..120f,
                            steps = 7
                        )
                        Text(
                            text = "${settings.notificationInterval} minutes",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Divider()

                    // High AQI Notifications
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Notify on High AQI",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Switch(
                                checked = settings.notifyOnHighAqi,
                                onCheckedChange = { viewModel.updateNotifyOnHighAqi(it) }
                            )
                        }

                        AnimatedVisibility(
                            visible = settings.notifyOnHighAqi,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text(
                                    text = "High AQI Threshold",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Slider(
                                    value = settings.highAqiThreshold.toFloat(),
                                    onValueChange = { viewModel.updateHighAqiThreshold(it.toInt()) },
                                    valueRange = 50f..200f,
                                    steps = 15
                                )
                                Text(
                                    text = "AQI > ${settings.highAqiThreshold}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 