package com.hyperdesign.myapplication.presentation.home.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.domain.Entity.AdsEntity
import com.hyperdesign.myapplication.domain.Entity.Branch
import com.hyperdesign.myapplication.domain.Entity.HomeMenu
import com.hyperdesign.myapplication.domain.Entity.Meal
import com.hyperdesign.myapplication.domain.Entity.SlideShowEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.MainHeader
import com.hyperdesign.myapplication.presentation.common.wedgits.ShowAuthentaionDialge
import com.hyperdesign.myapplication.presentation.home.mvi.HomeIntents
import com.hyperdesign.myapplication.presentation.home.mvi.HomeViewModel
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.AdsWdegit
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.FeaturedWedgits
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.OffersList
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.navcontroller.goToScreenMealDeataisWithString
import com.hyperdesign.myapplication.presentation.main.navcontroller.goToScreenMealDetails
import com.hyperdesign.myapplication.presentation.main.theme.ui.Gray
import com.hyperdesign.myapplication.presentation.main.theme.ui.Primary
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.androidx.compose.koinViewModel

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
) {
    val navController = LocalNavController.current
    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()
    val showAuthDialoge by homeViewModel.showAuthDialoge
    val context = LocalContext.current
    var status by remember { mutableStateOf(homeViewModel.tokenManager.getStatus() == 1) }
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var userLocation by remember { mutableStateOf(LatLng(30.0444, 31.2357)) } // Default to Cairo
    var isFetchingLocation by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        } else {
            isFetchingLocation = true
            scope.launch {
                try {
                    val lastLocation = fusedLocationClient.lastLocation.await()
                    if (lastLocation != null) {
                        userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                        Log.d("HomeScreen", "Location fetched (last): $userLocation")
                    } else {
                        val currentLocation = fusedLocationClient.getCurrentLocation(
                            Priority.PRIORITY_HIGH_ACCURACY, null
                        ).await()
                        if (currentLocation != null) {
                            userLocation = LatLng(currentLocation.latitude, currentLocation.longitude)
                            Log.d("HomeScreen", "Location fetched (current): $userLocation")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("HomeScreen", "Failed to get location", e)
                } finally {
                    isFetchingLocation = false
                }
                homeViewModel.handleIntents(HomeIntents.ChangeLat(userLocation.latitude.toString()))
                homeViewModel.handleIntents(HomeIntents.ChangeLng(userLocation.longitude.toString()))
                homeViewModel.handleIntents(HomeIntents.CheckLocation)
            }
        }
    }

    if (!permissionState.allPermissionsGranted) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Location permission is required")
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HomeScreenContent(
            branches = homeState.branches?.branches.orEmpty(),
            offers = homeState.homeMenues?.slideshow.orEmpty(),
            homeMenus = homeState.homeMenues?.homeMenus.orEmpty(),
            onBranchSelected = { branchId ->
                homeViewModel.handleIntents(HomeIntents.GetHomeMenuId(branchId))
                homeViewModel.tokenManager.saveBranchId(branchId)
            },
            onBackPressed = { /* navController.popBackStack() */ },
            status = status,
            onStatusChanged = { newStatus ->
                status = newStatus
                homeViewModel.tokenManager.saveStatus(if (newStatus) 1 else 0)
            },
            onCartPressed = {
                if (homeViewModel.tokenManager.getUserData()?.authenticated == "authenticated") {
                    navController.navigate(Screen.CartScreen.route)
                } else {
                    homeViewModel.showAuthDialoge.value = true
                }
            },
            saveBranchId = { branchId ->
                homeViewModel.tokenManager.saveBranchId(branchId)
            },
            getBranchId = homeViewModel.tokenManager.getBranchId() ?: homeState.branches?.branches?.get(0)?.id ?: 0,
            myStatus = homeViewModel.tokenManager.getStatus(),
            bestSelling = homeState.homeMenues?.bestSalesMeals.orEmpty(),
            ads = homeState.homeMenues?.ads.orEmpty(),
            navToMap = {
                navController.navigate(Screen.MapScreen.route)
            },
            currentResturentBranch = homeState.checkLocationResponseEntity?.data?.currentResturantBranch.orEmpty()
        )

        if (showAuthDialoge) {
            ShowAuthentaionDialge(
                onNavToLogin = {
                    navController.navigate(Screen.LoginInScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                    homeViewModel.showAuthDialoge.value = false
                },
                onCancel = {
                    homeViewModel.showAuthDialoge.value = false
                }
            )
        }

        if (homeState.isLoading || isFetchingLocation) {
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
    offers: List<SlideShowEntity>,
    homeMenus: List<HomeMenu>,
    onBranchSelected: (Int) -> Unit,
    onBackPressed: () -> Unit,
    onCartPressed: () -> Unit,
    status: Boolean,
    getBranchId: Int,
    saveBranchId: (Int) -> Unit,
    bestSelling: List<Meal>,
    ads: List<AdsEntity>,
    onStatusChanged: (Boolean) -> Unit,
    myStatus: Int?,
    navToMap: () -> Unit,
    currentResturentBranch: String
) {
    val navController = LocalNavController.current
    var expanded by remember { mutableStateOf(false) }
    var selectedBranch by remember { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    LaunchedEffect(currentResturentBranch) {
        if (myStatus == 0 && currentResturentBranch.isNotEmpty()) {
            val selected = branches.find { it.id == currentResturentBranch.toIntOrNull() }
            if (selected != null) {
                selectedBranch = selected.title
                onBranchSelected(selected.id)
            }
        }
    }

    LaunchedEffect(branches) {
        if (branches.isNotEmpty()) {
            if (myStatus == 0) {
                // Wait for currentResturentBranch to set selectedBranch
            } else {
                if (getBranchId != 0) {
                    val selected = branches.find { it.id == getBranchId }
                    if (selected != null) {
                        selectedBranch = selected.title
                        onBranchSelected(selected.id)
                    } else {
                        saveBranchId(0)
                        selectedBranch = branches[0].title
                        onBranchSelected(branches[0].id)
                        saveBranchId(branches[0].id)
                    }
                } else {
                    selectedBranch = branches[0].title
                    onBranchSelected(branches[0].id)
                    saveBranchId(branches[0].id)
                }
            }
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
            onCartPressed = {
                onCartPressed()
            },
            showStatus = true,
            onClickChangStatus = onStatusChanged,
            myStatus = myStatus,
            goToMap = {
                navToMap()
            }
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
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable {
                                    if (myStatus == 1) {
                                        expanded = true
                                    } else {
                                        navToMap()
                                    }
                                }
                            )
                            IconButton(
                                onClick = {
                                    if (myStatus == 1) {
                                        expanded = true
                                    } else {
                                        navToMap()
                                    }
                                },
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
            }

            item {
                Spacer(modifier = Modifier.height(15.dp))
                OffersList(
                    offers = offers,
                    navToMealDetais = { mealId ->
                        val route = goToScreenMealDeataisWithString(mealId)
                        navController.navigate(route)
                    },
                    navToMenu = {
                        navController.navigate(Screen.MenueScreen.route)
                    }
                )
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        stringResource(R.string.meals),
                        color = Secondry,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            homeMenus.forEach { homeMenu ->
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(homeMenu.meals, key = { meal -> meal.id }) { meal ->
                            Box(
                                modifier = Modifier.width(screenWidth)
                            ) {
                                FeaturedWedgits(
                                    meal = meal,
                                    onItemClick = {
                                        val route = goToScreenMealDetails(meal)
                                        navController.navigate(route)
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.best_selling),
                        color = Secondry,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            items(bestSelling, key = { meal -> meal.id }) { meal ->
                FeaturedWedgits(
                    meal = meal,
                    onItemClick = {
                        val route = goToScreenMealDetails(meal)
                        navController.navigate(route)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        stringResource(R.string.offers),
                        color = Secondry,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(top = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(ads, key = { meal -> meal.id }) { meal ->
                        AdsWdegit(
                            meal = meal,
                            onItemClick = {
                                val route = goToScreenMealDeataisWithString(meal.mealId ?: "")
                                navController.navigate(route)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
