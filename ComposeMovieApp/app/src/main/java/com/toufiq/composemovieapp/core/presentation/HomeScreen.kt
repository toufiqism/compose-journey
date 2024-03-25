package com.toufiq.composemovieapp.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.toufiq.composemovieapp.R
import com.toufiq.composemovieapp.core.routes.Routes
import com.toufiq.composemovieapp.movielist.presentation.MovieListUIEvent
import com.toufiq.composemovieapp.movielist.presentation.MovieListViewModel
import com.toufiq.composemovieapp.movielist.presentation.PopularMovieScreen
import com.toufiq.composemovieapp.movielist.presentation.UpcomingMovieScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val movieListViewModel: MovieListViewModel = hiltViewModel()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()

    Scaffold(bottomBar = {
        BottomNavigationBar(
            bottomNavController = bottomNavController,
            onEvent = movieListViewModel::onEvent
        )


    }, topBar = {
        TopAppBar(
            title = {
                Text(
                    text = if (movieListState.isCurrentPopularScreen) stringResource(R.string.popular) else stringResource(
                        R.string.upcoming
                    ), fontSize = 28.sp
                )
            },
            modifier = Modifier.shadow(4.dp),
            colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.inverseOnSurface)
        )
    }, content = { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()){
            NavHost(navController = bottomNavController, startDestination = Routes.PopularMovieList.rout ){
                composable(Routes.PopularMovieList.rout){
                    PopularMovieScreen(movieListState = movieListState, navController = navController, onEvent = movieListViewModel::onEvent)
                        

                }
                composable(Routes.UpcomingMovieList.rout){
                    UpcomingMovieScreen(movieListState = movieListState, navController = navController, onEvent = movieListViewModel::onEvent)
                }
            }
        }
    })
}

@Composable
fun BottomNavigationBar(bottomNavController: NavController, onEvent: (MovieListUIEvent) -> Unit) {

    val items = listOf(
        BottomItem(title = stringResource(R.string.popular), icon = Icons.Rounded.Movie),
        BottomItem(title = stringResource(R.string.upcoming), icon = Icons.Rounded.Upcoming)
    )
    val selected = rememberSaveable() {
        mutableIntStateOf(0)
    }
    NavigationBar {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(
                    selected = selected.intValue == index,
                    onClick = {
                        selected.intValue = index
                        when (selected.intValue) {
                            0 -> {
                                onEvent(MovieListUIEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Routes.PopularMovieList.rout)
                            }

                            1 -> {
                                onEvent(MovieListUIEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Routes.UpcomingMovieList.rout)
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = bottomItem.icon,
                            contentDescription = bottomItem.title
                        )
                    },
                    label = {
                        Text(
                            text = bottomItem.title,
                            color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    })
            }

        }
    }

}


data class BottomItem(
    val title: String,
    val icon: ImageVector
)