package com.toufiq.navigationexample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.toufiq.navigationexample.ui.theme.NavigationExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun MyApp() {
    NavigationExampleTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            NavigationExample()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NavigationExampleTheme {
        MyApp()
    }
}