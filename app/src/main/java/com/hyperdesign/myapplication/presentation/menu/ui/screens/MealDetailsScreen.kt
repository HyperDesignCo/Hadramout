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
import com.hyperdesign.myapplication.presentation.common.wedgits.ShowAuthentaionDialge
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.menu.mvi.MealDetailsViewModel
import com.hyperdesign.myapplication.presentation.menu.mvi.MealDetialsIntents
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.FoodCardDesign
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.SizeOption
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.SubChoiceOption
import org.koin.androidx.compose.koinViewModel


sealed class MealInput() {
    data class MealJson(val json: String) : MealInput()
    data class MealId(val id: String) : MealInput()
}
@Composable
fun MealDetailsScreen(
    mealJson: MealInput?,
    mealDetailsViewModel: MealDetailsViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val mealDetailsState by mealDetailsViewModel.MealDetailsState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var mealDetails by remember { mutableStateOf<MealDetailsEntity?>(null) }
    var addCartMessage by remember { mutableStateOf("") }
    val showAuthDialoge by mealDetailsViewModel.showAuthDialoge

    // Utility function to clean the mealId string
    fun cleanMealId(input: String?): String? {
        if (input == null) return null
        // Remove surrounding quotes and trim whitespace
        val cleaned = input.trim().removeSurrounding("\"", "\"").removeSurrounding("'", "'")
        // Validate if the cleaned string is numeric
        return if (cleaned.matches(Regex("\\d+"))) cleaned else null
    }

    // Parse the input to get the mealId
    val mealId = remember(mealJson) {
        when (mealJson) {
            is MealInput.MealJson -> {
                try {
                    val meal = Gson().fromJson(mealJson.json, Meal::class.java)
                    Log.d("MealDetailsScreen", "Parsed MealJson: $meal")
                    cleanMealId(meal?.id?.toString())
                } catch (e: Exception) {
                    Log.e("MealDetailsScreen", "Failed to parse MealJson: ${mealJson.json}", e)
                    null
                }
            }
            is MealInput.MealId -> {
                Log.d("MealDetailsScreen", "Received MealId: ${mealJson.id}")
                cleanMealId(mealJson.id)
            }
            null -> {
                Log.w("MealDetailsScreen", "mealJson is null")
                null
            }
        }
    }

    // Log the mealId to verify its value
    Log.d("MealDetailsScreen", "mealId: '$mealId'")

    // Trigger API call only if mealId is valid
    LaunchedEffect(mealId) {
        if (mealDetailsState.MealDetailsData == null && !mealDetailsState.isLoading && mealId != null) {
            val mealIdInt = mealId.toIntOrNull()
            Log.d("MealDetailsScreen", "Attempting to parse mealId: '$mealId' -> $mealIdInt")
            if (mealIdInt != null) {
                mealDetailsViewModel.handleIntents(
                    MealDetialsIntents.showMealDetails(
                        branchId = mealDetailsViewModel.tokenManager.getBranchId() ?: 0,
                        mealId = mealIdInt
                    )
                )
            } else {
                Log.e("MealDetailsScreen", "Invalid mealId: '$mealId' cannot be converted to integer")
                Toast.makeText(context, "Invalid meal ID", Toast.LENGTH_SHORT).show()
            }
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
                    val choice = meal.choices.find { it.id == choiceId } ?: return@MealDetailsContent
                    val max = choice.max
                    selectedSubChoices = selectedSubChoices.toMutableMap().apply {
                        val currentList = getOrPut(choiceId) { mutableListOf() }.toMutableList()
                        if (isSelected) {
                            if (max > 0 && currentList.size >= max) {
                                if (max == 1 && !currentList.contains(subChoiceId)) {
                                    currentList.clear()
                                    currentList.add(subChoiceId)
                                } else {
                                    Toast.makeText(context, context.getString(R.string.validation_error_max_choices, max), Toast.LENGTH_SHORT).show()
                                    return@apply
                                }
                            } else if (!currentList.contains(subChoiceId)) {
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
                    if (mealId != null) {
                        if(mealDetailsViewModel.tokenManager.getUserData()?.authenticated=="authenticated"){
                            mealDetailsViewModel.handleIntents(
                                MealDetialsIntents.addMealToCart(
                                    mealId = mealId,
                                    quantity = mealDetailsState.quantity,
                                    sizeId = selectedSizeId,
                                    branchId = mealDetailsViewModel.tokenManager.getBranchId().toString(),
                                    choices = selectedSubChoices,
                                    pickupStatus = mealDetailsViewModel.tokenManager.getStatus().toString()
                                )
                            )
                        }else{
                            mealDetailsViewModel.showAuthDialoge.value =true

                        }

                    } else {
                        Log.e("MealDetailsScreen", "Cannot add to cart: mealId is null")
                        Toast.makeText(context, "Invalid meal ID", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            if(showAuthDialoge){
                ShowAuthentaionDialge(onNavToLogin = {
                    navController.navigate(Screen.LoginInScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                    mealDetailsViewModel.showAuthDialoge.value =false

                }, onCancel = {
                    mealDetailsViewModel.showAuthDialoge.value =false

                })
            }

        }

        // Show confirmation dialog when meal is added successfully
        if (addCartMessage.isNotEmpty() && addCartMessage == "Meal Added successfully") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { }
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    FoodCardDesign(
                        imageUrl = mealDetails?.imageUrl.orEmpty(),
                        onGoToCart = {
                            navController.navigate(Screen.CartScreen.route) {
                                popUpTo(Screen.MealDetailsScreen.route) { inclusive = true }
                            }
                        },
                        onGoToMenu = {  navController.navigate(Screen.MenueScreen.route) {
                            popUpTo(Screen.MealDetailsScreen.route) { inclusive = true }
                        } }
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
    val context = LocalContext.current
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
// Validate min and max choices
    val isValidSelection by remember(selectedSubChoices, meal) {
        derivedStateOf {
            meal?.choices?.all { choice ->
                val selectedCount = selectedSubChoices[choice.id]?.size ?: 0
                selectedCount >= choice.min && (choice.max == 0 || selectedCount <= choice.max)
            } ?: true
        }
    }
    var showValidationErrors by remember { mutableStateOf(false) }
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
                val selectedCount = selectedSubChoices[choice.id]?.size ?: 0
                val isChoiceValid = selectedCount >= choice.min && (choice.max == 0 || selectedCount <= choice.max)
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (showValidationErrors && !isChoiceValid) Color.Red else Color.Black),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
// Display choice title with min/max requirements
                        Text(
                            text = when {
                                choice.min == 0 -> {
                                    "${choice.choiceTitle} (" + stringResource(R.string.meal_details_fragment_choice_adapter_maximum_choose_text) + " 1 ) "
                                }
                                choice.min == choice.max  -> {
                                    "${choice.choiceTitle} (" + stringResource(R.string.meal_details_fragment_choice_adapter_must_choose_choose_text) + " ${choice.min})"
                                }
//                                choice.max == 0 -> {
//                                    "${choice.choiceTitle} (" + stringResource(R.string.meal_details_fragment_choice_adapter_minimum_choose_text) + " ${choice.min})"
//                                }
                                else -> {
                                    "${choice.choiceTitle} (" + stringResource(R.string.meal_details_fragment_choice_adapter_bracket_choose_text) + " ${choice.min})"
                                }
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedChoiceId == choice.id) Color.White else Color.Black
                        )
// Show validation message if selection is invalid and showValidationErrors is true
                        if (showValidationErrors && !isChoiceValid) {
                            Text(
                                text = when {
                                    selectedCount < choice.min -> stringResource(
                                        R.string.validation_error_min_choices,
                                        choice.min
                                    )
                                    selectedCount > choice.max -> stringResource(
                                        R.string.validation_error_max_choices,
                                        choice.max
                                    )
                                    else -> ""
                                },
                                color = Color.Red,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        if (choice.subChoices.isNotEmpty()) {
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                choice.subChoices.forEach { subChoice ->
                                    SubChoiceOption(
                                        subChoice = subChoice,
                                        choiceId = choice.id,
                                        min = choice.min,
                                        max = choice.max,
                                        isSelected = selectedSubChoices[choice.id]?.contains(subChoice.id) == true,
                                        onSelected = onSubChoiceSelected
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
                onClick = {
                    if (isValidSelection) {
                        showValidationErrors = false
                        onAddToCart()
                    } else {
                        showValidationErrors = true
                        Toast.makeText(
                            context,
                            context.getString(R.string.validation_error_general),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
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