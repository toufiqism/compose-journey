package com.toufiq.navigationexample.moviexample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toufiq.navigationexample.R
import com.toufiq.navigationexample.screens.components.MyAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    movies: List<Movie>,
    onNavigate: () -> Unit, onNavigateUp: () -> Unit = {}
) {
    Scaffold(topBar = {
        MyAppBar(title = "Homepage", canNavBack = false)
    }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(movies) { movie ->
                Text(text = movie.name)
            }
        }
    }
}




@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewScreenHomePage() {
    HomePage(listOf(
        Movie(
            id = 3,
            name = "Captain Marvel",
            category = "Superhero",
            image = R.drawable.captainmarvel
        )
    ), {})
}