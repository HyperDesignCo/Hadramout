package com.hyperdesign.myapplication.presentation.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hyperdesign.myapplication.BuildConfig
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.main.mvi.AuthViewModel
import com.hyperdesign.myapplication.presentation.main.navcontroller.AppNavigation
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.theme.ui.HadramoutTheme
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.profile.settings.common.mvi.SettingViewModel
import com.hyperdesign.myapplication.presentation.profile.settings.common.ui.screens.updateLocale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModel()

    private val settingViewModel: SettingViewModel by viewModel()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        

        val selectedLanguge = settingViewModel.getLanguage()

        Log.d("MainActivity",selectedLanguge)
        updateLocale(context = this,selectedLanguge)
        Log.d("map_api_key", BuildConfig.MAPS_API_KEY)
        lifecycleScope.launch {
            setContent {
                rememberSystemUiController().setStatusBarColor(
                    color = Secondry,
                    darkIcons = false
                )
                HadramoutTheme (
                    darkTheme = false,
                    dynamicColor = false
                ){

                    var destination by remember { mutableStateOf<String?>(null) }

                    var showSplash by remember { mutableStateOf(true) }
                    if (showSplash) {

                        LaunchedEffect(Unit) {
                            destination = authViewModel.checkUserState()
                        }
                        CustomSplashScreen(onTimeout = {
                            if (destination != null) {
                                showSplash = false
                            } else {
                                showSplash = true
                            }

                        }
                        )
                    } else {
//                    HomeScreen()
                        if (destination == null) {
//
                        } else {
                            val startDestination = when (destination) {
                                "home" -> {
                                    Screen.HomeScreen.route
                                }

                                "login" -> Screen.LoginInScreen.route
//                                "status" -> ScreenRoutes.SignUpScreenStep4.route
//                                "verify" -> ScreenRoutes.OtpScreen.route
                                else -> Screen.LoginInScreen.route
                            }

                            AppNavigation(startDestination = startDestination)
                        }


                    }
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



