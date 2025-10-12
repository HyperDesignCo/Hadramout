package com.hyperdesign.myapplication.presentation.menu.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.SubChoiceEntity

@Composable
fun SizeOption(size: String, price: String, selectedSize: String, onSelected: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (selectedSize == size)  Brush.linearGradient(
                listOf(Color(0xFFF15A25), Color(0xFFFCB203))
            ) else Brush.linearGradient(
                listOf(Color(0xFFF8F8F8), Color(0xFFF8F8F8))
            ) )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(size, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = if (selectedSize == size) Color.White else Color.Black)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("$price ${stringResource(R.string.egy2)}", fontSize = 14.sp, color = if (selectedSize == size) Color.White else Color.Black)
            RadioButton(
                colors = RadioButtonDefaults.colors(if (selectedSize == size) Color.White else Color.Black),

                selected = selectedSize == size,
                onClick = { onSelected(size) }
            )
        }
    }
}



@Composable
fun SubChoiceOption(
    subChoice: SubChoiceEntity,
    choiceId: String,
    min: Int,
    max: Int,
    isSelected: Boolean,
    onSelected: (String, String, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) Brush.linearGradient(
                    listOf(Color(0xFFF15A25), Color(0xFFFCB203))
                ) else Brush.linearGradient(
                    listOf(Color(0xFFF8F8F8), Color(0xFFF8F8F8))
                )
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = subChoice.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else Color.Black
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${subChoice.price} ${stringResource(R.string.egy2)}",
                fontSize = 14.sp,
                color = if (isSelected) Color.White else Color.Black
            )
            if (max == 1) {
                RadioButton(
                    selected = isSelected,
                    onClick = {
                        val newSelected = if (isSelected && min == 0) false else true
                        onSelected(choiceId, subChoice.id, newSelected)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor =Color.White,
                        unselectedColor = Color.Black
                    )
                )
            } else {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onSelected(choiceId, subChoice.id, it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFFFCB203),
                        checkmarkColor = Color.White,
                        uncheckedColor = Color.Black
                    )
                )
            }
        }
    }
}