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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun DiceGame(modifier: Modifier = Modifier) {

    var resultLeft by remember { mutableStateOf(1) }
    var resultRight by remember { mutableStateOf(1) }
    var diceSum by remember { mutableStateOf(0) }
    var status by remember { mutableStateOf("") }
    var target by remember { mutableStateOf(0) }
    var btnText by remember { mutableStateOf("Roll") }
    var isGameOver by remember { mutableStateOf(false) }

    fun reset() {
        btnText = "Roll"
        resultLeft = 1
        resultRight = 1
        diceSum = 0
        status = ""
        isGameOver = false
        target = 0
    }

    fun roll() {
        resultLeft = (1..6).random()
        resultRight = (1..6).random()
        diceSum = resultLeft + resultRight
        if (target > 0) {
            if (diceSum == target) {
                status = "You Win"
                isGameOver = true
            } else if (diceSum == 7) {
                status = "You Lose"
                isGameOver = true
            }
        } else if (diceSum == 7 || diceSum == 11) {
            status = "You Win"
            isGameOver = true
        } else if (diceSum == 2 || diceSum == 3 || diceSum == 12) {
            status = "You Lose"
            isGameOver = true
        } else {
            target = diceSum
        }
        if (isGameOver) {
            btnText = "Reset"
        }
    }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        DiceRow(
            imageResource1 = resultLeft,
            imageResource2 = resultRight,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(20.dp))
        DiceSumTextView("Result: $diceSum")
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = status, fontSize = 18.sp)
        if (target > 0) {
            Text(text = "Your target is: $target", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(onClick = {
            if (isGameOver) {
                reset()
            } else {
                roll()
            }
        }) {
            Text(text = btnText)
        }

    }

}

@Composable
private fun DiceSumTextView(text: String, modifier: Modifier = Modifier) {
    Text(text = text, fontSize = 18.sp,modifier=modifier)
}

@Composable
fun DiceImage(@DrawableRes imageResource: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = getDiceImageResource(imageResource)),
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

fun getDiceImageResource(result: Int): Int {
    return when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
}

@Preview(showBackground = true)
@Composable
fun DiceGamePreview() {
    DiceGame()
}