package com.sol.smsredirectorai.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),          // Light purple
    secondary = Color(0xFF9ECAFF),        // Light blue
    tertiary = Color(0xFFFFB1C8),         // Light pink
    background = Color(0xFF1A1C1E),       // Dark background
    surface = Color(0xFF1A1C1E),          // Dark surface
    primaryContainer = Color(0xFF4F378B),  // Deep purple
    secondaryContainer = Color(0xFF004968),// Deep blue
    tertiaryContainer = Color(0xFF8B2864),// Deep pink
    onPrimary = Color(0xFF381E72),        // Dark purple for text on primary
    onSecondary = Color(0xFF003355),      // Dark blue for text on secondary
    onTertiary = Color(0xFF4E2532),       // Dark pink for text on tertiary
    onBackground = Color(0xFFE3E2E6),     // Light gray for text on background
    onSurface = Color(0xFFE3E2E6),        // Light gray for text on surface
    onPrimaryContainer = Color(0xFFEADDFF),// Light purple for text on primary container
    onSecondaryContainer = Color(0xFFCCE5FF),// Light blue for text on secondary container
    onTertiaryContainer = Color(0xFFFFD8E9) // Light pink for text on tertiary container
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),          // Purple
    secondary = Color(0xFF006C93),        // Blue
    tertiary = Color(0xFFB23F7C),         // Pink
    background = Color(0xFFFFFBFE),       // White background
    surface = Color(0xFFFFFBFE),          // White surface
    primaryContainer = Color(0xFFEADDFF),  // Light purple
    secondaryContainer = Color(0xFFCCE5FF),// Light blue
    tertiaryContainer = Color(0xFFFFD8E9), // Light pink
    onPrimary = Color.White,              // White text on primary
    onSecondary = Color.White,            // White text on secondary
    onTertiary = Color.White,             // White text on tertiary
    onBackground = Color(0xFF1C1B1F),     // Dark text on background
    onSurface = Color(0xFF1C1B1F),        // Dark text on surface
    onPrimaryContainer = Color(0xFF21005D),// Dark purple text on primary container
    onSecondaryContainer = Color(0xFF001E30),// Dark blue text on secondary container
    onTertiaryContainer = Color(0xFF31111D) // Dark pink text on tertiary container
)

@Composable
fun SMSRedirectorAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}