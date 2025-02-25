package com.toufiq.navigationexample.moviexample

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toufiq.navigationexample.screens.components.MyAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsPage(
    id: Long,
    onNavigateUp: () -> Unit = {}
) {
    val vm: MovieViewModel = viewModel()
    val uiState by vm.getMovieById(id).collectAsState()
    Scaffold(topBar = {
        MyAppBar(title = "Details Page", canNavBack = true, onNavigateUp = onNavigateUp)
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = uiState.movie.image),
                contentDescription = uiState.movie.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(400.dp)
            )
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewScreenDetailsPage() {
    DetailsPage(id = 1, {})
}