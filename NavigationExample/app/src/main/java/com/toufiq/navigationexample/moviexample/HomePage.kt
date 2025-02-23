package com.toufiq.navigationexample.moviexample

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toufiq.navigationexample.R
import com.toufiq.navigationexample.screens.components.MyAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    movies: List<Movie>,
    onNavigate: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            MyAppBar(title = "Homepage", canNavBack = false)
        }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(movies) { movie ->
                MovieItem(movie = movie, onMovieItemClick = {
                    onNavigate(movie.id)
                })
            }
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onMovieItemClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .padding(8.dp)
        .clickable {
            onMovieItemClick(movie)
        }) {
        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = movie.image),
                contentDescription = movie.name,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(100.dp)
            )
            Column(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = movie.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = movie.category,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewScreenHomePage() {
    HomePage(DataSource().generateMovieList(), {})
}