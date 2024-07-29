package com.toufiq.bmiapp

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BMIViewModel : ViewModel() {
    var weight = mutableStateOf("")
        private set
    var height = mutableStateOf("")
        private set
    var bmi = mutableStateOf("0.0")
        private set
    var bmiResult = mutableStateOf("")
        private set

    fun getBmiStatus(doubleBmi: Double): String {
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
                "Obesity"
            }

            else -> {
                "Invalid BMI"
            }
        }
    }

    @SuppressLint("DefaultLocale")
    fun calculateBMI(weight: Double, height: Double): String {
        return String.format("%.1f", (weight / (height * height)))
    }

}