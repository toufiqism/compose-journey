package com.toufiq.pokedexapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.toufiq.pokedexapp.ui.screens.PokemonListScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.POKEMON_LIST_SCREEN) {

        composable(Routes.POKEMON_LIST_SCREEN) {
            PokemonListScreen(navController = navController)
        }
        composable(route = Routes.POKEMON_DETAILS_SCREEN + "/${Routes.DOMINANT_COLOR}" + "/${Routes.POKEMON_NAME}",
            arguments = listOf(
                navArgument(Routes.DOMINANT_COLOR) { type = NavType.IntType },
                navArgument(Routes.POKEMON_NAME) { type = NavType.StringType }
            )) {
            val dominantColor = remember {
                it.arguments?.getInt(Routes.DOMINANT_COLOR) ?: Color.White
            }
            val pokemonName= remember {
                it.arguments?.getString(Routes.POKEMON_NAME)
            }

        }
    }


}