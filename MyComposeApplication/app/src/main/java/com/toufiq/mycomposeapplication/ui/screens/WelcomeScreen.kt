package com.toufiq.mycomposeapplication.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import com.toufiq.mycomposeapplication.ui.FactsViewModel
import com.toufiq.mycomposeapplication.ui.components.AppBarView
import com.toufiq.mycomposeapplication.ui.components.FactComposable
import com.toufiq.mycomposeapplication.ui.components.TextComponent
import com.toufiq.mycomposeapplication.ui.components.TextWithShadow

@Composable
fun WelcomeScreen(userName: String?, animalSelected: String?) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {
            AppBarView(text = "Hello and Welcome $userName")

            TextComponent(textValue = "Thanks for sharing your opinion", size = 18.sp)

            Spacer(modifier = Modifier.size(40.dp))

            val selected = if (animalSelected.equals("cat")) {
                "Cat"
            } else {
                "Dog"
            }
            TextWithShadow(text = "You are a $selected Lover !!")

            val factsViewModel=FactsViewModel()

            FactComposable(value = factsViewModel.generateRandomFact(animalSelected!!))
        }

    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen("", "")
}