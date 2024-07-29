package com.toufiq.bmiapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun BMIScreen(modifier: Modifier = Modifier, vm: BMIViewModel = viewModel()) {

    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeightHeightTextField(
            label = "Enter Weight (in Kg)",
            value = vm.weight.value,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            onValueChanged = { vm.weight.value = it })
        WeightHeightTextField(
            label = "Enter Height (in Meter)",
            value = vm.height.value,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            onValueChanged = { vm.height.value = it },
        )
        Spacer(modifier = Modifier.size(15.dp))
        CalculateBMIButton(
            onClick = {
                vm.bmi.value = vm.calculateBMI(
                    vm.weight.value.toDoubleOrNull() ?: 0.0,
                    vm.height.value.toDoubleOrNull() ?: 0.0
                )
                val doubleBmi = vm.bmi.value.toDouble()
                vm.bmiResult.value = vm.getBmiStatus(doubleBmi)
            },
            label = "Calculate BMI",
        )
        BMIResultView(result = vm.bmi.value)
        Spacer(modifier = Modifier.size(15.dp))
        if (vm.bmiResult.value.trim().isNotBlank())
            BMIStatusView(status = vm.bmiResult.value)
    }
}

@Composable
fun CalculateBMIButton(onClick: () -> Unit, label: String) {
    Button(onClick = onClick) {
        Text(text = label)
    }
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

@Composable
fun BMIStatusView(
    status: String, modifier: Modifier = Modifier
) {
    Text(
        text = "You are : $status",
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