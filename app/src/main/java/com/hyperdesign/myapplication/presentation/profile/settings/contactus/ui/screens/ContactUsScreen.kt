package com.hyperdesign.myapplication.presentation.profile.settings.contactus.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.AboutEntity
import com.hyperdesign.myapplication.domain.Entity.SettingEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.profile.settings.whoarewe.mvi.WhoAreWeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ContactUsScreen(aboutUsViewModel: WhoAreWeViewModel= koinViewModel()){
    val navController = LocalNavController.current

    val aboutUsState by aboutUsViewModel.aboutUsState.collectAsStateWithLifecycle()


    var settingState by remember { mutableStateOf<SettingEntity?>(null) }
    LaunchedEffect(aboutUsState) {
        settingState =aboutUsState.aboutUsResponse?.setting

    }
    settingState?.let {
        ContactUsScreenContent(onBackPressed = {
            navController.popBackStack()
        }, setting = it)
    }
}




@Composable
fun ContactUsScreenContent(setting: SettingEntity,onBackPressed:()->Unit){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainHeader(
            onBackPressesd = { onBackPressed() },
            showTitle = true,
            title = stringResource(R.string.contact_us),
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
                Row {
                    Image(painter = painterResource(R.drawable.phone), modifier = Modifier.size(23.dp),contentDescription = "phone")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(stringResource(R.string.phone_number), color = Color.Black, fontWeight = FontWeight.Bold,fontSize = 16.sp)
                }
            }
            item {
                Text(setting.phone2, color = Color.LightGray,fontSize = 14.sp)
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Image(painter = painterResource(R.drawable.link), modifier = Modifier.size(23.dp), contentDescription = "link")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(stringResource(R.string.email_address), color = Color.Black, fontWeight = FontWeight.Bold,fontSize = 16.sp)
                }
            }

            item {
                Text(setting.contactEmail, color =  Color.LightGray,fontSize = 14.sp)
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Image(painter = painterResource(R.drawable.facebook), modifier = Modifier.size(23.dp), contentDescription = "facebook")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(stringResource(R.string.faceBook_page), color = Color.Black, fontWeight = FontWeight.Bold,fontSize = 16.sp)
                }
            }

            item {
                Text(setting.youtube, color =  Color.LightGray,fontSize = 14.sp)
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Image(painter = painterResource(R.drawable.instagram), modifier = Modifier.size(23.dp), contentDescription = "instagram")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(stringResource(R.string.insta_page), color = Color.Black, fontWeight = FontWeight.Bold,fontSize = 16.sp)
                }
            }

            item {
                Text(setting.instgram, color =  Color.LightGray,fontSize = 14.sp)
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(setting.hotline)
                            .crossfade(true)
                            .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                            .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                            .build(),
                        contentDescription = "hotline",
                        modifier = Modifier
                            .fillMaxWidth()
                            ,
                        contentScale = ContentScale.Fit
                    )
                }
            }



        }

    }
}