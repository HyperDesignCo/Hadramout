package com.hyperdesign.myapplication.presentation.home.ui.wedgit

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.Meal
import kotlinx.coroutines.delay

data class offers(
    val image: String, // Changed from Int to String for URL
    val id: Int
)

@Composable
fun OffersList(offers: List<Meal>) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { offers.size }
    )

    // Auto-scroll only if offers is not empty
    LaunchedEffect(offers) {
        if (offers.isNotEmpty()) {
            while (true) {
                delay(3000) // Wait for 3 seconds
                val nextPage = (pagerState.currentPage + 1) % offers.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    if (offers.isEmpty()) {

    } else {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(horizontal = 4.dp)
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(offers[page].image)
                        .crossfade(true)
                        .error(R.drawable.test_food)
                        .placeholder(R.drawable.test_food)
                        .build(),
                    contentDescription = "Offer image ${offers[page].id}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }
}