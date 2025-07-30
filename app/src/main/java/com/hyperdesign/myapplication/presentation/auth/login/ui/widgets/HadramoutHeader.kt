package com.hyperdesign.myapplication.presentation.auth.login.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hyperdesign.myapplication.R

@Composable
fun HadramoutHeader() {
    Box(modifier = Modifier.padding(top = 40.dp).fillMaxWidth().height(200.dp)){
        Image(
            painter = painterResource(id = R.drawable.hadramout_header),
            contentDescription = "Hadramout Header",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )



    }



}