package com.toufiq.navigationexample.moviexample

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toufiq.navigationexample.screens.components.MyAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsPage(onNavigate: () -> Unit,onNavigateUp:()->Unit={}) {
    Scaffold(topBar = {
        MyAppBar(title = "Details Page", canNavBack = true)
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Details Page", fontSize = 25.sp)
            Spacer(modifier = Modifier.size(20.dp))
            Button(onClick = onNavigate) {
                Text(text = "Go to Screen Two")
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewScreenDetailsPage() {
    DetailsPage({})
}