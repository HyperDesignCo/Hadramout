package com.hyperdesign.myapplication.presentation.menu.ui.screens

import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.CartMealEntity
import com.hyperdesign.myapplication.domain.Entity.SellingMealEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.navcontroller.goToScreenMealDeataisWithString
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.menu.mvi.CartIntents
import com.hyperdesign.myapplication.presentation.menu.mvi.CartViewModel
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.CartBottomBar
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.PromoCodeInput
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.SwipeToDismissCartItem
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.UpSellingComponent
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val cartMealState by cartViewModel.cartState.collectAsStateWithLifecycle()

    var cartMeals by remember { mutableStateOf(listOf<CartMealEntity>()) }
    var cartId by remember { mutableStateOf("") }
    var deliveryStatus by remember { mutableStateOf(false) }

    var isBottomBarExpanded by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    Log.d("deliveryStatus", cartMealState.showCartDate?.cart?.pickUpStatus.toString())
    Log.d("deliveryStatus", deliveryStatus.toString())

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }
            .distinctUntilChanged()
            .collect { (index, offset) ->
                if (index > 0 || offset > 100) {
                    isBottomBarExpanded = true
                } else if (index == 0 && offset < 50) {
                    isBottomBarExpanded = false
                }
            }
    }

    LaunchedEffect(Unit) {
        cartViewModel.handleIntent(CartIntents.GetCart(cartViewModel.tokenManager.getBranchId() ?: 0))
    }

    LaunchedEffect(cartMealState) {
        cartMeals = cartMealState.showCartDate?.cart?.cartMeals.orEmpty()
        cartId = cartMealState.showCartDate?.cart?.id.orEmpty()
        deliveryStatus = if (cartMealState.showCartDate?.cart?.pickUpStatus == "0") true else false
    }

    Log.d("CartScreen", "Updated cartMeals: $cartMeals")

    Box(modifier = Modifier.fillMaxSize()) {
        if (cartMeals.isEmpty() && !cartMealState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.shopping_cart),
                        contentDescription = "shopping Cart"
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.your_cart_is_empty),
                        color = Secondry,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
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
                onChangeQuantity = { quantity ->
                    cartViewModel.handleIntent(CartIntents.OnChangeQuantity(quantity))
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
                        CartIntents.deleteCartItem(cardId, cartItemId)
                    )
                },
                onGoToCheckOutScreen = {
                    navController.navigate("${Screen.CheckOutScreen.route}?deliveryTime=${cartMealState.showCartDate?.deliveryTime.orEmpty()}")
                },
                deliveryPrice = cartMealState.showCartDate?.cart?.deliveryCost?.toString() ?: "0.00",
                totalItems = cartMealState.showCartDate?.cart?.primaryPrice?.toString() ?: "0",
                totalPrice = cartMealState.showCartDate?.cart?.totalPrice?.toString() ?: "0.00",
                pickUpStatus = deliveryStatus,
                deliveryTime = cartMealState.showCartDate?.deliveryTime.orEmpty(),
                upSellingMeals = cartMealState.showCartDate?.upSellingMeal.orEmpty(),
                onNavToMealDetails = { mealId ->
                    val route = goToScreenMealDeataisWithString(mealId)
                    navController.navigate(route)
                },
                listState = listState,
                isBottomBarExpanded = isBottomBarExpanded
            )
        }
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
    upSellingMeals: List<SellingMealEntity>,
    totalItems: String,
    totalPrice: String,
    copoun: String,
    copounMessage: String? = null,
    onCopounChange: (String) -> Unit,
    onChangeQuantity: (String) -> Unit,
    onDeleteItem: (String, String) -> Unit,
    onIncreaseQuantity: (String, String) -> Unit,
    onDecreaseQuantity: (String, String) -> Unit,
    deliveryPrice: String,
    cartId: String,
    onCheckCoponClick: () -> Unit,
    onGoToCheckOutScreen: () -> Unit,
    pickUpStatus: Boolean,
    deliveryTime: String,
    onNavToMealDetails: (String) -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState,
    isBottomBarExpanded: Boolean
) {
    val context = LocalContext.current
    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

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

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(5.dp))
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.cart_meals),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            items(cartMeals, key = { cart -> cart.id }) { cartMeal ->
                SwipeToDismissCartItem(
                    cartMeal = cartMeal,
                    onDelete = {
                        onDeleteItem(cartId, cartMeal.id)
                    },
                    onDecrease = {
                        onChangeQuantity(cartMeal.quantity.toString())
                        onDecreaseQuantity(cartId, cartMeal.id)
                    },
                    onIncrease = {
                        onChangeQuantity(cartMeal.quantity.toString())
                        onIncreaseQuantity(cartId, cartMeal.id)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (upSellingMeals.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(R.string.you_can_order_also),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
                items(upSellingMeals, key = { meal -> meal.id }) { meal ->
                    UpSellingComponent(meal) {
                        onNavToMealDetails(meal.id)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(240.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            // Swipe instruction with GIF
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(if (isRtl) R.drawable.drag_left else R.drawable.drag_right)
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = "Swipe instruction",
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.swipe_to_delete),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.promo_code),
                color = Secondry,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            PromoCodeInput(
                onClickCoponCkeck = onCheckCoponClick,
                copoun = copoun,
                onCopounChange = onCopounChange,
                copounMessage = copounMessage
            )
        }

        AnimatedVisibility(
            visible = isBottomBarExpanded,
            enter = expandVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
            exit = shrinkVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.price),
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = totalItems,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Secondry
                    )
                }

                if (pickUpStatus) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.delivery),
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = deliveryPrice,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondry
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                if (pickUpStatus) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.delivery_time),
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = stringResource(R.string.minutes, deliveryTime),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondry
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        CartBottomBar(
            priceItems = totalItems,
            deliveryPrice = deliveryPrice,
            totalPrice = totalPrice,
            buttonText = stringResource(R.string.complete_order),
            onPayClick = { onGoToCheckOutScreen() },
            pickUpStatus = pickUpStatus,
            deliveryTime = deliveryTime
        )
    }
}