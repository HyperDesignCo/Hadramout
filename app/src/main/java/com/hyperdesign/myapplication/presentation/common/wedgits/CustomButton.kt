package com.hyperdesign.myapplication.presentation.common.wedgits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    startColor: Color = Color(0xFFF15A25), // Outer gradient color
    endColor: Color = Color(0xFFFCB203),  // Inner gradient color
    textColor: Color = Color.White,
    cornerRadius: Int = 26,
    paddingHorizontal: Int = 16,
    paddingVertical: Int = 2
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(startColor, endColor)
                ),
                shape = RoundedCornerShape(cornerRadius.dp)
            )
            .padding(horizontal = paddingHorizontal.dp, vertical = paddingVertical.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(cornerRadius.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}