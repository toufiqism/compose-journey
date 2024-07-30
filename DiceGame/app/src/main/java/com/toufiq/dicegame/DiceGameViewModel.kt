package com.toufiq.dicegame

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DiceGameViewModel : ViewModel() {

    private val _diceGameUiState = MutableStateFlow(DiceGameUIState())
    val diceGameUIState = _diceGameUiState.asStateFlow()

    fun rollDice() {
        val resultLeft = (1..6).random()
        val resultRight = (1..6).random()
        val diceSum = resultLeft + resultRight
        var status = ""
        var target = _diceGameUiState.value.target
        var isGameOver = false
        var btnText = "Roll"

        if (target > 0) {
            when (diceSum) {
                target -> {
                    status = "You Win"
                    isGameOver = true
                }
                7 -> {
                    status = "You Lose"
                    isGameOver = true
                }
            }
        } else {
            when (diceSum) {
                7, 11 -> {
                    status = "You Win"
                    isGameOver = true
                }
                2, 3, 12 -> {
                    status = "You Lose"
                    isGameOver = true
                }
                else -> target = diceSum
            }
        }

        if (isGameOver) {
            btnText = "Reset"
        }

        _diceGameUiState.update {
            it.copy(
                resultLeft = resultLeft,
                resultRight = resultRight,
                diceSum = diceSum,
                status = status,
                target = target,
                btnText = btnText,
                isGameOver = isGameOver
            )
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

    fun resetGame() {
        _diceGameUiState.update {
            DiceGameUIState()
        }
    }
}
data class DiceGameUIState(
    val resultLeft: Int = 1,
    val resultRight: Int = 1,
    val diceSum: Int = 0,
    val status: String = "",
    val target: Int = 0,
    val btnText: String = "Roll",
    val isGameOver: Boolean = false
)