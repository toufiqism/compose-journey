package com.toufiq.aqiteller.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object NavigationAnimations {
    private const val ANIMATION_DURATION = 300

    fun enterTransition(): EnterTransition {
        return slideInHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            initialOffsetX = { fullWidth -> fullWidth }
        ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
    }

    fun exitTransition(): ExitTransition {
        return slideOutHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            targetOffsetX = { fullWidth -> -fullWidth }
        ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
    }

    fun popEnterTransition(): EnterTransition {
        return slideInHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            initialOffsetX = { fullWidth -> -fullWidth }
        ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
    }

    fun popExitTransition(): ExitTransition {
        return slideOutHorizontally(
            animationSpec = tween(ANIMATION_DURATION),
            targetOffsetX = { fullWidth -> fullWidth }
        ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
    }
} 