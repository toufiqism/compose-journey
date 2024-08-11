package com.toufiq.navigationexample.moviexample

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MovieApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home_page") {
        composable(route = "home_page") {
            HomePage(
                listOf(),
                onNavigate = {
                    navController.navigate("")
                })
        }
        composable(route = "details_page") {
            DetailsPage(onNavigate = {
                navController.navigate("")
            })
        }
    }
}