package com.sol.smsredirectorai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.webkit.URLUtil
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.sol.smsredirectorai.data.AppPreferences
import com.sol.smsredirectorai.data.SimInfo
import com.sol.smsredirectorai.ui.theme.SMSRedirectorAITheme
import kotlinx.coroutines.launch
import com.sol.smsredirectorai.data.AppDatabase
import com.sol.smsredirectorai.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Toast
import android.provider.Telephony
import android.content.ContentResolver
import android.database.Cursor
import androidx.compose.foundation.background
import com.sol.smsredirectorai.data.SmsData
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.asPaddingValues
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import android.net.Uri
import android.provider.Settings

class MainActivity : ComponentActivity() {
    private lateinit var appPreferences: AppPreferences

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            // Start the service when permissions are granted
            startSmsService()
        }
    }

    companion object {
        private const val OVERLAY_PERMISSION_REQ_CODE = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appPreferences = AppPreferences(this)

        // Request permissions
        requestPermissions()

        setContent {
            SMSRedirectorAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(appPreferences)
                }
            }
        }
    }

    private fun requestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_BOOT_COMPLETED
        )

        // Add notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Check and request system alert window permission
        if (!Settings.canDrawOverlays(this)) {
            // If the app doesn't have permission, request it
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
        }

        // Check and request other permissions
        if (permissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            // Permissions are already granted
            startSmsService()
            return
        }

        requestPermissionLauncher.launch(permissions.toTypedArray())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Settings.canDrawOverlays(this)) {
                // Permission granted, start service
                startSmsService()
            } else {
                // Permission denied
                Toast.makeText(
                    this,
                    "System alert window permission is required",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startSmsService() {
        val serviceIntent = Intent(this, SMSService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(appPreferences: AppPreferences) {
    val scope = rememberCoroutineScope()
    var showSim1Dialog by remember { mutableStateOf(false) }
    var showSim2Dialog by remember { mutableStateOf(false) }
    var urlError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS).addInterceptor(logging).build()

    val apiService = remember {
        Retrofit.Builder()
            .baseUrl("https://placeholder.com") // This base URL won't be used
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    val sim1Info = appPreferences.sim1Info.collectAsStateWithLifecycle(initialValue = SimInfo("", ""))
    val sim2Info = appPreferences.sim2Info.collectAsStateWithLifecycle(initialValue = SimInfo("", ""))
    var apiUrl by remember { mutableStateOf("") }
    var isUrlEditable by remember { mutableStateOf(false) }

    // Collect API URL separately
    LaunchedEffect(Unit) {
        appPreferences.apiUrl.collect { url ->
            apiUrl = url
        }
    }

    // Get status bar height
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(statusBarPadding)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 2.dp
        ) {
            Text(
                text = "SMS Redirector AI",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(24.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        // Content
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // SIM Cards Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "SIM Configuration",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    
                    // SIM 1 Button
                    ElevatedButton(
                        onClick = { showSim1Dialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(if (sim1Info.value.number.isEmpty()) "Configure SIM 1" 
                            else "SIM 1: ${sim1Info.value.name}")
                    }

                    // SIM 2 Button
                    ElevatedButton(
                        onClick = { showSim2Dialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(if (sim2Info.value.number.isEmpty()) "Configure SIM 2" 
                            else "SIM 2: ${sim2Info.value.name}")
                    }
                }
            }

            // URL Configuration Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "API Configuration",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = apiUrl,
                            onValueChange = { newUrl ->
                                if (isUrlEditable) {
                                    apiUrl = newUrl
                                    urlError = validateUrl(newUrl)
                                }
                            },
                            label = { Text("API URL") },
                            isError = urlError != null,
                            supportingText = urlError?.let { { Text(it) } },
                            modifier = Modifier.weight(1f),
                            enabled = isUrlEditable,
                            readOnly = !isUrlEditable,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = MaterialTheme.colorScheme.surface,
//                                textColor = MaterialTheme.colorScheme.onSurface,
//                                placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                focusedLabelColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        
                        Column {
                            IconButton(
                                onClick = { isUrlEditable = !isUrlEditable }
                            ) {
                                Icon(
                                    imageVector = if (isUrlEditable) 
                                        Icons.Default.Lock 
                                    else 
                                        Icons.Default.Edit,
                                    contentDescription = if (isUrlEditable) "Lock URL" else "Edit URL",
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                            
                            FilledTonalButton(
                                onClick = {
                                    if (urlError == null) {
                                        scope.launch {
                                            appPreferences.saveApiUrl(apiUrl)
                                            Toast.makeText(context, "URL saved", Toast.LENGTH_SHORT).show()
                                            isUrlEditable = false
                                        }
                                    }
                                },
                                enabled = isUrlEditable && urlError == null && apiUrl.isNotEmpty(),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            }

            // Action Section
            FilledTonalButton(
                onClick = {
                    if (apiUrl.isEmpty()) {
                        Toast.makeText(context, "Please configure API URL first", Toast.LENGTH_SHORT).show()
                        return@FilledTonalButton
                    }
                    
                    scope.launch {
                        try {
                            // First try to get SMS from database
                            var lastSms = database.smsDao().getLastSms()
                            
                            // If not found in database, try content provider
                            if (lastSms == null) {
                                lastSms = getLastSmsFromContentProvider(context.contentResolver)?.let { sms ->
                                    // Create SmsData from content provider SMS
                                    SmsData.create(
                                        sender = sms.first,
                                        receiver = sim1Info.value.number, // Use configured SIM as receiver
                                        body = sms.second
                                    )
                                }
                            }

                            if (lastSms != null) {
                                val response = apiService.sendSms(apiUrl, lastSms)
                                if (response.isSuccessful) {
                                    // Only delete from database if it was from there
                                    if (lastSms.id != 0L) {
                                        database.smsDao().delete(lastSms)
                                    }
                                    Toast.makeText(context, "SMS sent successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Failed to send SMS: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "No SMS found", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text("Send Last SMS")
            }
        }
    }

    // SIM 1 Dialog
    if (showSim1Dialog) {
        SimConfigDialog(
            initialSimInfo = sim1Info.value,
            onDismiss = { showSim1Dialog = false },
            onSave = { simInfo ->
                scope.launch {
                    appPreferences.saveSim1Info(simInfo)
                    showSim1Dialog = false
                }
            }
        )
    }

    // SIM 2 Dialog
    if (showSim2Dialog) {
        SimConfigDialog(
            initialSimInfo = sim2Info.value,
            onDismiss = { showSim2Dialog = false },
            onSave = { simInfo ->
                scope.launch {
                    appPreferences.saveSim2Info(simInfo)
                    showSim2Dialog = false
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimConfigDialog(
    initialSimInfo: SimInfo,
    onDismiss: () -> Unit,
    onSave: (SimInfo) -> Unit
) {
    var number by remember { mutableStateOf(initialSimInfo.number) }
    var name by remember { mutableStateOf(initialSimInfo.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Configure SIM") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Phone Number") }
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name/Description") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(SimInfo(number, name))
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun validateUrl(url: String): String? {
    if (url.isEmpty()) return null
    if (!url.startsWith("http://") && !url.startsWith("https://")) {
        return "URL must start with http:// or https://"
    }
    if (!URLUtil.isValidUrl(url)) {
        return "Invalid URL format"
    }
    return null
}

private fun getLastSmsFromContentProvider(contentResolver: ContentResolver): Pair<String, String>? {
    val projection = arrayOf(
        Telephony.Sms.ADDRESS,
        Telephony.Sms.BODY
    )
    
    return contentResolver.query(
        Telephony.Sms.CONTENT_URI,
        projection,
        null,
        null,
        "${Telephony.Sms.DATE} DESC LIMIT 1"
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
            val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
            Pair(address, body)
        } else null
    }
}
