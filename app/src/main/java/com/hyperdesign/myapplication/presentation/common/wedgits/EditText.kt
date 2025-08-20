package com.hyperdesign.myapplication.presentation.common.wedgits

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    textColor: Color = Color.Black,
    borderColor: Color = Color(0xFFFCB203),
    errorBorderColor: Color = Color.Red, // Added for error state
    borderWidth: Float = 2f,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1,
    onNext: () -> Unit = {},
    focusRequester: FocusRequester = FocusRequester(),
    isError: Boolean = false,
    errorMessage: String? = null,
    validator: ((String) -> String?)? = null
) {
    var textFieldValue by rememberSaveable { mutableStateOf(value) }
    var validationError by rememberSaveable { mutableStateOf<String?>(null) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None

    LaunchedEffect(value) {
        if (value != textFieldValue) {
            textFieldValue = value
        }
    }

    Column { // Wrap in Column to display error message below
        BasicTextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                validationError = validator?.invoke(it)
                onValueChange(it)
            },
            modifier = modifier
                .focusRequester(focusRequester)
                .border(
                    width = borderWidth.dp,
                    color = if (isError || validationError != null) errorBorderColor else borderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(
                    horizontal = if (isPassword) 12.dp else 12.dp,
                    vertical = if (isPassword) 0.dp else 12.dp
                ),
            textStyle = TextStyle(
                color = textColor,
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                onNext()
            }),
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (textFieldValue.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = textColor.copy(alpha = 0.3f),
                                fontSize = 12.sp
                            )
                        }
                        innerTextField()
                    }
                    if (isPassword) {
                        IconButton(
                            onClick = { isPasswordVisible = !isPasswordVisible }
                        ) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                                tint = textColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        )

        // Display error message if isError is true or validator returns an error
        if (isError || validationError != null) {
            Text(
                text = validationError ?: errorMessage.orEmpty(),
                color = errorBorderColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "CustomTextFieldPreview", showSystemUi = true)
@Composable
fun CustomTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        CustomTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "Enter text",
            textColor = Color.Black,
            borderWidth = 2f,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = "Enter password",
            textColor = Color.Black,
            borderWidth = 2f,
            modifier = Modifier.fillMaxWidth(),
            isPassword = true
        )
    }
}