package com.hyperdesign.myapplication.presentation.home.ui.screens

import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.Branch
import com.hyperdesign.myapplication.domain.Entity.HomeMenu
import com.hyperdesign.myapplication.domain.Entity.Meal
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.home.mvi.HomeIntents
import com.hyperdesign.myapplication.presentation.home.mvi.HomeViewModel
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.FeaturedWedgits
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.OffersList
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.offers
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.goToScreenMealDetails
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.main.theme.ui.Primary
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()

    var branchList by remember { mutableStateOf<List<Branch>>(emptyList()) }
    var bestSalesMeals by remember { mutableStateOf<List<Meal>>(emptyList()) }
    var homeMenus by remember { mutableStateOf<List<HomeMenu>>(emptyList()) }
    var status by remember { mutableStateOf(false) } // Track delivery/pickup status

    Log.d("HomeScreen", "bestSalesMeals: ${homeState.homeMenues?.bestSalesMeals}")
    Log.d("HomeScreen", "homeMenus: ${homeState.homeMenues?.homeMenus}")

    LaunchedEffect(homeState) {
        branchList = homeState.branches?.branches ?: emptyList()
        bestSalesMeals = homeState.homeMenues?.bestSalesMeals ?: emptyList()
        homeMenus = homeState.homeMenues?.homeMenus ?: emptyList()
        if (homeState.errorMessage.isNotEmpty()) {
            Log.d("HomeScreen", "Error: ${homeState.errorMessage}")
            // Optionally show Toast or Snackbar for error
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HomeScreenContent(
            branches = branchList,
            offers = bestSalesMeals,
            homeMenus = homeMenus,
            onBranchSelected = { branchId ->
                homeViewModel.handleIntents(HomeIntents.changeBranchId(branchId))
            },
            onBackPressed = { /* navController.popBackStack() */ },
            status = status,
            onStatusChanged = { newStatus ->
                status = newStatus // Update status state
            }
        )
        if (homeState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Secondry)
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    branches: List<Branch>,
    offers: List<Meal>,
    homeMenus: List<HomeMenu>,
    onBranchSelected: (Int) -> Unit,
    onBackPressed: () -> Unit,
    status: Boolean,
    onStatusChanged: (Boolean) -> Unit // Callback to update status
) {
    val navController = LocalNavController.current
    var expanded by remember { mutableStateOf(false) }
    var selectedBranch by remember { mutableStateOf("Select Branch") }

    LaunchedEffect(branches) {
        if (branches.isNotEmpty() && selectedBranch == "Select Branch") {
            selectedBranch = branches[0].title
            onBranchSelected(branches[0].id)
        }
    }

    Log.d("HomeScreen", "Branches: $branches, Offers: $offers, HomeMenus: $homeMenus")

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
            onCartPressed = {},
            showStatus = true,
            onClickChangStatus = onStatusChanged // Pass the callback to update status
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.location),
                                contentDescription = "location",
                                tint = Primary
                            )
                            Text(
                                text = if (!status) stringResource(R.string.delevery_to) else stringResource(R.string.pickup_from),
                                modifier = Modifier.padding(horizontal = 10.dp),
                                color = Secondry,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
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
                                if (branches.isEmpty()) {
                                    // DropdownMenuItem(
                                    //     text = { Text("No branches available") },
                                    //     onClick = { expanded = false }
                                    // )
                                } else {
                                    branches.forEach { branch ->
                                        DropdownMenuItem(
                                            text = { Text(branch.title) },
                                            onClick = {
                                                selectedBranch = branch.title
                                                expanded = false
                                                onBranchSelected(branch.id)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
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
                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.meals),
                        color = Secondry,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            homeMenus.forEach { homeMenu ->
                items(homeMenu.meals, key = { meal -> meal.id }) { meal ->
                    FeaturedWedgits(
                        meal = meal,
                        onItemClick = {
                            val route = goToScreenMealDetails(meal)
                            navController.navigate(route)
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}