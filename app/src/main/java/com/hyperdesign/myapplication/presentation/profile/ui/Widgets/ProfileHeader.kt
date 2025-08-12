package com.hyperdesign.myapplication.presentation.profile.ui.Widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R

@Composable
fun ProfileHeader(
    onBackPressed: () -> Unit = { /* Default no-op */ }
) {


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
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
                        imageVector = Icons.Default.ArrowBack,
                        tint = Color.White,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }

            Row(
                modifier = Modifier.padding(start = 6.dp).fillMaxSize(),
//                horizontalArrangement =Arrangement.,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(50.dp)
                        ,
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "Abdallah Alsayed",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "01151828780",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold

                    )
                }



            }

        }

    }

}

//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun ProfileHeaderPreview() {
//    ProfileHeader(onBackPressed = {})
//}