package com.toufiq.mycomposeapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.toufiq.mycomposeapplication.R
import com.toufiq.mycomposeapplication.data.UserDataUiEvents
import com.toufiq.mycomposeapplication.ui.UserInputViewModel
import com.toufiq.mycomposeapplication.ui.components.AnimalCard
import com.toufiq.mycomposeapplication.ui.components.AppBarView
import com.toufiq.mycomposeapplication.ui.components.TextComponent
import com.toufiq.mycomposeapplication.ui.components.TextFieldComponent

@Composable
fun UserInputScreen(userInputViewModel: UserInputViewModel) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {
            AppBarView("Hi There Bro!")
            TextComponent(
                textValue = "Let's Learn About You !", size = 24.sp
            )
            Spacer(modifier = Modifier.size(20.dp))
            TextComponent(
                textValue = "This app will do something according to your input you provided !",
                size = 18.sp
            )
            Spacer(modifier = Modifier.size(60.dp))
            TextComponent(
                textValue = "Name",
                size = 18.sp
            )
            Spacer(modifier = Modifier.size(10.dp))
            TextFieldComponent(onTextChange = {
                userInputViewModel.onEvent(UserDataUiEvents.UserNameEntered(it))
            })
            Spacer(modifier = Modifier.size(20.dp))
            TextComponent(
                textValue = "What Do you Like ?",
                size = 18.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimalCard(
                    image = R.drawable.ic_cat,
                    selected = userInputViewModel.uIState.value.animalSelected == "cat",
                    onAnimalSelected = {
                        userInputViewModel.onEvent(UserDataUiEvents.AnimalSelected(it))
                    })
                AnimalCard(
                    image = R.drawable.ic_dog,
                    selected = userInputViewModel.uIState.value.animalSelected == "dog",
                    onAnimalSelected = {
                        userInputViewModel.onEvent(UserDataUiEvents.AnimalSelected(it))
                    })
            }

        }
    }
}

@Preview
@Composable
fun UserInputScreenPreview() {
    UserInputScreen(UserInputViewModel())
}