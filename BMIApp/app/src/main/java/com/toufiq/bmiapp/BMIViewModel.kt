package com.toufiq.bmiapp

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BMIViewModel : ViewModel() {
    var weight by mutableStateOf("")
        private set
    var height by mutableStateOf("")
        private set

    private val _bmiUIState = MutableStateFlow(BMIStateViewState())
    val bmiUIState = _bmiUIState.asStateFlow()


    fun updateWeight(it: String) {
        weight = it
    }

    fun updateHeight(it: String) {
        height = it
    }

    private fun getBmiStatus(doubleBmi: Double): String {
        return when {
            doubleBmi < 18.5 -> {
                "Underweight"
            }

            doubleBmi in 18.5..24.9 -> {
                "Normal weight"
            }

            doubleBmi in 25.0..29.9 -> {
                "Overweight"
            }

            doubleBmi >= 30.0 -> {
                "Obese"
            }

            else -> {
                "Invalid BMI"
            }
        }
    }

    private fun isValid(): Boolean {
        return weight.toDoubleOrNull() != null && height.toDoubleOrNull() != null
    }

    @SuppressLint("DefaultLocale")
    fun calculateBMI() {
        if (isValid()) {
            val bmi = (weight.toDouble()) / (height.toDouble()) * (height.toDouble())
            val bmiString = String.format("%.1f", bmi)
            val status = getBmiStatus(bmi)
            _bmiUIState.update {
                it.copy(bmi = bmiString, bmiStatus = status)
            }
        }


    }

}

data class BMIStateViewState(
    val bmi: String = "0",
    val bmiStatus: String = ""
)