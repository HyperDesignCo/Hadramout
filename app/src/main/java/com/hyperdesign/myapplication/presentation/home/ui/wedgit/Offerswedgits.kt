package com.hyperdesign.myapplication.presentation.home.ui.wedgit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hyperdesign.myapplication.R
import kotlinx.coroutines.delay

data class offers(
    val image: Int,
    val id: Int,
)

@Composable
fun OffersList(offers: List<offers>) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { offers.size }
    )

    // Auto-scroll every 3 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Wait for 3 seconds
            val nextPage = (pagerState.currentPage + 1) % offers.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp) // Adjust height as needed
            .padding(horizontal = 4.dp)
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp)
        ) {
            Image(
                painter = painterResource(id = offers[page].image),
                contentDescription = "offer image ${offers[page].id}",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun OffersListPreview() {
    OffersList(
        offers = listOf(
            offers(
                image = R.drawable.test_food,
                id = 1
            ),
            offers(
                image = R.drawable.test_food,
                id = 2
            )
        )
    )
}