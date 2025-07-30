package com.hyperdesign.myapplication.presentation.common.wedgits

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    textColor: Color = Color.Black,
    borderColor: Color = Color(0xFF8F8F8F),
    borderWidth: Float = 2f
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .border(
                width = borderWidth.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        textStyle = androidx.compose.ui.text.TextStyle(
            color = textColor,
            fontSize = 16.sp
        ),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = textColor.copy(alpha = 0.3f),
                    fontSize = 16.sp
                )
            }
            innerTextField()
        }
    )
}

@Preview(showBackground = true, name = "CustomTextFieldPreview", showSystemUi = true)
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        value = "+2 ",
        onValueChange = {},
        placeholder = "Enter text",
        textColor = Color.Black,
        borderWidth = 2f,
        modifier = Modifier.padding(16.dp).fillMaxWidth()
    )
}
