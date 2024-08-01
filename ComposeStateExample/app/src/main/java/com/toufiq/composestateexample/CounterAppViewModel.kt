package com.toufiq.composestateexample

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CounterAppViewModel : ViewModel() {

    private var count by mutableStateOf(0)

    private val _counterUIState = MutableStateFlow(CounterUIState())
    val counterUIState = _counterUIState.asStateFlow()


    fun increase() {
        count++
        _counterUIState.update {
            it.copy(counter = count)
        }
    }
}

data class CounterUIState(val counter: Int = 0)