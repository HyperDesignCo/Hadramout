package com.hyperdesign.myapplication.presentation.profile.addresses.ui.widgets

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.hyperdesign.myapplication.domain.Entity.AreaAndRegionEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchableDropdownMenu(
    modifier: Modifier = Modifier,
    items: List<AreaAndRegionEntity>,
    selectedItem: String?,
    hint: String? = null,
    onItemSelected: (String) -> Unit,
    onItemSelectId: (AreaAndRegionEntity) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null,
    borderColor: Color = Color(0xFFFCB203),
    fillColor: Color = Color.White,
    hintColor: Color = Color.Black,
    selectedId: Int? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(items, selectedId, selectedItem) {
        if (items.isNotEmpty()) {
            if (selectedId != null) {
                val initialItem = items.find { it.id == selectedId }
                if (initialItem != null && selectedText != initialItem.name) {
                    selectedText = initialItem.name
                    Log.d("DropdownSelection", "Initial selection set from ID: ${initialItem.name}")
                }
            } else if (selectedItem != null && selectedText.isEmpty()) {
                selectedText = selectedItem
                Log.d("DropdownSelection", "Initial selection set from item: $selectedItem")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        Log.d("CustomSearchableDropdownMenu", "Column tapped! Expanded: $expanded -> ${!expanded}")
                        expanded = !expanded
                    }
                )
            }
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            shape = RoundedCornerShape(8.dp),
            label = { Text(hint.orEmpty(), color = hintColor) },
            modifier = modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        Log.d("CustomSearchableDropdownMenu", "Trailing icon clicked! Expanded: $expanded -> ${!expanded}")
                        expanded = !expanded
                    }
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Dropdown Icon",
                        tint = if (isError) Color.Red else Color(0xFFFCB203)
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Black,
                focusedBorderColor = if (isError) Color.Red else borderColor,
                unfocusedBorderColor = if (isError) Color.Red else borderColor,
                disabledBorderColor = if (isError) Color.Red else borderColor,
            ),
            isError = isError
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search...", color = Color.Black) },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            val filteredItems = items.filter { it.name.contains(searchText, ignoreCase = true) }

            filteredItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name, color = Color.Black) },
                    onClick = {
                        selectedText = item.name
                        onItemSelected(item.name)
                        onItemSelectId(item)
                        expanded = false
                        searchText = ""
                    }
                )
            }

            if (filteredItems.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No results found", color = Color.Black) },
                    onClick = {}
                )
            }
        }

        if (isError) {
            Text(
                text = errorMessage.orEmpty(),
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}