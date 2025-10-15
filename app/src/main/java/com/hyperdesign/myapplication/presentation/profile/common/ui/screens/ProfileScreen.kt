package com.hyperdesign.myapplication.presentation.profile.common.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.profile.common.ui.Widgets.ProfileHeader
import com.hyperdesign.myapplication.presentation.profile.common.ui.Widgets.SettingItem


@Composable
fun ProfileScreen() {
    val navController = LocalNavController.current

    ProfileScreenCotent(onBackPressed = {
        navController.popBackStack()
    }, onGoToMyOrdersScreen = {
        navController.navigate(Screen.MyOrders.route)
    }, onGotToSettingsScreen = {
        navController.navigate(Screen.SettingsScreen.route)
    }, onGoToAllAddressesScreen = {navController.navigate(Screen.AllAddressesScreen.route)}
    , onGoToCartScreen = {navController.navigate(Screen.CartScreen.route)}
    )
}


@Composable
fun ProfileScreenCotent(onGoToCartScreen:()->Unit,onBackPressed: () -> Unit,onGoToAllAddressesScreen:()->Unit ,onGoToMyOrdersScreen:()->Unit,onGotToSettingsScreen:()->Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ProfileHeader(onBackPressed = {
            onBackPressed()

        })

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            SettingItem(
                title = stringResource(R.string.my_orders),
                icon = R.drawable.ic_my_order,

                onClick = { onGoToMyOrdersScreen() }
            )

            SettingItem(
                title = stringResource(R.string.settings),
                icon = R.drawable.setting,
                onClick = { onGotToSettingsScreen() }
            )

            SettingItem(
                title = stringResource(R.string.Saved_addresses),
                icon = R.drawable.shop_location,
                onClick = { onGoToAllAddressesScreen() }
            )

            SettingItem(
                title = stringResource(R.string.Cart),
                icon = R.drawable.bxs_cart,
                onClick = { onGoToCartScreen() }
            )

//            SettingItem(
//                title = stringResource(R.string.pick_up),
//                icon = R.drawable.ic_pickup,
//                onClick = { /* Handle Logout Click */ }
//            )
//
//            SettingItem(
//                title = stringResource(R.string.Delivery),
//                icon = R.drawable.delivery,
//                onClick = { /* Handle Logout Click */ }
//            )

            SettingItem(
                title = stringResource(R.string.Call_support),
                icon = R.drawable.call,
                description = "19527",
                onClick = { /* Handle Logout Click */ }
            )


        }
    }

}