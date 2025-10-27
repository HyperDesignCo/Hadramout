package com.hyperdesign.myapplication.presentation.profile.common.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.common.wedgits.ShowAuthentaionDialge
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.profile.common.mvi.ProfileViewModel
import com.hyperdesign.myapplication.presentation.profile.common.ui.Widgets.ProfileHeader
import com.hyperdesign.myapplication.presentation.profile.common.ui.Widgets.SettingItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen() {
    val navController = LocalNavController.current
    val userProfileViewModel: ProfileViewModel = koinViewModel()
    val showAuthDialoge by userProfileViewModel.showAuthDialoge

    ProfileScreenContent(
        onBackPressed = {
            navController.popBackStack()
        },
        onGoToMyOrdersScreen = {
            if (userProfileViewModel.tokenManager.getUserData()?.authenticated == "authenticated") {
                navController.navigate(Screen.MyOrders.route)
            } else {
                userProfileViewModel.showAuthDialoge.value = true
            }
        },
        onGotToSettingsScreen = {
            navController.navigate(Screen.SettingsScreen.route)
        },
        onGoToAllAddressesScreen = {
            if (userProfileViewModel.tokenManager.getUserData()?.authenticated == "authenticated") {
                navController.navigate(Screen.AllAddressesScreen.route)
            } else {
                userProfileViewModel.showAuthDialoge.value = true
            }
        },
        hotline = userProfileViewModel.tokenManager.getHomeHotline().toString(),
        onGoToCartScreen = {
            if (userProfileViewModel.tokenManager.getUserData()?.authenticated == "authenticated") {
                navController.navigate(Screen.CartScreen.route)
            } else {
                userProfileViewModel.showAuthDialoge.value = true
            }
        },
        onGotUserProfileScren = {
            if (userProfileViewModel.tokenManager.getUserData()?.authenticated == "authenticated") {
                navController.navigate(Screen.UserProfileScreen.route)
            } else {
                userProfileViewModel.showAuthDialoge.value = true
            }
        }
    )

    if (showAuthDialoge) {
        ShowAuthentaionDialge(
            onNavToLogin = {
                navController.navigate(Screen.LoginInScreen.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                userProfileViewModel.showAuthDialoge.value = false
            },
            onCancel = {
                userProfileViewModel.showAuthDialoge.value = false
            }
        )
    }
}

@Composable
fun ProfileScreenContent(
    onGotUserProfileScren: () -> Unit,
    hotline: String,
    onGoToCartScreen: () -> Unit,
    onBackPressed: () -> Unit,
    onGoToAllAddressesScreen: () -> Unit,
    onGoToMyOrdersScreen: () -> Unit,
    onGotToSettingsScreen: () -> Unit
) {
    val context = LocalContext.current

    fun makePhoneCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PermissionChecker.PERMISSION_GRANTED
        ) {
            context.startActivity(intent)
        } else {
            // Optionally, request permission or open dialer instead
            val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            context.startActivity(dialIntent)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ProfileHeader(
            onBackPressed = {
                onBackPressed()
            }
        )

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
                title = stringResource(R.string.Cart),
                icon = R.drawable.bxs_cart,
                onClick = { onGoToCartScreen() }
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
                title = stringResource(R.string.update_your_profile),
                icon = R.drawable.user_profile_edit,
                onClick = { onGotUserProfileScren() }
            )

            SettingItem(
                title = stringResource(R.string.Call_support),
                icon = R.drawable.call,
                description = hotline,
                onClick = { makePhoneCall(hotline) }
            )
        }
    }
}