package com.hyperdesign.myapplication.presentation.home.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hyperdesign.myapplication.presentation.auth.login.ui.widgets.HadramoutHeader
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController

@Composable
fun HomeScreen() {

    val navController = LocalNavController.current
    HomeScreenContent(onBackPressed = {
//        navController.popBackStack()

    })

}



@Composable
fun HomeScreenContent(onBackPressed: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        MainHeader(
            title = "",
            onBackPressesd = {
                onBackPressed()
            },
            showIcon = true,
            cardCount = "2",
            onCartPressed = {}
        )

        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(5.dp))

        }
    }
}