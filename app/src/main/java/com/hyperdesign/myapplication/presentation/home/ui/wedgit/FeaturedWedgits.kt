package com.hyperdesign.myapplication.presentation.home.ui.wedgit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.main.theme.ui.Primary
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

data class Featured(
    val image: Int,
    val id: Int ,
    val title: String,
    val description: String,
    val price: String
)

@Composable
fun FeaturedWedgits(
    featured: Featured,
    onItemClick: (Featured) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
//            .height(180.dp)
            .clip(RoundedCornerShape(8.dp)),
        colors = cardColors(
            containerColor = Gray
        ),
        onClick = { onItemClick(featured) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = featured.image),
                    contentDescription = null,
                    modifier = Modifier
                        .width(140.dp)
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.padding(start = 4.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = featured.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondry
                    )
                    Text(
                        text = featured.description,
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "EGY ${featured.price}",
                        fontSize = 14.sp,
                        color = Secondry
                    )

                    Spacer(modifier = Modifier.weight(1f))


                    Box(
                        modifier = Modifier

                            .size(30.dp)
                            .background(color = Primary, shape = CircleShape)
                            .align(Alignment.End)

                        ,
                        contentAlignment = Alignment.BottomEnd

                    ) {
                        IconButton(
                            onClick = { /* Handle click */ },
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
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true
)
fun FeaturedWedgitsPreview() {
    FeaturedWedgits(
        featured = Featured(
            image = com.hyperdesign.myapplication.R.drawable.featured,
            title = "Featured Item",
            description = "This is a description of the featured item.",
            price = "100.00"
            , id = 1
        ),
        onItemClick = {}
    )
}