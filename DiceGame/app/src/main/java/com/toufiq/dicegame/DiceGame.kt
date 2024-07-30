package com.toufiq.dicegame

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun DiceGameScreen(modifier: Modifier = Modifier, vm: DiceGameViewModel = viewModel()) {
    val diceGameUIState by vm.diceGameUIState.collectAsState()
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        DiceRow(
            imageResource1 = diceGameUIState.resultLeft,
            imageResource2 = diceGameUIState.resultRight,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(20.dp))
        DiceSumTextView("Result: ${diceGameUIState.diceSum}")
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = diceGameUIState.status, fontSize = 18.sp)
        if (diceGameUIState.target > 0) {
            Text(text = "Your target is: ${diceGameUIState.target}", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(onClick = {
            if (diceGameUIState.isGameOver) {
                vm.resetGame()
            } else {
                vm.rollDice()
            }
        }) {
            Text(text = diceGameUIState.btnText)
        }
    }
}

@Composable
private fun DiceSumTextView(text: String, modifier: Modifier = Modifier) {
    Text(text = text, fontSize = 18.sp, modifier = modifier)
}

@Composable
fun DiceImage(
    @DrawableRes imageResource: Int,
    modifier: Modifier = Modifier,
    vm: DiceGameViewModel = viewModel()
) {
    Image(
        painter = painterResource(id = vm.getDiceImageResource(imageResource)),
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
fun DiceRow(
    @DrawableRes imageResource1: Int,
    @DrawableRes imageResource2: Int,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.Center, modifier = modifier) {
        DiceImage(imageResource = imageResource1, modifier = Modifier.size(200.dp))
        DiceImage(imageResource = imageResource2, modifier = Modifier.size(200.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DiceGamePreview() {
    DiceGameScreen()
}