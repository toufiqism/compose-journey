package com.toufiq.mycomposeapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toufiq.mycomposeapplication.R

@Composable
fun AppBarView(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 21.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.ic_dice),
            contentDescription = "dice logo",
            modifier = Modifier.size(40.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AppBarViewPreview() {
    AppBarView("Hello")
}


@Composable
fun TextComponent(textValue: String, size: TextUnit, colorValue: Color = Color.Black) {
    Text(text = textValue, fontSize = size, color = colorValue, fontWeight = FontWeight.Light)
}

@Preview(showBackground = true)
@Composable
fun TextComponentPreview() {
    TextComponent(textValue = "Hey!", size = 24.sp)
}


@Composable
fun TextFieldComponent(onTextChange: (name: String) -> Unit) {
    val currentValue by remember {
        mutableStateOf("")
    }
    val localFocusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = currentValue,
        onValueChange = {
            onTextChange(it)
        },
        placeholder = {
            Text(text = "Enter Your Name", fontSize = 18.sp)
        },
        textStyle = TextStyle.Default.copy(fontSize = 24.sp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions { localFocusManager.clearFocus() }
    )
}

@Preview(showBackground = true)
@Composable
fun TextFieldComponentPreview() {
    TextFieldComponent({})
}


@Composable
fun AnimalCard(image:Int) {
    Card(
        modifier = Modifier
            .padding(24.dp)
            .size(130.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Image(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight()
                .wrapContentWidth(),
            painter = painterResource(id = image),
            contentDescription = "Animal Image"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AnimalCardPreview() {
    AnimalCard(image = R.drawable.ic_dog)
}