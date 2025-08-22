package com.hyperdesign.myapplication.presentation.auth.login.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R

@Composable
fun HadramoutHeader(title:String?=null) {
    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {

        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Hadramout Header",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Image(
                painter = painterResource(id = R.drawable.hadramout_logo),
                contentDescription = "Hadramout Header",
                modifier = Modifier.height(120.dp).width(120.dp),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            )

            if (title != null){
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = title,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,)
            }
//            Spacer(modifier = Modifier.height(10.dp))
//            Text(text = title?:"",
//                color = Color.White,
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center,
//                fontWeight = FontWeight.Bold,
//                fontSize = 18.sp,)
        }



    }



}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HadramoutHeaderPreview() {
    HadramoutHeader()
}