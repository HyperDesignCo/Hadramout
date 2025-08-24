package com.hyperdesign.myapplication.presentation.home.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.Featured
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.FeaturedWedgits
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.OffersList
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.offers
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.navcontroller.goToScreenMealDetails
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.main.theme.ui.Primary
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun HomeScreen() {
    val navController = LocalNavController.current
    HomeScreenContent(
        onBackPressed = {
            // navController.popBackStack()
        },
        offers = listOf(
            offers(R.drawable.test_food, 1),
            offers(R.drawable.test_food, 2),
            offers(R.drawable.test_food, 3),
            offers(R.drawable.test_food, 4),
            offers(R.drawable.test_food, 5)
        ),
        fetured = listOf(
            Featured(R.drawable.featured, 1, "Charcoal Grills", "Lorem ipsum dolor sitamet, Lorem ipsum dolor sitamet,", "$10.00"),
            Featured(R.drawable.featured, 2, "Charcoal Grills", "Lorem ipsum dolor sitamet, Lorem ipsum dolor sitamet", "$12.00"),
            Featured(R.drawable.featured, 3, "Charcoal Grills", "Lorem ipsum dolor sitamet, Lorem ipsum dolor sitamet", "$15.00")
        )
    )
}

@Composable
fun HomeScreenContent(onBackPressed: () -> Unit, offers: List<offers>, fetured: List<Featured>) {
    val navController = LocalNavController.current
    var expanded by remember { mutableStateOf(false) }
    var selectedBranch by remember { mutableStateOf("Select Branch") }
    val branches = listOf("Main Branch", "Downtown Branch", "West Branch", "East Branch")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MainHeader(
            showLogo = true,
            title = "",
            onBackPressesd = { onBackPressed() },
            showIcon = true,
            cardCount = "2",
            onCartPressed = {}
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    stringResource(R.string.hello),
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Primary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Gray)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.location),
                                    contentDescription = "location",
                                    tint = Primary
                                )

                                Text(
                                    stringResource(R.string.delevery_to),
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    color = Secondry,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedBranch,
                                    color = Secondry,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                IconButton(
                                    onClick = { expanded = true },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    if (expanded) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropUp,
                                            contentDescription = "Select Branch",
                                            tint = Primary
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Select Branch",
                                            tint = Primary
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    branches.forEach { branch ->
                                        DropdownMenuItem(
                                            text = { Text(branch) },
                                            onClick = {
                                                selectedBranch = branch
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        stringResource(R.string.offers),
                        color = Secondry,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                OffersList(offers = offers)
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        stringResource(R.string.featured),
                        color = Secondry,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            items(items = fetured, key = { featured -> featured.id }) { featured ->
                FeaturedWedgits(
                    featured = featured,
                    onItemClick = {
                        val route = goToScreenMealDetails(featured)
                        navController.navigate(route)

                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HomeScreenContentPreview() {
    HomeScreenContent(
        onBackPressed = {},
        offers = listOf(
            offers(R.drawable.test_food, 1),
            offers(R.drawable.test_food, 2)
        ),
        fetured = listOf(
            Featured(R.drawable.featured, 1, "Charcoal Grills", "Lorem ipsum dolor sitamet", "$10.00")
        )
    )
}