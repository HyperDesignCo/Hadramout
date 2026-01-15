package com.hyperdesign.myapplication.presentation.home.ui.wedgit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.Branch
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.home.HomeObject
import kotlin.collections.forEach

@Composable
fun NoDeliveryDialogHome(
    onPickUp: () -> Unit, onChangeLocation: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { }, title = {
            Text(
                stringResource(R.string.no_delivery_available),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }, confirmButton = {
            TextButton(
                modifier = Modifier.fillMaxWidth(), onClick = {
                    onChangeLocation()
                    HomeObject.updateStatus(1)
                }) {
                Text(
                    stringResource(R.string.change_Location),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFFFCB203)
                )
            }
        }, dismissButton = {
            TextButton(onClick = {
                onPickUp()
                HomeObject.updateStatus(1)
            }) {
                Text(
                    stringResource(R.string.pick_up),
                    color = Color(0xFFF15A25),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }, containerColor = Color.White
    )
}

@Composable
fun PickupBranchDialog(
    branches: List<Branch>,
    currentBranchId: Int,
    onBranchSelected: (Branch) -> Unit,
    onDismiss: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedBranch by remember { mutableStateOf<Branch?>(null) }

    LaunchedEffect(currentBranchId, branches) {
        if (branches.isNotEmpty()) {
            selectedBranch = branches.find { it.id == currentBranchId } ?: branches[0]
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss, icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_pickup),
                contentDescription = "Pickup Icon",
                tint = Color.Black,
                modifier = Modifier.size(48.dp)
            )
        }, title = {
            Text(
                text = "استلام من الفرع",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }, text = {
            Column {
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .background(Color.White)
                    .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = selectedBranch?.title ?: "",
                        color = if (selectedBranch == null) Color.Gray else Color.Black,
                        fontSize = 16.sp
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color(0xFFF15A25)
                    )
                }
                DropdownMenu(
                    expanded = expanded, onDismissRequest = { expanded = false }) {
                    if (branches.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("No branches available") },
                            onClick = { expanded = false })
                    } else {
                        branches.forEach { branch ->
                            DropdownMenuItem(text = { Text(branch.title) }, onClick = {
                                selectedBranch = branch
                                expanded = false
                            })
                        }
                    }
                }
            }
        }, confirmButton = {
            CustomButton(
                onClick = {
                    selectedBranch?.let { onBranchSelected(it) }
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.complete_order),
            )
        }, containerColor = Color.White
    )
}