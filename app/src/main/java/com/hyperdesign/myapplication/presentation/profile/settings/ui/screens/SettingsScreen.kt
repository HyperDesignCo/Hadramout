package com.hyperdesign.myapplication.presentation.profile.settings.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.profile.common.ui.Widgets.SettingItem

@Composable
fun SettingsScreen(){
    val navController = LocalNavController.current
    SettingsScreenContent(onBackPressed = {
        navController.popBackStack()
    })
}


@Composable
fun SettingsScreenContent(onBackPressed:()->Unit){

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
            item {
                Spacer(modifier = Modifier.height(15.dp))

            }

            item{
                SettingItem(title = stringResource(R.string.who_we_are), onClick = {})

            }

            item {
                SettingItem(title = stringResource(R.string.contact_us), onClick = {})

            }

            item {
                SettingItem(title = stringResource(R.string.share_app), onClick = {})

            }

            item {
                SettingItem(title = stringResource(R.string.languages), onClick = {})

            }

            item {
                SettingItem(title = stringResource(R.string.return_policy), onClick = {})
            }

            item {
                SettingItem(title = stringResource(R.string.terms_and_conditions), onClick = {})

            }

            item {
                SettingItem(title = stringResource(R.string.privacy_policy), onClick = {})

            }

            item {
                SettingItem(title = stringResource(R.string.log_out), onClick = {})

            }

            item {
                SettingItem(title = stringResource(R.string.delete_account), onClick = {})

            }







        }
    }

}