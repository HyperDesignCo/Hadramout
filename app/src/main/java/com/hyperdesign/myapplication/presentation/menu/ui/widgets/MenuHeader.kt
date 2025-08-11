package com.hyperdesign.myapplication.presentation.menu.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
fun MenuHeader(
    onBackPressed: () -> Unit,
    onCartPressed: () -> Unit = {},
    cardCount: String? = "7",
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    val tabs = listOf("Offers", "Chickens", "Meat", "Charcoal Grills")

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
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

                Image(
                    painter = painterResource(id = R.drawable.hadramout_logo),
                    contentDescription = "logo",
                    modifier = Modifier.padding(top = 10.dp).size(70.dp),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .padding(top = 4.dp, end = 10.dp),
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
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {


                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent,
                    contentColor = Color.White,
                    indicator = {
                       TabRowDefaults.Indicator(
                           color = Color.Transparent
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { onTabSelected(index) },
                            modifier = Modifier.background(
                                brush = if (selectedTab == index) Brush.linearGradient(
                                    colors = listOf(Color(0xFFF15A25), Color(0xFFFCB203))
                                ) else Brush.linearGradient(
                                    colors = listOf(Color.Transparent, Color.Transparent)
                                )
                            ),
                            text = {
                                Text(
                                    text = title,
                                    textAlign = TextAlign.Center,
                                    color = if (selectedTab == index) Color.White else Color.White.copy(
                                        alpha = 0.7f
                                    ),
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun MenuHeaderPreview() {
    MenuHeader(
        onBackPressed = {},
        onCartPressed = {},
        cardCount = "3",
        selectedTab = 0,
        onTabSelected = {}
    )
}