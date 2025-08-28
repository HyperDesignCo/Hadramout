package com.hyperdesign.myapplication.presentation.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.home.ui.screens.HomeScreen
import com.hyperdesign.myapplication.presentation.main.navcontroller.AppNavigation
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.HadramoutTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            HadramoutTheme {

                var showSplash by remember { mutableStateOf(true) }
                if (showSplash) {
                    CustomSplashScreen { showSplash = false }
                } else {
//                    HomeScreen()
                    AppNavigation(startDestination = Screen.LoginInScreen.route)
                }


            }
        }
    }
}


@Composable
fun CustomSplashScreen(onTimeout: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Image(
            painter = painterResource(id = R.drawable.hadramout_logo),
            contentDescription = "Hadramout Logo",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
        )
    }
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }
}
