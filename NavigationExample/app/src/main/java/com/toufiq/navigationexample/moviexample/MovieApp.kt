package com.toufiq.navigationexample.moviexample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MovieApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val vm: MovieViewModel = viewModel()
    val uiState by vm.homeUiState.collectAsState()
    NavHost(navController = navController, startDestination = "home_page") {
        composable(route = "home_page") {
            HomePage(
                movies = uiState.movies,
                onNavigate = {
                    navController.navigate("details_page/${it}")
                })
        }
        composable(
            route = "details_page/{id}",
            arguments =
            listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )) {
            val id = it.arguments?.getLong("id")
            DetailsPage(id = id!!) {
                navController.navigateUp()
            }
        }
    }
}