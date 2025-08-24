package com.hyperdesign.myapplication.presentation.common.wedgits

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun MainHeader(
    modifier: Modifier= Modifier,
    title: String,
    showBackPressedIcon: Boolean = false,
    onBackPressesd: () -> Unit,
    onCartPressed: () -> Unit = {},
    showIcon: Boolean = false,
    cardCount: String? = "7",
    showLogo: Boolean = false,
    showTitle: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.menueheader),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) {
            if (showBackPressedIcon) {
                IconButton(
                    onClick = onBackPressesd,
                    modifier = Modifier
                        .padding(start = 5.dp, top = 30.dp)
                        .size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        tint = Color.White,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(30.dp))
            }

            if (showTitle) {
                Text(
                    text = title,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 35.dp)
                        .fillMaxWidth(0.8f),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            if (showIcon) {
                Box(
                    modifier = Modifier
                        .padding(top = 26.dp, end = 10.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 27.dp, end = 3.dp)
                            .size(17.dp)
                            .background(color = Color.Red, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cardCount ?: "",
                            modifier = Modifier
                                .padding(bottom = 2.dp)
                                .fillMaxSize(),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onCartPressed,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.cart),
                            tint = Color.LightGray,
                            contentDescription = "cart",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.width(30.dp))
            }
        }

        if (showLogo) {
            Image(
                painter = painterResource(R.drawable.hadramout_logo),
                contentDescription = "Hadramout Logo",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(100.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MainHeaderPreview() {
    MainHeader(
        title = "",
        onBackPressesd = {},
        showIcon = true,
        showLogo = true,
        showBackPressedIcon = true
    )
}