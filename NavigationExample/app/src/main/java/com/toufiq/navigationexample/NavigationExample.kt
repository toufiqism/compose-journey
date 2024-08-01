package com.toufiq.navigationexample

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.toufiq.navigationexample.screens.ScreenOne
import com.toufiq.navigationexample.screens.ScreenThree
import com.toufiq.navigationexample.screens.ScreenTwo

@Composable
fun NavigationExample(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "screen_one"
    ) {
        composable(route = "screen_one") { ScreenOne { navController.navigate("screen_two") } }
        composable(route = "screen_two") { ScreenTwo { navController.navigate("screen_three") } }
        composable(route = "screen_three") { ScreenThree { navController.popBackStack(route = "screen_one", inclusive = false) } }
    }
}