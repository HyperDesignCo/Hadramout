package com.hyperdesign.myapplication.presentation.auth.forgotpassword.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R

@Composable
fun ForgetPasswordHeader(title: String?=null,body:String?=null, onBackPressesd: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop

        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = onBackPressesd,
                modifier = Modifier
                    .padding(start = 5.dp, top = 30.dp)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }



        }

        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.hadramout_logo),
                contentDescription = "Hadramout Logo",
                modifier = Modifier
                    .size(100.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text =title?:"",
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )


            Text(
                text = body?:"",
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 17.sp,
            )

        }

    }



}


@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ForgetPasswordHeaderPreview() {
    ForgetPasswordHeader(onBackPressesd = {})
}