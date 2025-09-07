package com.hyperdesign.myapplication.presentation.profile.addresses.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hyperdesign.myapplication.domain.Entity.AddressEntity

@Composable
fun AddressItem(address: AddressEntity,onDeleteAddress:()->Unit,onGoToUpdateAddress:()->Unit){

    Row (modifier = Modifier.fillMaxWidth().clickable{
        onGoToUpdateAddress()
    }, horizontalArrangement = Arrangement.SpaceBetween){
        IconButton(
            onClick = {onDeleteAddress()},
        ){
            Icon(
                imageVector = Icons.Default.Delete, contentDescription = "delete",
                tint = Color(0XFFFCB203)
            )
        }

        Text("${address.area.name},${address.region.name},${address.sub_region},${address.street},${address.special_sign},${address.building_number},${address.floor_number}")
    }

}