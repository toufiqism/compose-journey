package com.toufiq.composewithmvvm.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toufiq.composewithmvvm.models.TweetListItem
import com.toufiq.composewithmvvm.viewmodels.TweetsViewModel
import kotlin.random.Random

@Composable
fun DetailsScreen() {
    val detailsViewModel: TweetsViewModel = hiltViewModel()
    val tweets: State<List<TweetListItem>> = detailsViewModel.tweets.collectAsState()

    LazyColumn {
        items(tweets.value) {
            TweetListItem(it.text!!)
        }
    }
}

@Composable
fun TweetListItem(tweet: String) {
    val ctx = LocalContext.current
    Card(
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .clickable {
                toast(ctx)
            },
        border = BorderStroke(1.dp, generateRandomColors())
    ) {
        Text(
            text = tweet,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

fun generateRandomColors(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f
    )
}

fun toast(context: Context) {
    Toast.makeText(context, "This is a Sample Toast", Toast.LENGTH_LONG).show()
}