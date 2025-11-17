package com.hyperdesign.myapplication.presentation.common.wedgits

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun MainHeader(
    modifier: Modifier = Modifier,
    title: String? = null,
    showBackPressedIcon: Boolean = false,
    onBackPressesd: () -> Unit = {},
    onCartPressed: () -> Unit = {},
    showIcon: Boolean = false,
    cardCount: Int? = 0,
    showLogo: Boolean = false,
    height: Int = 140,
    showTitle: Boolean = false,
    showStatus: Boolean = false,
    onClickChangStatus: (Boolean) -> Unit = {},
    myStatus: Int? = null,
    makePickup: Boolean = false,
    goToMap: () -> Unit = {}
) {
    var statusState by remember { mutableStateOf(myStatus != 0) }
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier.height(height.dp)) {
        // Background Image
        Image(
            painter = painterResource(R.drawable.menueheader),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Top Row: Back + Title + Cart + Status
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back Button
            if (showBackPressedIcon) {
                IconButton(
                    onClick = onBackPressesd,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            } else {
                Spacer(Modifier.width(48.dp))
            }

            // Title (Centered)
            if (showTitle) {
                Text(
                    text = title.orEmpty(),
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                Spacer(Modifier.weight(1f))
            }

            // Right Side: Cart Icon + Badge + Status
            Column(horizontalAlignment = Alignment.End) {
                // Cart with Badge
                if (showIcon) {
                    Box(modifier = Modifier.padding(end = 16.dp)) {
                        IconButton(onClick = onCartPressed) {
                            Icon(
                                painter = painterResource(R.drawable.cart),
                                contentDescription = "Cart",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // Beautiful Badge
                        cardCount?.takeIf { it > 0 }?.let { count ->
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.White,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 10.dp, y = (-6).dp)
                            ) {
                                Text(
                                    text = if (count > 99) "99+" else count.toString(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                // Pickup / Delivery Status
                if (showStatus) {
                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp, end = 16.dp)
                            .clickable { showDialog = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(
                                if (makePickup || !statusState) R.drawable.ic_pickup
                                else R.drawable.delivery
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = if (makePickup || !statusState)
                                stringResource(R.string.pick_up)
                            else
                                stringResource(R.string.delivery),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Logo at Bottom Center
        if (showLogo) {
            Image(
                painter = painterResource(R.drawable.hadramout_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .size(90.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Confirmation Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                title = {
                    Image(painter = painterResource(R.drawable.warning), contentDescription = null)
                },
                text = {
                    Text(
                        text = stringResource(
                            R.string.are_you_sure_you_want_to_change_to,
                            if (makePickup || !statusState) stringResource(R.string.pick_up)
                            else stringResource(R.string.delivery)
                        ),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (makePickup || !statusState)
                                stringResource(R.string.pick_up)
                            else
                                stringResource(R.string.delivery),
                            color = Secondry,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable {
                                    if (makePickup) {
                                        statusState = false
                                        onClickChangStatus(false)
                                        goToMap()
                                    } else {
                                        statusState = !statusState
                                        onClickChangStatus(statusState)
                                        if (!statusState) goToMap()
                                    }
                                    showDialog = false
                                }
                                .padding(12.dp)
                        )
                        Text(
                            text = stringResource(R.string.no),
                            color = Secondry,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable { showDialog = false }
                                .padding(12.dp)
                        )
                    }
                },
                dismissButton = {}
            )
        }
    }
}