@file:OptIn(ExperimentalMaterial3Api::class)

package com.hyperdesign.myapplication.presentation.menu.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.gson.Gson
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.ChoiceEntity
import com.hyperdesign.myapplication.domain.Entity.Meal
import com.hyperdesign.myapplication.domain.Entity.MealDetailsEntity
import com.hyperdesign.myapplication.domain.Entity.MealEntity
import com.hyperdesign.myapplication.domain.Entity.SubChoiceEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.menu.mvi.MealDetailsViewModel
import com.hyperdesign.myapplication.presentation.menu.mvi.MealDetialsIntents
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.SizeOption
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.SubChoiceOption
import org.koin.androidx.compose.koinViewModel

@Composable
fun MealDetailsScreen(mealJson: String?, mealDetailsViewModel: MealDetailsViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val mealDetailsState by mealDetailsViewModel.MealDetailsState.collectAsStateWithLifecycle()
    val featured = mealJson?.let { Gson().fromJson(it, Meal::class.java) }

    var mealDetails by remember { mutableStateOf<MealDetailsEntity?>(null) }

    // Trigger API call only once when the composable is first composed
    LaunchedEffect(Unit) {
        if (mealDetailsState.MealDetailsData == null && !mealDetailsState.isLoading) {
            mealDetailsViewModel.handleIntents(
                MealDetialsIntents.showMealDetails(branchId = 2, mealId = featured?.id ?: 0)
            )
        }
    }

    // Update mealDetails when state changes
    LaunchedEffect(mealDetailsState) {
        mealDetails = mealDetailsState.MealDetailsData?.meal
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Show content only when data is available
        mealDetails?.let { meal ->
            var selectedSize by remember { mutableStateOf(meal.sizes.firstOrNull()?.sizeTitle ?: "") }
            var selectedSubChoices by remember { mutableStateOf(setOf<String>()) }
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

            MealDetailsContent(
                meal = meal,
                selectedSize = selectedSize,
                selectedSubChoices = selectedSubChoices,
                scrollBehavior = scrollBehavior,
                onSizeSelected = { size -> selectedSize = size },
                onSubChoiceSelected = { subChoiceId, isSelected ->
                    selectedSubChoices = if (isSelected) {
                        selectedSubChoices + subChoiceId
                    } else {
                        selectedSubChoices - subChoiceId
                    }
                },
                onBackPressed = { navController.popBackStack() },
                onAddToCart = { /* TODO: Implement add to cart */ }
            )
        }

        // Show loading indicator when isLoading is true or data is not yet loaded
        if (mealDetailsState.isLoading || mealDetails == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Secondry)
                }
            }
        }

        // Show error message if there's an error
        if (mealDetailsState.errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mealDetailsState.errorMessage ?: "An error occurred",
                    color = Color.Red,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailsContent(
    meal: MealDetailsEntity?,
    selectedSize: String,
    selectedSubChoices: Set<String>,
    scrollBehavior: TopAppBarScrollBehavior,
    onSizeSelected: (String) -> Unit,
    onSubChoiceSelected: (String, Boolean) -> Unit,
    onBackPressed: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val maxAppBarHeight = 300.dp
    val minAppBarHeight = 56.dp
    val appBarHeight = (maxAppBarHeight + density.run { scrollBehavior.state.heightOffset.toDp() })
        .coerceIn(minAppBarHeight, maxAppBarHeight)

    // Calculate total price with derivedStateOf
    val totalPrice by remember(meal, selectedSize, selectedSubChoices) {
        derivedStateOf {
            val selectedSizePrice = meal?.sizes?.find { it.sizeTitle == selectedSize }?.price ?: meal?.price ?: 0.0
            val subChoicesPrice = meal?.choices?.flatMap { it.subChoices }
                ?.filter { selectedSubChoices.contains(it.id) }
                ?.sumOf { it.price } ?: 0.0
            selectedSizePrice + subChoicesPrice
        }
    }

    var bottomBarHeight by remember { mutableStateOf(0.dp) }
    val bottomBarModifier = Modifier
        .fillMaxWidth()
        .onSizeChanged { size ->
            bottomBarHeight = with(density) { size.height.toDp() }
        }
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Box(
                modifier = Modifier
                    .height(appBarHeight)
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                MainHeader(
                    title = "",
                    showBackPressedIcon = true,
                    onBackPressesd = onBackPressed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .offset(y = density.run { scrollBehavior.state.heightOffset.toDp() })
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(meal?.imageUrl)
                            .crossfade(true)
                            .error(R.drawable.test_food)
                            .placeholder(R.drawable.test_food)
                            .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                            .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                            .build(),
                        contentDescription = meal?.title,
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
                modifier = bottomBarModifier
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
                        "ج.م${String.format("%.2f", totalPrice)}",
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
//                .padding(bottom = bottomBarHeight + 16.dp) // Dynamic padding + extra margin

                ,
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = meal?.title ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = meal?.description ?: "",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sizes Section
            if (!meal?.sizes.isNullOrEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color.Black),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = stringResource(R.string.select_size),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            meal.sizes.forEach { size ->
                                SizeOption(
                                    size = size.sizeTitle,
                                    price = String.format("%.2f", size.price),
                                    selectedSize = selectedSize,
                                    onSelected = onSizeSelected
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Choices Section
            meal?.choices?.forEach { choice ->
                if (choice.subChoices.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color.Black),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = choice.choiceTitle,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                choice.subChoices.forEach { subChoice ->
                                    SubChoiceOption(
                                        subChoice = subChoice,
                                        isSelected = selectedSubChoices.contains(subChoice.id),
                                        onSelected = onSubChoiceSelected
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}