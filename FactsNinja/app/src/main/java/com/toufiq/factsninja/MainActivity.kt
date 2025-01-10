package com.toufiq.factsninja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toufiq.factsninja.ui.screens.FactsScreen
import com.toufiq.factsninja.ui.theme.FactsNinjaTheme
import com.toufiq.factsninja.ui.viewmodel.FactsViewModel
import com.toufiq.factsninja.ui.viewmodel.FactsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            FactsNinjaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: FactsViewModel = viewModel(
                        factory = FactsViewModelFactory(applicationContext)
                    )
                    val uiState by viewModel.uiState.collectAsState()

                    FactsScreen(
                        uiState = uiState,
                        onRefresh = viewModel::fetchFact
                    )
                }
            }
        }
    }
}