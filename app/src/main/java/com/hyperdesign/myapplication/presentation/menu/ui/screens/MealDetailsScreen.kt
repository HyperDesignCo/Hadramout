@file:OptIn(ExperimentalMaterial3Api::class)

package com.hyperdesign.myapplication.presentation.menu.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.hyperdesign.myapplication.domain.Entity.SubChoiceEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.menu.mvi.MealDetailsViewModel
import com.hyperdesign.myapplication.presentation.menu.mvi.MealDetialsIntents
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.FoodCardDesign
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.SizeOption
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.SubChoiceOption
import org.koin.androidx.compose.koinViewModel

@Composable
fun MealDetailsScreen(mealJson: String?, mealDetailsViewModel: MealDetailsViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val mealDetailsState by mealDetailsViewModel.MealDetailsState.collectAsStateWithLifecycle()
    val featured = mealJson?.let { Gson().fromJson(it, Meal::class.java) }

    val context = LocalContext.current
    var mealDetails by remember { mutableStateOf<MealDetailsEntity?>(null) }
    var addCartMessage by remember { mutableStateOf("") }

    // Trigger API call only once when the composable is first composed
    LaunchedEffect(Unit) {
        if (mealDetailsState.MealDetailsData == null && !mealDetailsState.isLoading) {
            mealDetailsViewModel.handleIntents(
                MealDetialsIntents.showMealDetails(branchId = mealDetailsViewModel.tokenManager.getBranchId()?:0, mealId = featured?.id ?: 0)
            )
        }
    }

    // Update mealDetails when state changes
    LaunchedEffect(mealDetailsState) {
        mealDetails = mealDetailsState.MealDetailsData?.meal
        addCartMessage = mealDetailsState.AddToCartData?.message.orEmpty()
        if (mealDetailsState.AddToCartData?.message == "Meal Added successfully") {
            Toast.makeText(context, "Meal Added successfully", Toast.LENGTH_SHORT).show()
        } else if (mealDetailsState.AddToCartData?.message != null) {
            Toast.makeText(context, mealDetailsState.AddToCartData?.message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Show content only when data is available
        mealDetails?.let { meal ->
            var selectedSize by remember { mutableStateOf(meal.sizes.firstOrNull()?.sizeTitle ?: "") }
            var selectedSizeId by remember { mutableStateOf(meal.sizes.firstOrNull()?.id ?: "") }
            var selectedChoiceId by remember { mutableStateOf<String?>(null) }
            var selectedSubChoices by remember { mutableStateOf<Map<String, List<String>>>(emptyMap()) }

            MealDetailsContent(
                meal = meal,
                selectedSize = selectedSize,
                selectedSubChoices = selectedSubChoices,
                onSizeSelected = { size -> selectedSize = size },
                onSubChoiceSelected = { choiceId, subChoiceId, isSelected ->
                    selectedSubChoices = selectedSubChoices.toMutableMap().apply {
                        val currentList = this[choiceId]?.toMutableList() ?: mutableListOf()
                        if (isSelected) {
                            if (!currentList.contains(subChoiceId)) {
                                currentList.add(subChoiceId)
                            }
                        } else {
                            currentList.remove(subChoiceId)
                        }
                        this[choiceId] = currentList
                    }
                },
                onBackPressed = { navController.popBackStack() },
                onSelectedSizeId = { sizeId -> selectedSizeId = sizeId },
                onSelectedChoiceId = { choiceId -> selectedChoiceId = choiceId },
                selectedChoiceId = selectedChoiceId,
                onAddToCart = {
                    mealDetailsViewModel.handleIntents(
                        MealDetialsIntents.addMealToCart(
                            mealId = featured?.id.toString(),
                            quantity = mealDetailsState.quantity,
                            sizeId = selectedSizeId,
                            branchId = mealDetailsViewModel.tokenManager.getBranchId().toString(),
                            choices = selectedSubChoices
                        )
                    )
                }
            )
        }

        if (addCartMessage.isNotEmpty() && addCartMessage == "Meal Added successfully") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { }
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    FoodCardDesign(
                        imageUrl = featured?.image.orEmpty(),
                        onGoToCart = { navController.navigate(Screen.CartScreen.route) {
                            popUpTo(Screen.MealDetailsScreen.route) { inclusive = true }
                        } },
                        onGoToMenu = { navController.popBackStack() }
                    )
                }
            }
        }

        // Show loading indicator when isLoading is true or data is not yet loaded
        if (mealDetailsState.isLoading || mealDetails == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Secondry)
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
    selectedSubChoices: Map<String, List<String>>,
    onSizeSelected: (String) -> Unit,
    onSubChoiceSelected: (String, String, Boolean) -> Unit,
    onBackPressed: () -> Unit,
    onAddToCart: () -> Unit,
    onSelectedSizeId: (String) -> Unit,
    onSelectedChoiceId: (String) -> Unit,
    selectedChoiceId: String?,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var bottomBarHeight by remember { mutableStateOf(0.dp) }
    val bottomBarModifier = Modifier
        .fillMaxWidth()
        .onSizeChanged { size ->
            bottomBarHeight = with(density) { size.height.toDp() }
        }
    val scrollState = rememberScrollState()

    // Calculate total price with derivedStateOf
    val totalPrice by remember(meal, selectedSize, selectedSubChoices) {
        derivedStateOf {
            val selectedSizePrice = meal?.sizes?.find { it.sizeTitle == selectedSize }?.price ?: meal?.price ?: 0.0
            val subChoicesPrice = meal?.choices?.flatMap { it.subChoices }
                ?.filter { subChoice ->
                    selectedSubChoices.any { entry -> entry.value.contains(subChoice.id) }
                }
                ?.sumOf { it.price } ?: 0.0
            selectedSizePrice + subChoicesPrice
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Fixed App Bar with Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
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

        // Content Section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = bottomBarHeight + 16.dp) // Account for bottom bar
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = meal?.title ?: "",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = meal?.description ?: "",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sizes Section
            if (!meal?.sizes.isNullOrEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = stringResource(R.string.select_size),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        meal.sizes.forEach { size ->
                            SizeOption(
                                size = size.sizeTitle,
                                price = String.format("%.2f", size.price),
                                selectedSize = selectedSize,
                                onSelected = { selected ->
                                    onSizeSelected(selected)
                                    onSelectedSizeId(size.id)
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Choices Section
            meal?.choices?.forEach { choice ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "${choice.choiceTitle} ${if(choice.min!=0){
                                stringResource(R.string.you_must_choose, choice.min)
                            }else{
                                if(choice.max!=0){
                                    stringResource(R.string.you_can_choose, choice.max)
                                }else{
                                    ""
                                }
                            }
                            }",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedChoiceId == choice.id) Color.White else Color.Black
                        )

                        if (choice.subChoices.isNotEmpty()) {
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                choice.subChoices.forEach { subChoice ->
                                    SubChoiceOption(
                                        subChoice = subChoice,
                                        choiceId = choice.id,
                                        isSelected = selectedSubChoices[choice.id]?.contains(subChoice.id) == true,
                                        onSelected = onSubChoiceSelected,
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Bottom Bar (fixed at the bottom)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp) // Separation from content
        ) {
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
                        "${String.format("%.2f", totalPrice)} ${stringResource(R.string.egy2)}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}