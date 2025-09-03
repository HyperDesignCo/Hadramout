package com.hyperdesign.myapplication.presentation.profile.myorders.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Replay5
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.Meal
import com.hyperdesign.myapplication.domain.Entity.MealOrderEntity
import com.hyperdesign.myapplication.domain.Entity.OrderEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.CustomButton
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.main.theme.ui.Primary
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun ShowNewOrdersOrPreviousOrders(onSelectNewOrPreviousOrder:(Boolean)->Unit){

    var showOrder by remember { mutableStateOf(true) }
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp,Color.LightGray),
        colors = CardDefaults.cardColors(Gray)
    ){
        Row (modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()){

            Box(modifier =Modifier
                .weight(1f)
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(color = if (showOrder) Secondry else Color.Transparent)
                .clickable {
                    showOrder = true
                    onSelectNewOrPreviousOrder(true)
                }, contentAlignment = Alignment.Center ){
                Text(
                    stringResource(R.string.new_orders),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if(showOrder) Color.White else Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,



                    )
            }


            Box(modifier =Modifier
                .weight(1f)
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(color = if (!showOrder) Secondry else Color.Transparent)
                .clickable {
                    showOrder = false
                    onSelectNewOrPreviousOrder(false)

                }, contentAlignment = Alignment.Center ){
                Text(
                    stringResource(R.string.previous_orders),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if(!showOrder) Color.White else Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,



                    )
            }
        }

    }
}



@Composable
fun OrderItem(
    order: OrderEntity,
    meal : MealOrderEntity,
    onReOrder:()->Unit

) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .height(190.dp) // Fixed height for consistent card size
            .clip(RoundedCornerShape(8.dp)),
        colors = cardColors(containerColor = Gray),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(meal.mealImage)
                    .crossfade(true)
                    .error(R.drawable.test_food)
                    .placeholder(R.drawable.test_food)
                    .build(),
                contentDescription = "Meal image ",
                modifier = Modifier
                    .width(110.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = meal.mealTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondry,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = order.createdAt,
                    fontSize = 16.sp,
                    color = Color.LightGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Quantity: ${meal.quantity}",
                    fontSize = 16.sp,
                    color = Color.LightGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.egy, meal.price),
                    fontSize = 16.sp,
                    color = Color.LightGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )




            }
        }
            Box(modifier = Modifier
                .padding(bottom = 15.dp)
                .fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        onReOrder()
                    },
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFF15A25), Color(0xFFFCB203))
                            ),
                            shape = RoundedCornerShape(26.dp)
                        )
                        .padding(horizontal = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay5,
                        contentDescription = "",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource(R.string.reorder),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

        }

    }
}
//
//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun preview(){
//    OrderItem()
//}