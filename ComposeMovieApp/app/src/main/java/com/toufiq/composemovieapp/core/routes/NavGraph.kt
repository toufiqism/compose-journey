package com.toufiq.composemovieapp.core.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.toufiq.composemovieapp.core.presentation.HomeScreen
import com.toufiq.composemovieapp.details.presantation.DetailsScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Home.rout) {
        composable(Routes.Home.rout) {
            HomeScreen(navController)
        }
        composable(
            Routes.Details.rout + "/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            DetailsScreen(navBackStackEntry = it)
        }
    }
}