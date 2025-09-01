package com.hyperdesign.myapplication.presentation.menu.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun CartBottomBar(
    priceItems: String,
    deliveryPrice: String,
    totalPrice: String,
    buttonText:String,
    onPayClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = priceItems,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondry
                    )
                    Text(
                        text = deliveryPrice,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondry
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.price),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = stringResource(R.string.delivery),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

            }
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
                    .background(Color.LightGray)

            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = " جنيه${totalPrice}",
                    modifier = Modifier.fillMaxWidth(0.7f),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondry
                )

                Text(
                    text = stringResource(R.string.total_price),
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                onClick = onPayClick,
                text =buttonText

                )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun CartBottomBarPreview() {
    CartBottomBar("3", "1000","1500", "",onPayClick = {})
}