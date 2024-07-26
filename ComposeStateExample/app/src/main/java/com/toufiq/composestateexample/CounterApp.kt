package com.toufiq.composestateexample

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun CounterApp(modifier: Modifier = Modifier) {
    val count = remember {
        mutableIntStateOf(0)
    }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "${count.intValue}", fontSize = 80.sp)
        Button(onClick = {
            count.intValue++
            Log.d("TAG", "CounterApp: ${count.intValue}")
        }) {
            Text(text = "Click ME!")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CounterAppPreview() {
    CounterApp()
}