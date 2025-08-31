package com.hyperdesign.myapplication.presentation.menu.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    var cartId by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        cartViewModel.handleIntent(CartIntents.GetCart(2))
    }

    LaunchedEffect(cartMealState) {
        cartMeals = cartMealState.showCartDate?.cart?.cartMeals.orEmpty()
        cartId = cartMealState.showCartDate?.cart?.id.orEmpty()
        Log.d("CartScreen", "Updated cartMeals: $cartMeals")
    }

    Log.d("CartScreen", "cartMealState: ${cartMealState.quantity}")

    Box(modifier = Modifier.fillMaxSize()) {
        CartScreenContent(
            onBackPressesd = { navController.popBackStack() },
            cartMeals = cartMeals,
            cartId = cartId,
            copoun = cartMealState.copoun,
            onCheckCoponClick = {
                cartViewModel.handleIntent(
                    CartIntents.OnCkeckCopounClick(
                        cartId = cartId,
                        promoCode = cartMealState.copoun
                    )
                )
            },
            onCopounChange = { cartViewModel.handleIntent(CartIntents.OnChangeCopounText(it)) },
            copounMessage = cartMealState.copounMessage,
            onChangeQuantity = { quntity ->
                cartViewModel.handleIntent(
                    CartIntents.OnChangeQuantity(
                        quntity
                    )
                )
            },
            onDecreaseQuantity = { cardId, cartItemId ->
                cartViewModel.handleIntent(
                    CartIntents.DecreaseCartItemQuantity(
                        cardId,
                        cartItemId,
                        cartMealState.quantity
                    )
                )
            },
            onIncreaseQuantity = { cardId, cartItemId ->
                cartViewModel.handleIntent(
                    CartIntents.IncreaseCartItemQuantity(
                        cardId,
                        cartItemId,
                        cartMealState.quantity
                    )
                )
            },
            onDeleteItem = { cardId, cartItemId ->
                cartViewModel.handleIntent(
                    CartIntents.deleteCartItem(
                        cardId,
                        cartItemId
                    )
                )
            },
            deliveryPrice = cartMealState.showCartDate?.cart?.deliveryCost?.toString() ?: "0.00",
            totalItems = cartMealState.showCartDate?.cart?.primaryPrice?.toString()
                ?: "0", // Placeholder for total items
            totalPrice = cartMealState.showCartDate?.cart?.totalPrice?.toString()
                ?: "0.00" // Placeholder for total price

        )
        if (cartMealState.isLoading) {
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
}

@Composable
fun CartScreenContent(
    onBackPressesd: () -> Unit,
    cartMeals: List<CartMealEntity>,
    totalItems: String,
    totalPrice: String,
    copoun:String,
    copounMessage:String?=null,
    onCopounChange:(String)->Unit,
    onChangeQuantity:(String)->Unit,
    onDeleteItem:(String,String)->Unit,
    onIncreaseQuantity:(String,String)->Unit,
    onDecreaseQuantity:(String,String)->Unit,
    deliveryPrice: String,
    cartId:String,
    onCheckCoponClick:()->Unit
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
                        onDeleteItem(cartId,cartMeal.id)
                    },
                    onDecrease = {
                        onChangeQuantity(cartMeal.quantity.toString())
                        onDecreaseQuantity(cartId,cartMeal.id)
                                 },
                    onIncrease = {
                        onChangeQuantity(cartMeal.quantity.toString())
                        onIncreaseQuantity(cartId,cartMeal.id)
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
                PromoCodeInput(onClickCoponCkeck = onCheckCoponClick,copoun = copoun,onCopounChange = onCopounChange, copounMessage = copounMessage)
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