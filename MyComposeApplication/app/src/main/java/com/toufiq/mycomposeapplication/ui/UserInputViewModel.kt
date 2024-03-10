package com.toufiq.mycomposeapplication.ui

import android.service.autofill.UserData
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.toufiq.mycomposeapplication.data.UserDataUiEvents
import com.toufiq.mycomposeapplication.data.UserInputScreenState

class UserInputViewModel : ViewModel() {

     var uIState = mutableStateOf(UserInputScreenState())


    fun onEvent(event: UserDataUiEvents) {
        when (event) {
            is UserDataUiEvents.UserNameEntered -> {
                uIState.value=uIState.value.copy(
                    nameEntered = event.name
                )
            }

            is UserDataUiEvents.AnimalSelected -> {
                uIState.value=uIState.value.copy(
                    animalSelected = event.animalValue
                )
            }
        }

    }
    fun isValid():Boolean{
        return uIState.value.animalSelected.isNotEmpty() && uIState.value.nameEntered.isNotEmpty()
    }
}



