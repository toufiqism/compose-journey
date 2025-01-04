package com.toufiq.firebasetrackerapp.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.toufiq.firebasetrackerapp.R
import com.toufiq.firebasetrackerapp.ui.theme.*

@Composable
fun PulsingDot(color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulsing")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(12.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun CollectingAnimation(isActive: Boolean) {
    val colors = listOf(BrightBlue, BrightGreen, BrightOrange, BrightPink, BrightPurple)
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isActive) {
            colors.forEachIndexed { index, color ->
                key(index) {
                    PulsingDot(
                        color = color
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedCounter(seconds: Int) {
    val rotation by rememberInfiniteTransition(label = "rotation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        ),
        label = "rotate"
    )

    Icon(
        painter = painterResource(id = R.drawable.ic_timer),
        contentDescription = "Timer",
        modifier = Modifier
            .size(24.dp)
            .graphicsLayer { rotationZ = rotation },
        tint = BrightOrange
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text("$seconds seconds")
} 