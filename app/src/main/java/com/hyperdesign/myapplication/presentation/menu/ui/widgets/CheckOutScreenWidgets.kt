package com.hyperdesign.myapplication.presentation.menu.ui.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.menu.mvi.CheckOutStateModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialRequestEditText(state: CheckOutStateModel,onChangeSpecialRequest:(String)->Unit){

    Card (
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Gray),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.special_request_optinal),
                color = Color.Black,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
            )
            Text(
                text = stringResource(R.string.no_extras_are_allowed_as_a_special_request),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth(),
                color = Color.Gray,
                textAlign = TextAlign.Start,


                )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = state.specialRequest,
                onValueChange = { onChangeSpecialRequest(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                ,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                placeholder = { Text(stringResource(R.string.enter_your_request_here), color = Color.LightGray) }
            )


        }
    }
}

/**
 * Smart address section:
 * - When addresses exist: shows the current address card with a subtle "+ Add new address" text link at the bottom.
 * - When no addresses exist: shows a prominent empty-state card with a pulsing icon and bold red CTA.
 */
@Composable
fun AddressSection(
    district: String,
    addressDetails: String,
    addressList: List<Pair<String, String>>,
    onAddressSelected: (String, String) -> Unit,
    onClickToAddNewAddress: () -> Unit
) {
    if (addressList.isNotEmpty()) {
        // ── Has addresses: show selector + subtle add link ──────────────────────
        AddressWithSelectorCard(
            district = district,
            addressDetails = addressDetails,
            addressList = addressList,
            onAddressSelected = onAddressSelected,
            onClickToAddNewAddress = onClickToAddNewAddress
        )
    } else {
        // ── No addresses: prominent empty-state CTA ─────────────────────────────
        EmptyAddressCard(onClickToAddNewAddress = onClickToAddNewAddress)
    }
}

@Composable
private fun AddressWithSelectorCard(
    district: String,
    addressDetails: String,
    addressList: List<Pair<String, String>>,
    onAddressSelected: (String, String) -> Unit,
    onClickToAddNewAddress: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Gray),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Section label
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFFFCB203),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.delevery_to),
                    color = Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Address selector row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .clickable { expanded = true }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = district,
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    if (addressDetails.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = addressDetails,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Change address",
                    tint = Color(0xFFFCB203)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White)
            ) {
                addressList.forEach { (districtItem, addressDetailsItem) ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(
                                    text = districtItem,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                if (addressDetailsItem.isNotEmpty()) {
                                    Text(
                                        text = addressDetailsItem,
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        },
                        onClick = {
                            onAddressSelected(districtItem, addressDetailsItem)
                            expanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subtle "+ Add new address" text link
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = Color(0xFF888888),
                            fontSize = 13.sp,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("+ ")
                        append(stringResource(R.string.add_new_address))
                    }
                },
                modifier = Modifier
                    .clickable { onClickToAddNewAddress() }
                    .padding(start = 4.dp)
            )
        }
    }
}

@Composable
private fun EmptyAddressCard(onClickToAddNewAddress: () -> Unit) {
    // Pulsing scale animation for the icon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_scale"
    )

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .border(
                width = 1.5.dp,
                color = Secondry,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClickToAddNewAddress() },
        colors = CardDefaults.cardColors(Color(0xFFFFF5F5)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pulsing location icon
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Secondry,
                modifier = Modifier
                    .size(48.dp)
                    .scale(scale)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.you_should_add_your_address_first),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Secondry,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tap-to-add CTA
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Secondry)
                    .clickable { onClickToAddNewAddress() }
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "+ ${stringResource(R.string.add_new_address)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
@Composable
fun PaymentsOption(payment: String, selectedPayment: String, onSelected: (String) -> Unit) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 12.dp)
                .background(color = Color.White)
                .border(
                    1.dp,
                    color = if (selectedPayment == payment) Color(0xFFFCB203) else Color.Transparent
                )
                .clip(RoundedCornerShape(8.dp))

                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                payment,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (selectedPayment == payment) Color(0xFFFCB203) else Color.Black
            )

            RadioButton(
                colors = RadioButtonDefaults.colors(if (selectedPayment == payment) Color(0xFFFCB203) else Color.Black),
                selected = selectedPayment == payment,
                onClick = { onSelected(payment) }
            )

        }

}

@Composable
fun showBranchDetails(
    branchName:String?,
    branchaddress:String?

){

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Gray),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.pickup_from),
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontSize = 15.sp,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = branchName?:"",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 5.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = branchaddress?:"",
                modifier = Modifier.padding(horizontal = 5.dp),
                fontSize = 13.sp,
                color = Color.Black,
                textAlign = TextAlign.Start,
            )
        }

    }
}



@Composable
fun OrderOptions(timepicker: String?,order: String, selectedOrder: String, onSelected: (String) -> Unit){

    Card(
        modifier = Modifier.padding(vertical = 10.dp,horizontal = 10.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(10.dp)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
//                .border(
//                    1.dp,
//                    color = if (selectedOrder == order) Color(0xFFFCB203) else Color.Transparent
//                )
                .clip(RoundedCornerShape(10.dp))

                ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row (horizontalArrangement = Arrangement.Absolute.SpaceBetween){
                Text(
                    order,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontWeight = FontWeight.Medium,
                    color = if (selectedOrder == order) Color(0xFFFCB203) else Color.Black
                )

                Text(
                    timepicker.orEmpty(),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 2.dp),
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }


            RadioButton(
                colors = RadioButtonDefaults.colors(if (selectedOrder == order) Color(0xFFFCB203) else Color.Black),
                selected = selectedOrder == order,
                onClick = { onSelected(order) }
            )

        }
    }
}

//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun Preview(){
//    PaymentsOption(payment = "visa", selectedPayment = "visa",{
//
//    })
//}