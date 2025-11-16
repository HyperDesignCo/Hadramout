package com.hyperdesign.myapplication.presentation.menu.ui.widgets

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    priceItems: String,          // primary price (items only)
    deliveryPrice: String,       // delivery cost
    totalPrice: String,          // final total
    buttonText: String,
    vatCost:String?,
    serviceCharcheCost:String?,
    deliveryTime: String,
    pickUpStatus: Boolean,       // true → show delivery rows
    onPayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // RTL detection (layout direction + locale fallback)
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
            // ── Price (items) ───────────────────────
            PriceRow(
                label = stringResource(R.string.price),
                value = priceItems
            )

            // ── Delivery (only when pickUpStatus == true) ─────
            if (pickUpStatus) {
//                Spacer(modifier = Modifier.height(8.dp))
                PriceRow(
                    label = stringResource(R.string.delivery),
                    value = deliveryPrice
                )
            }

            // ── Delivery time (only when pickUpStatus == true) ─
            if (pickUpStatus) {
//                Spacer(modifier = Modifier.height(8.dp))
                PriceRow(
                    label = stringResource(R.string.delivery_time),
                    value = stringResource(R.string.minutes, deliveryTime)
                )
            }

            vatCost?.let {
                PriceRow(
                    label = stringResource(R.string.VAT),
                    value = vatCost
                )
            }

            serviceCharcheCost?.let {
                PriceRow(
                    label = stringResource(R.string.service_charge_cost),
                    value = serviceCharcheCost
                )
            }



            // ── Divider & final total ───────────────────────
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.total_price),
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${totalPrice} ${stringResource(R.string.egy2)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondry
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── Pay button ───────────────────────────────────
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

/** Small reusable row for a label-value pair */
@Composable
private fun PriceRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Secondry
        )
    }
}


//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//private fun CartBottomBarPreview() {
//    CartBottomBar(
//        priceItems = "1000",
//        deliveryPrice = "50",
//        totalPrice = "1050",
//        buttonText = "Pay Now",
//        deliveryTime = "30",
//        onPayClick = { Log.d("CartBottomBar", "Pay clicked") },
//        pickUpStatus = true
//    )
//}
//
//@Composable
//@Preview(showBackground = true, showSystemUi = true, locale = "ar")
//private fun CartBottomBarArabicPreview() {
//    CartBottomBar(
//        priceItems = "١٠٠٠",
//        deliveryPrice = "٥٠",
//        totalPrice = "١٠٥٠",
//        buttonText = "ادفع الآن",
//        deliveryTime = "٣٠",
//        onPayClick = { Log.d("CartBottomBar", "Pay clicked") },
//        pickUpStatus = true
//    )
//}