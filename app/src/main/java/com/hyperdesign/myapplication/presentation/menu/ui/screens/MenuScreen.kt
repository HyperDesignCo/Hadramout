package com.hyperdesign.myapplication.presentation.menu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.presentation.home.ui.screens.HomeScreen
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.MenuHeader
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.OffersScreen

@Composable
fun MenuScreen() {
    val navController = LocalNavController.current
    MenuScreenContent(onBackPresed = {
        navController.popBackStack()

    })
}

@Composable
fun MenuScreenContent(onBackPresed:()->Unit) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MenuHeader(
            onBackPressed = {onBackPresed()},
            onCartPressed = {},
            cardCount = "22",
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                selectedTab = tab
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            when (selectedTab) {
                0 -> OffersScreen()
                1 -> Text(
                    text = "Chickens Content",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                2 -> Text(
                    text = "Meat Content",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                3 -> Text(
                    text = "Charcoal Grills Content",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}