package com.toufiq.mycomposeapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.dice),
            contentDescription = "dice logo",
            modifier = Modifier.size(40.dp)
        )
    }
}


@Preview
@Composable
fun AppBarViewPreview() {
    AppBarView("Hello")
}


@Composable
fun TextComponent(textValue: String, size: TextUnit) {
    Text(text = textValue, fontSize = size)
}

@Preview
@Composable
fun TextComponentPreview() {
    TextComponent(textValue = "Hey!", size = 17.sp)
}


