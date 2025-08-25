@file:OptIn(ExperimentalMaterial3Api::class)

package com.hyperdesign.myapplication.presentation.menu.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.gson.Gson
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.Meal
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.SizeOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetails(mealJson: String?) {
    val navController = LocalNavController.current
    val featured = mealJson?.let { Gson().fromJson(it, Meal::class.java) }

    featured?.let {
        var selectedSize by androidx.compose.runtime.mutableStateOf("small")
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

        MealDetailsContent(
            featured = it,
            selectedSize = selectedSize,
            scrollBehavior = scrollBehavior,
            onSizeSelected = { size -> selectedSize = size },
            onBackPressed = { navController.popBackStack() },
            onAddToCart = { /* TODO: Implement add to cart */ }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailsContent(
    featured: Meal,
    selectedSize: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onSizeSelected: (String) -> Unit,
    onBackPressed: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val maxAppBarHeight = 300.dp
    val minAppBarHeight = 56.dp
    val appBarHeight = (maxAppBarHeight + density.run { scrollBehavior.state.heightOffset.toDp() })
        .coerceIn(minAppBarHeight, maxAppBarHeight)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Box(
                modifier = Modifier
                    .height(appBarHeight)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                // Pinned MainHeader (back button stays visible)
                MainHeader(
                    title = "",
                    showBackPressedIcon = true,
                    onBackPressesd = onBackPressed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )

                // Collapsing Image
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .offset(y = density.run { scrollBehavior.state.heightOffset.toDp() })
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(featured.image)
                            .crossfade(true)
                            .error(R.drawable.test_food)
                            .placeholder(R.drawable.test_food)
                            .build(),
                        contentDescription = featured.title,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(230.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        bottomBar = {
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFF15A25), Color(0xFFFCB203))
                        )
                    ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.add_to_cart),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${featured.price}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                // Start content with small margin
                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = featured.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description
                Text(
                    text = featured.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Price
                Text(
                    text = "Price: ${featured.price}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Sizes Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        SizeOption("small", "160.00", selectedSize, onSizeSelected)
                        SizeOption("medium", "230.00", selectedSize, onSizeSelected)
                        SizeOption("large", "300.00", selectedSize, onSizeSelected)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun MealDetailsScreenPreview() {
//    val sampleFeatured = Featured(
//        image = R.drawable.featured,
//        id = 1,
//        title = "Charcoal Grills",
//        description = "Lorem ipsum dolor sit amet",
//        price = "$10.00"
//    )
//    val mealJson = Gson().toJson(sampleFeatured)
//    MealDetails(mealJson)
//}