package com.hyperdesign.myapplication.presentation.profile.common.ui.Widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.auth.login.mvi.LoginViewModel
import com.hyperdesign.myapplication.presentation.common.wedgits.ShowAuthentaionDialge
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.profile.common.mvi.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileHeader(
    onBackPressed: () -> Unit = { /* Default no-op */ },
    userProfileViewModel: ProfileViewModel= koinViewModel()
) {

    val showAuthDialoge by userProfileViewModel.showAuthDialoge



    val navController = LocalNavController.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable{
                if (userProfileViewModel.tokenManager.getUserData()?.authenticated == "authenticated") {
                    navController.navigate(Screen.UserProfileScreen.route)

                }else{
                    userProfileViewModel.showAuthDialoge.value =true


                }
            }

        ,
    ) {
        Image(
            painter = painterResource(id = R.drawable.menueheader),
            contentDescription = "header background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {

                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier
                        .padding(start = 5.dp, top = 10.dp)
                        .size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        tint = Color.White,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }

            Row(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .fillMaxSize(),
//                horizontalArrangement =Arrangement.,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(userProfileViewModel.tokenManager.getUserData()?.image)
                        .crossfade(true)
                        .error(R.drawable.test_food)
                        .placeholder(R.drawable.test_food)
                        .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                        .diskCachePolicy(coil.request.CachePolicy.ENABLED)
                        .build(),
                    contentDescription = "image",
                    modifier = Modifier
                        .padding(start = 10.dp, top = 10.dp)
                        .size(70.dp)
                        .clip(shape = CircleShape),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = userProfileViewModel.tokenManager.getUserData()?.name?: stringResource(R.string.visitor),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userProfileViewModel.tokenManager.getUserData()?.mobile.orEmpty(),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold

                    )
                }



            }

        }

        if (showAuthDialoge) {
            ShowAuthentaionDialge(onNavToLogin = {
                navController.navigate(Screen.LoginInScreen.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                userProfileViewModel.showAuthDialoge.value = false

            }, onCancel = {
                userProfileViewModel.showAuthDialoge.value = false

            })
        }

    }

}

//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun ProfileHeaderPreview() {
//    ProfileHeader(onBackPressed = {})
//}