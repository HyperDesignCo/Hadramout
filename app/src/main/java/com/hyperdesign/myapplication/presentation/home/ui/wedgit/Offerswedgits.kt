package com.hyperdesign.myapplication.presentation.home.ui.wedgit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

data class offers(
    val image: Int,
    val id: Int,
)

@Composable
fun OffersList(offers: offers) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .height(140.dp)
            .width(230.dp)


    ){
        Image(
            painter = painterResource(id = offers.image),
            contentDescription = "offer image",
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop

        )
    }


}


//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun offers(){
//    OffersList(
//        offers(
//            image = R.drawable.test_food,
//            id = 1
//        )
//    )
//}