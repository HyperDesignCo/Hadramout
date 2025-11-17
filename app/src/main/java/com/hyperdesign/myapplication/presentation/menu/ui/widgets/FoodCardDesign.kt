package com.hyperdesign.myapplication.presentation.menu.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import coil.request.ImageRequest
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun FoodCardDesign(imageUrl: String,onGoToCart:()->Unit,onGoToMenu:()->Unit) {
    // Set RTL layout direction for Arabic support
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .error(com.hyperdesign.myapplication.R.drawable.test_food)
                            .placeholder(R.drawable.test_food)
                            .build(),
                        contentDescription = "Food image",
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .width(180.dp)
                            .height(140.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = stringResource(R.string.the_meal_has_been_added_to_the_cart),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { onGoToMenu() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .width(130.dp)
                                .height(48.dp)
                        ) {
                            Text(stringResource(R.string.menu), color = Secondry)
                        }

                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .width(140.dp)
                                .height(48.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(Color(0xFFF15A25), Color(0xFFFCB203))
                                    )
                                )
                        ) {
                            Button(
                                onClick = { onGoToCart() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Text(stringResource(R.string.view_cart), color = Color.White)
                            }
                        }
                    }
                }
                // Optional close icon at top right
//                IconButton(
//                    onClick = { /* Handle close */ },
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .padding(8.dp)
//                ) {
//                    Icon(
//                        painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
//                        contentDescription = "Close",
//                        tint = Color.Gray
//                    )
//                }
            }
        }
    }
}


@Composable
fun MinimumChargeDesign(cancel:()->Unit,onGoToMenu:()->Unit,minmumCharge:String) {
    // Set RTL layout direction for Arabic support
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.warning),
                        contentDescription = "warining",
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .padding(top = 10.dp)
//                            .width(180.dp)
//                            .height(140.dp)
                            ,
                    )
                    Text(
                        text = "${stringResource(R.string.please_note_minumm_charge_is)} $minmumCharge",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding( vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { cancel() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color.Black),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .width(130.dp)
                                .height(48.dp)
                        ) {
                            Text(stringResource(R.string.cancel), color = Secondry)
                        }

                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .width(140.dp)
                                .height(48.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(Color(0xFFF15A25), Color(0xFFFCB203))
                                    )
                                )
                        ) {
                            Button(
                                onClick = { onGoToMenu() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Text(stringResource(R.string.menu), color = Color.White)
                            }
                        }
                    }
                }
                // Optional close icon at top right
//                IconButton(
//                    onClick = { /* Handle close */ },
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .padding(8.dp)
//                ) {
//                    Icon(
//                        painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
//                        contentDescription = "Close",
//                        tint = Color.Gray
//                    )
//                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun FoodCardDesignPreview() {
    FoodCardDesign(imageUrl = "", onGoToCart = {}, onGoToMenu = {})
}