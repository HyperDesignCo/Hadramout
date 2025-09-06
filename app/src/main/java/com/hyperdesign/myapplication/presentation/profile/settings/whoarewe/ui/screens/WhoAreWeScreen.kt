package com.hyperdesign.myapplication.presentation.profile.settings.whoarewe.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.AboutEntity
import com.hyperdesign.myapplication.domain.Entity.AboutUsResponseEntity
import com.hyperdesign.myapplication.domain.Entity.SettingEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.profile.settings.whoarewe.mvi.WhoAreWeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun WhoAreWeScreen(aboutUsViewModel: WhoAreWeViewModel= koinViewModel()){

    val aboutUsState by aboutUsViewModel.aboutUsState.collectAsStateWithLifecycle()

    var aboutState by remember { mutableStateOf<AboutEntity?>(null) }

    var settingState by remember { mutableStateOf<SettingEntity?>(null) }
    LaunchedEffect(aboutUsState) {
        aboutState = aboutUsState.aboutUsResponse?.about
        settingState =aboutUsState.aboutUsResponse?.setting

    }
    Log.d("WhoAreWeScreen",aboutState.toString())
    Log.d("WhoAreWeScreen",settingState.toString())

    val navController = LocalNavController.current
    aboutUsState.aboutUsResponse?.let {
        aboutState?.let { setting ->
            settingState?.let { onBackPressed ->
                WhoAreWeScreenContent(onBackPressed = {
                    navController.popBackStack()
                }, about = setting,
                    setting = onBackPressed
                )
            }
        }
    }

}

@Composable
fun WhoAreWeScreenContent(about: AboutEntity, setting: SettingEntity, onBackPressed:()->Unit){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            MainHeader(
                onBackPressesd = { onBackPressed() },
                showTitle = true,
                title = stringResource(R.string.who_we_are),
                showBackPressedIcon = true,
                height = 90
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                }
                item {

                    Text(AnnotatedString.fromHtml(about.aboutUs))


                }
            }

        }

}