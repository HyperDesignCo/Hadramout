package com.hyperdesign.myapplication.presentation.menu.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.CartMealEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.menu.mvi.CartIntents
import com.hyperdesign.myapplication.presentation.menu.mvi.CartViewModel
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.CartItem
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.PromoCodeInput
import org.koin.androidx.compose.koinViewModel
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.CartBottomBar
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.SwipeToDismissCartItem

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val cartMealState by cartViewModel.cartState.collectAsStateWithLifecycle()

    var cartMeals by remember { mutableStateOf(listOf<CartMealEntity>()) }

    LaunchedEffect(Unit) {
        cartViewModel.handleIntent(CartIntents.GetCart(2))
    }

    LaunchedEffect(cartMealState) {
        cartMeals = cartMealState.showCartDate?.cart?.cartMeals.orEmpty()
        Log.d("CartScreen", "Updated cartMeals: $cartMeals")
    }

    Log.d("CartScreen", "cartMealState: $cartMealState")

    CartScreenContent(
        onBackPressesd = { navController.popBackStack() },
        cartMeals = cartMeals,
        deliveryPrice = cartMealState.showCartDate?.cart?.deliveryCost?.toString() ?: "0.00",
        totalItems = cartMealState.showCartDate?.cart?.primaryPrice?.toString() ?: "0", // Placeholder for total items
        totalPrice = cartMealState.showCartDate?.cart?.totalPrice?.toString() ?: "0.00" // Placeholder for total price
    )
}

@Composable
fun CartScreenContent(
    onBackPressesd: () -> Unit,
    cartMeals: List<CartMealEntity>,
    totalItems: String,
    totalPrice: String,
    deliveryPrice: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainHeader(
            showTitle = true,
            height = 90,
            showBackPressedIcon = true,
            title = stringResource(R.string.cart),
            onBackPressesd = { onBackPressesd() },
            onCartPressed = {}
        )

        // Scrollable content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(5.dp))
            }
            items(cartMeals, key = { cart -> cart.id }) { cartMeal ->
                SwipeToDismissCartItem(
                    cartMeal = cartMeal,
                    onDelete = {
//                        onDeleteItem(cartMeal)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.promo_code),
                    color = Secondry,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                PromoCodeInput()
            }
        }

        CartBottomBar(
            priceItems = totalItems,
            deliveryPrice = deliveryPrice,
            totalPrice = totalPrice,
            onPayClick = { /* Handle payment action */ }
        )

    }
}