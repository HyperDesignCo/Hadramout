package com.hyperdesign.myapplication.presentation.menu.ui.widgets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import java.util.Locale

@Composable
fun CartBottomBar(
    priceItems: String,
    deliveryPrice: String,
    totalPrice: String,
    buttonText: String,
    deliveryTime: String,
    onPayClick: () -> Unit,
    pickUpStatus: Boolean,
    modifier: Modifier = Modifier
) {
    // Determine if the layout should be RTL based on the device's locale
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl ||
            Locale.getDefault().language == "ar"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Price row
            Row(
                modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(R.string.price),
                    fontSize = 16.sp,
                    color = Color.Gray,
//                    textAlign = if (isRtl) TextAlign.Start else TextAlign.End,
//                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = priceItems,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondry,
//                    textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
//                    modifier = Modifier.weight(1f)
                )
            }

            // Delivery row (if pickUpStatus is true)
            if (pickUpStatus) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(R.string.delivery),
                        fontSize = 16.sp,
                        color = Color.Gray,
//                        textAlign = if (isRtl) TextAlign.Start else TextAlign.End,
//                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = deliveryPrice,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondry,
//                        textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
//                        modifier = Modifier.weight(1f)
                    )

                }
            }

            // Delivery/Pickup time row
            Row(
                modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(
                        if (!pickUpStatus) R.string.pickup_time else R.string.delivery_time
                    ),
                    fontSize = 16.sp,
                    color = Color.Gray,
//                    textAlign = if (isRtl) TextAlign.Start else TextAlign.End,
//                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = stringResource(R.string.minutes, deliveryTime),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondry,
//                    textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
//                    modifier = Modifier.weight(1f)
                )

            }

            // Divider
            Divider(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                color = Color.LightGray
            )

            // Total price row
            Row(
                modifier = Modifier.padding(horizontal = 5.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(R.string.total_price),
                    fontSize = 18.sp,
                    color = Color.Gray,
//                    textAlign = if (isRtl) TextAlign.Start else TextAlign.End,
//                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${totalPrice} ${stringResource(R.string.egy2)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondry,
//                    textAlign = if (isRtl) TextAlign.End else TextAlign.Start,
//                    modifier = Modifier.weight(1f)
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pay button
            CustomButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = onPayClick,
                text = buttonText
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CartBottomBarPreview() {
    CartBottomBar(
        priceItems = "1000",
        deliveryPrice = "50",
        totalPrice = "1050",
        buttonText = "Pay Now",
        deliveryTime = "30",
        onPayClick = { Log.d("CartBottomBar", "Pay clicked") },
        pickUpStatus = true
    )
}

@Composable
@Preview(showBackground = true, showSystemUi = true, locale = "ar")
fun CartBottomBarArabicPreview() {
    CartBottomBar(
        priceItems = "١٠٠٠",
        deliveryPrice = "٥٠",
        totalPrice = "١٠٥٠",
        buttonText = "ادفع الآن",
        deliveryTime = "٣٠",
        onPayClick = { Log.d("CartBottomBar", "Pay clicked") },
        pickUpStatus = true
    )
}