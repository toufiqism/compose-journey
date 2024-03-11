package com.toufiq.composewithmvvm.ui.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.toufiq.composewithmvvm.ui.screens.CategoryScreen
import com.toufiq.composewithmvvm.ui.screens.DetailsScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.ROUTES_CATEGORY) {
        composable(Routes.ROUTES_CATEGORY) {
            CategoryScreen() {
                navController.navigate(Routes.ROUTES_DETAIL + "/${it}")
            }
        }
        composable(route = "${Routes.ROUTES_DETAIL}/{category}", arguments = listOf(
            navArgument(Routes.ROUTES_CATEGORY) { type = NavType.StringType }
        )) {
            DetailsScreen()
        }
    }
}