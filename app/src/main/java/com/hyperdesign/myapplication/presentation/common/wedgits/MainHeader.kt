package com.hyperdesign.myapplication.presentation.common.wedgits

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun MainHeader(
    modifier: Modifier = Modifier,
    title: String? = null,
    showBackPressedIcon: Boolean = false,
    onBackPressesd: () -> Unit,
    onCartPressed: () -> Unit = {},
    showIcon: Boolean = false,
    cardCount: String? = "7",
    showLogo: Boolean = false,
    height: Int = 140,
    showTitle: Boolean = false,
    showStatus: Boolean = false,
    onClickChangStatus: (Boolean) -> Unit = {}, // Updated to pass the new status
    myStatus:Int?=null,
    makePickup: Boolean =false,
    goToMap:()->Unit={}
) {
    var statusState by remember { mutableStateOf(if(myStatus==0)false else true) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
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
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
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
                    text = title ?: "",
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
                Column {
                    Box(
                        modifier = Modifier
                            .padding(top = 26.dp, end = 10.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
//                        Box(
//                            modifier = Modifier
//                                .padding(bottom = 27.dp, end = 3.dp)
//                                .size(17.dp)
//                                .background(color = Color.Red, shape = CircleShape),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = cardCount ?: "",
//                                modifier = Modifier
//                                    .padding(bottom = 2.dp)
//                                    .fillMaxSize(),
//                                textAlign = TextAlign.Center,
//                                color = Color.White,
//                                fontSize = 9.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                        }

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
                    if (showStatus) {
                        Row(
                            modifier = Modifier
                                .padding(end = 5.dp, top = 15.dp)
                                .fillMaxWidth()
                                .weight(1f)
                                .clickable {
                                    showDialog = true // Show dialog on click
                                },
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(

                                painter = if(makePickup==true) painterResource(R.drawable.ic_pickup) else  if (!statusState) painterResource(R.drawable.delivery) else painterResource(R.drawable.delivery  ),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(if(makePickup==true) stringResource(R.string.delivery) else if (!statusState) stringResource(R.string.pick_up) else stringResource(R.string.delivery), color = Color.White)
                        }
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

        // Custom-designed Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                title = {
                   Image(
                       painter = painterResource(R.drawable.warning),
                       contentDescription = ""

                   )
                },
                text = {
                    Text(
                        text = stringResource(
                            R.string.are_you_sure_you_want_to_change_to,
                            if(makePickup==true)stringResource(R.string.delivery) else if (!statusState) stringResource(R.string.pick_up) else stringResource(R.string.delivery)
                        ),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                },
                confirmButton = {
                    Row(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(), horizontalArrangement =Arrangement.SpaceBetween ) {
                        Text(
                            text = if(makePickup==true)stringResource(R.string.delivery) else if (!statusState) stringResource(R.string.pick_up) else stringResource(R.string.delivery),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Secondry,
                            modifier = Modifier
                                .clickable {
                                    if(makePickup==true){
                                        statusState=false
                                        onClickChangStatus(false)
                                        goToMap()
                                        showDialog = false
                                    }else{
                                        statusState = !statusState
                                        onClickChangStatus(statusState) // Pass the new status
                                        if (!statusState){
                                            goToMap()
                                        }
                                        showDialog = false
                                    }

                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                        Text(
                            text = stringResource(R.string.no),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Secondry,
                            modifier = Modifier
                                .clickable { showDialog = false }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                },
                dismissButton = {

                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 8.dp
            )
        }
    }
}

//@Composable
//@Preview(showBackground = true, showSystemUi = true)
//fun MainHeaderPreview() {
//    MainHeader(
//        title = "",
//        onBackPressesd = {},
//        showIcon = true,
//        showLogo = true,
//        showBackPressedIcon = true,
//        showStatus = true,
//        onClickChangStatus = {}
//    )
//}