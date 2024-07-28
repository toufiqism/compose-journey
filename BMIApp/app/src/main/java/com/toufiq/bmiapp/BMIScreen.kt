package com.toufiq.bmiapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BMIScreen(modifier: Modifier = Modifier) {

    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("0.0") }

    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeightHeightTextField(
            label = "Enter Weight (in Kg)",
            value = weight,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChanged = { weight = it })
        WeightHeightTextField(
            label = "Enter Height (in Meter)",
            value = height,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            onValueChanged = { height = it },
        )

        Button(onClick = {
            bmi = calculateBMI(weight.toDoubleOrNull() ?: 0.0, height.toDoubleOrNull() ?: 0.0)
        }) {
            Text(text = "Calculate BMI")
        }

        BMIResultView(result = bmi)
    }
}

@SuppressLint("DefaultLocale")
fun calculateBMI(weight: Double, height: Double): String {
    return String.format("%.1f", (weight / (height * height)))
}

@Composable
fun WeightHeightTextField(
    label: String,
    keyboardOptions: KeyboardOptions,
    value: String,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
) {
    TextField(
        keyboardOptions = keyboardOptions,
        modifier = modifier.padding(8.dp),
        label = { Text(text = label) },
        value = value,
        onValueChange = onValueChanged,
    )
}

@Composable
fun BMIResultView(
    result: String, modifier: Modifier = Modifier
) {
    Text(
        text = "Your BMI Is : $result",
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        modifier = modifier
    )

}

@Preview(showBackground = true)
@Composable
fun PreviewBMIScreen() {
    BMIScreen()
}