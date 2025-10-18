package com.hyperdesign.myapplication.presentation.profile.settings.common.ui.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.common.wedgits.ShowAuthentaionDialge
import com.hyperdesign.myapplication.presentation.main.MainActivity
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.profile.common.ui.Widgets.SettingItem
import com.hyperdesign.myapplication.presentation.profile.settings.common.mvi.SettingViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun SettingsScreen(settingViewModel: SettingViewModel = koinViewModel()) {
    val state by settingViewModel.settingsState.collectAsStateWithLifecycle()
    var selectedLanguage by remember { mutableStateOf(settingViewModel.getLanguage() ?: "en") }
    Log.d("SettingsScreen", selectedLanguage)
    val navController = LocalNavController.current

    SettingsScreenContent(
        onBackPressed = { navController.popBackStack() },
        onGoToWhoAreWeScreen = { navController.navigate(Screen.WhoAreWeScreen.route) },
        onGoToContactUsScreenScreen = { navController.navigate(Screen.ContactUsScreen.route) },
        onSaveNewLanguage = { settingViewModel.setLanguage(it) },
        selectedLanguage = selectedLanguage,
        onLogOut = {
            if (settingViewModel.tokenManager.getUserData()?.authenticated == "authenticated") {
                settingViewModel.logOut()
                navController.navigate(Screen.LoginInScreen.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

            }else{
                navController.navigate(Screen.LoginInScreen.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }


            }


        },
        onGoToReturnPolicyScreen = {navController.navigate(Screen.ReturnPolicyScreen.route)},
        onGoToTermsAndConditionsScreen = {navController.navigate(Screen.TermesAndConditionsScreen.route)},
        onGoToPrivacyPolicyScreen = {navController.navigate(Screen.PrivacyPolicyScreen.route)},
        authState = settingViewModel.tokenManager.getUserData()?.authenticated.toString()
    )


}

@Composable
fun SettingsScreenContent(
    onSaveNewLanguage: (String) -> Unit, // Updated to reflect return type
    selectedLanguage: String,
    onBackPressed: () -> Unit,
    authState:String,
    onGoToWhoAreWeScreen: () -> Unit,
    onGoToContactUsScreenScreen: () -> Unit,
    onGoToReturnPolicyScreen:()->Unit,
    onGoToPrivacyPolicyScreen:()->Unit,
    onGoToTermsAndConditionsScreen:()->Unit,
    onLogOut:()->Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainHeader(
            onBackPressesd = { onBackPressed() },
            showTitle = true,
            title = stringResource(R.string.settings),
            showBackPressedIcon = true,
            height = 90
        )

        LazyColumn(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            item { Spacer(modifier = Modifier.height(15.dp)) }

            item {
                SettingItem(title = stringResource(R.string.who_we_are), onClick = {
                    onGoToWhoAreWeScreen()
                })
            }

            item {
                SettingItem(title = stringResource(R.string.contact_us), onClick = {
                    onGoToContactUsScreenScreen()
                })
            }

            item {
                SettingItem(title = stringResource(R.string.share_app), onClick = {})
            }

            item {
                Column {
                    SettingItem(
                        title = stringResource(
                            R.string.languages,
                            if (selectedLanguage == "en") stringResource(R.string.english) else stringResource(R.string.arabic)
                        ),
                        onClick = { expanded = true }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = if (selectedLanguage == "en") stringResource(R.string.arabic) else stringResource(R.string.english),
                                    fontSize = 16.sp
                                )
                            },
                            onClick = {
                                val newLanguage = if (selectedLanguage == "en") "ar" else "en"
//                                onChangeLanguage(newLanguage)
                                onSaveNewLanguage(newLanguage)
                                updateLocale(context, newLanguage)
                                expanded = false
                                // Restart activity with current route preserved
                                context.startActivity(
                                    Intent(context, MainActivity::class.java).apply {
                                        putExtra("destination_route", Screen.SettingsScreen.route)
                                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    }
                                )
                            }
                        )
                    }
                }
            }

            item {
                SettingItem(title = stringResource(R.string.return_policy), onClick = {onGoToReturnPolicyScreen()})
            }

            item {
                SettingItem(title = stringResource(R.string.terms_and_conditions), onClick = {onGoToTermsAndConditionsScreen()})
            }

            item {
                SettingItem(title = stringResource(R.string.privacy_policy), onClick = {onGoToPrivacyPolicyScreen()})
            }

            item {
                SettingItem(title = if(authState=="authenticated")stringResource(R.string.log_out) else stringResource(R.string.login) , onClick = {onLogOut()})
            }

            item {
                SettingItem(title = stringResource(R.string.delete_account), onClick = {})
            }
        }
    }
}

fun updateLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}