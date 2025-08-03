package com.hyperdesign.myapplication.presentation.common.wedgits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun MainHeader(
    title: String,
    onBackPressesd : () -> Unit

) {
        Row(modifier = Modifier.padding(top = 40.dp).fillMaxWidth().height(140.dp).background(color = Secondry)) {

            IconButton(
                onClick = onBackPressesd,
                modifier = Modifier.padding(start = 5.dp, top = 30.dp).size(30.dp)
            ) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                    tint = Color.White ,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )



            }



            Text(
                text = title,
                color = Color.White,
                modifier = Modifier.padding(top = 35.dp).fillMaxWidth(),
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            }



}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MainHeaderPreview() {
    MainHeader(
        title = "Main Header",
        onBackPressesd = {}
    )
}