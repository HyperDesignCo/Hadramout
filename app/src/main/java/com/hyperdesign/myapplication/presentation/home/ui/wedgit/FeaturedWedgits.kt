package com.hyperdesign.myapplication.presentation.home.ui.wedgit

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.AdsEntity
import com.hyperdesign.myapplication.domain.Entity.Meal
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.main.theme.ui.Primary
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun FeaturedWedgits(
    meal: Meal,
    onItemClick: (Meal) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .height(180.dp) // Fixed height for consistent card size
            .clip(RoundedCornerShape(8.dp)),
        colors = cardColors(containerColor = Gray),
        onClick = { onItemClick(meal) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(meal.image)
                    .crossfade(true)
                    .error(R.drawable.test_food)
                    .placeholder(R.drawable.test_food)
                    .build(),
                contentDescription = "Meal image ${meal.title}",
                modifier = Modifier
                    .width(140.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = meal.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Secondry,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = meal.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${meal.price} ${stringResource(R.string.egy2)}",
                    fontSize = 14.sp,
                    color = Secondry
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(color = Primary, shape = CircleShape)
                        .align(Alignment.End),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { onItemClick(meal) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to Cart",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AdsWdegit(
    meal: AdsEntity,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .width(140.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(8.dp)),
        colors = cardColors(containerColor = Gray),
        onClick = { onItemClick(meal.mealId?:"") }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(meal.image)
                    .crossfade(true)
                    .error(R.drawable.test_food)
                    .placeholder(R.drawable.test_food)
                    .build(),
                contentDescription = "Meal image ${meal.image}",
                modifier = Modifier
                    .width(140.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )


        }
    }
}