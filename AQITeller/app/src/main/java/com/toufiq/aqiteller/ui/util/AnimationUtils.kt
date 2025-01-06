package com.toufiq.aqiteller.ui.util

import androidx.compose.animation.*
import androidx.compose.animation.core.*

object AnimationUtils {
    const val ANIMATION_DURATION = 500
    const val FADE_DURATION = 300
    
    val enterTransition = fadeIn(
        animationSpec = tween(FADE_DURATION)
    ) + expandVertically(
        animationSpec = tween(ANIMATION_DURATION)
    )
    
    val exitTransition = fadeOut(
        animationSpec = tween(FADE_DURATION)
    ) + shrinkVertically(
        animationSpec = tween(ANIMATION_DURATION)
    )
} 