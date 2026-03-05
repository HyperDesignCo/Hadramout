package com.hyperdesign.myapplication.presentation.home.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavBackStackEntry
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
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
import com.hyperdesign.myapplication.presentation.home.HomeObject
import com.hyperdesign.myapplication.presentation.home.mvi.HomeIntents
import com.hyperdesign.myapplication.presentation.home.mvi.HomeViewModel
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.AdsWdegit
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.FeaturedWedgits
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.NoDeliveryDialogHome
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.OffersList
import com.hyperdesign.myapplication.presentation.home.ui.wedgit.PickupBranchDialog
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
    homeViewModel: HomeViewModel = koinViewModel(), navBackStackEntry: NavBackStackEntry? = null
) {
    val navController = LocalNavController.current
    val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()
    val showAuthDialoge by homeViewModel.showAuthDialoge
    val context = LocalContext.current
    var status by remember { mutableStateOf(homeViewModel.tokenManager.getStatus() == 1) }
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }


    var userLocation by remember { mutableStateOf(LatLng(30.0444, 31.2357)) } // Default to Cairo
    var isFetchingLocation by remember { mutableStateOf(false) }
    var makePichUp by remember { mutableStateOf(false) }
    var checkValueEqualZoroOrNot by remember { mutableStateOf(1) }
    val scope = rememberCoroutineScope()
    var selectedBranchState by remember { mutableStateOf("") }

    var newBranchId by remember { mutableStateOf(0) }

    LaunchedEffect(newBranchId) {
        if (newBranchId != 0) {
            homeViewModel.handleIntents(HomeIntents.GetHomeMenuId(newBranchId))
        }
    }

    val navArgsKey by rememberUpdatedState(
        "${navBackStackEntry?.arguments?.getString("lat")}:${
            navBackStackEntry?.arguments?.getString(
                "lng"
            )
        }:${navBackStackEntry?.arguments?.getString("pickup")}"
    )


    var hasShownNoDeliveryDialog by remember { mutableStateOf(false) }
    var showPickupBranchDialog by remember { mutableStateOf(false) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionState = rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        )

        var hasRequestedNotificationPermission by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            if (!hasRequestedNotificationPermission && !notificationPermissionState.status.isGranted) {
                hasRequestedNotificationPermission = true
                notificationPermissionState.launchPermissionRequest()
            }
        }
    }


    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        } else {
            if (navArgsKey == "noDialoge" || navArgsKey == "noDialoge::") {
            } else {
                isFetchingLocation = true
                if (navArgsKey.isEmpty() || navArgsKey == "::") {
                    scope.launch {
                        try {
                            val lastLocation = fusedLocationClient.lastLocation.await()
                            if (lastLocation != null) {
                                userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                            } else {
                                val currentLocation = fusedLocationClient.getCurrentLocation(
                                    Priority.PRIORITY_HIGH_ACCURACY, null
                                ).await()
                                if (currentLocation != null) {
                                    userLocation =
                                        LatLng(currentLocation.latitude, currentLocation.longitude)
                                }
                            }
                        } catch (e: Exception) {
                        } finally {
                            isFetchingLocation = false
                        }

                        homeViewModel.handleIntents(HomeIntents.ChangeLat(userLocation.latitude.toString()))
                        homeViewModel.handleIntents(HomeIntents.ChangeLng(userLocation.longitude.toString()))
                        homeViewModel.handleIntents(HomeIntents.CheckLocation)
                    }
                }
            }
        }
    }


    LaunchedEffect(navArgsKey) {
        navBackStackEntry?.arguments?.let { args ->
            val lat = args.getString("lat")
            val lng = args.getString("lng")
            val pickup = args.getString("pickup")

            if (lat != null && lng != null && lat.isNotEmpty() && lng.isNotEmpty()) {
                try {
                    userLocation = LatLng(lat.toDouble(), lng.toDouble())
                    homeViewModel.handleIntents(HomeIntents.ChangeLat(lat))
                    homeViewModel.handleIntents(HomeIntents.ChangeLng(lng))
                    homeViewModel.handleIntents(HomeIntents.CheckLocation)
                } catch (e: NumberFormatException) {
                } finally {
                    isFetchingLocation = false
                }
            }

            if (pickup == "1" && HomeObject.status == 0) {
                status = true
                homeViewModel.tokenManager.saveStatus(1)
                showPickupBranchDialog = true
            }
        }
    }

    if (!permissionState.allPermissionsGranted) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {}
        return
    }

    val deliveryStatus = homeState.checkLocationResponseEntity?.data?.deliveryStatus

    LaunchedEffect(deliveryStatus) {
        if (deliveryStatus == "0" && HomeObject.status == 0 && !hasShownNoDeliveryDialog && navArgsKey != "noDialoge" && navArgsKey != "noDialoge::") {
            hasShownNoDeliveryDialog = true
        } else {
            homeViewModel.tokenManager.saveCurrentResturentBranch(
                homeState.checkLocationResponseEntity?.data?.currentResturantBranch ?: ""
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HomeScreenContent(
            branches = homeState.branches?.branches.orEmpty(),
            offers = homeState.homeMenues?.slideshow.orEmpty(),
            homeMenus = homeState.homeMenues?.homeMenus.orEmpty(),
            onBranchSelected = { branchId ->
                newBranchId = branchId
                if (checkValueEqualZoroOrNot != 0) {
                    homeViewModel.tokenManager.saveBranchId(branchId)
                }
            },
            onBackPressed = { /* navController.popBackStack() */ },
            status = status,
            onStatusChanged = { newStatus ->
                status = newStatus
                homeViewModel.tokenManager.saveStatus(if (newStatus) 1 else 0)
                if (newStatus) {
                    hasShownNoDeliveryDialog = false
                    showPickupBranchDialog = true
                }
            },
            onCartPressed = {
                navController.navigate(Screen.CartScreen.route)

            },
            saveBranchId = { branchId ->
                homeViewModel.tokenManager.saveBranchId(branchId)
            },
            getBranchId = homeViewModel.tokenManager.getBranchId()
                ?: homeState.branches?.branches?.get(0)?.id ?: 0,
            myStatus = homeViewModel.tokenManager.getStatus(),
            bestSelling = homeState.homeMenues?.bestSalesMeals.orEmpty(),
            ads = homeState.homeMenues?.ads.orEmpty(),
            checkOnZeroOrNot = { valueEquealZoro ->
                checkValueEqualZoroOrNot = valueEquealZoro
            },
            navToMap = {
                navController.navigate(Screen.MapScreen.route.replace("{navigateFrom}", "Home"))
            },
            currentResturentBranch = homeState.checkLocationResponseEntity?.data?.currentResturantBranch.orEmpty(),
            selectedBranchState = remember { mutableStateOf(selectedBranchState) },
            onShowPickupDialog = { showPickupBranchDialog = true },
            makePickup = makePichUp,
            saveOpenBranch = {
                homeViewModel.tokenManager.saveOpenTimeBranch(it)
            },
            saveCloseBranch = {
                homeViewModel.tokenManager.saveCloseTimeBranch(it)
            },
            cartNum = homeState.cartNum
        )

        if (showAuthDialoge) {
            ShowAuthentaionDialge(onNavToLogin = {
                navController.navigate(Screen.LoginInScreen.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
                homeViewModel.showAuthDialoge.value = false
            }, onCancel = {
                homeViewModel.showAuthDialoge.value = false
            })
        }

        if (deliveryStatus == "0" && hasShownNoDeliveryDialog) {
            NoDeliveryDialogHome(onPickUp = {
                status = true
                homeViewModel.tokenManager.saveStatus(1)
                hasShownNoDeliveryDialog = false
                showPickupBranchDialog = true
            }, onChangeLocation = {
                status = false
                homeViewModel.tokenManager.saveStatus(0)
                hasShownNoDeliveryDialog = false
                navController.navigate(Screen.MapScreen.route.replace("{navigateFrom}", "Home"))
            })
        }

        if (showPickupBranchDialog) {
            PickupBranchDialog(
                branches = homeState.branches?.branches.orEmpty(),
                currentBranchId = homeViewModel.tokenManager.getBranchId()
                    ?: homeState.branches?.branches?.firstOrNull()?.id ?: 0,
                onBranchSelected = { branch ->
                    makePichUp = true
                    homeViewModel.tokenManager.saveBranchId(branch.id)
                    selectedBranchState = branch.title
                    showPickupBranchDialog = false
                    HomeObject.updateStatus(1)
                },
                onDismiss = {
                    showPickupBranchDialog = false
                    HomeObject.updateStatus(1)
                })
        }

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
    currentResturentBranch: String,
    checkOnZeroOrNot: (Int) -> Unit = {},
    selectedBranchState: MutableState<String>,
    onShowPickupDialog: () -> Unit,
    makePickup: Boolean = false,
    saveOpenBranch: (String) -> Unit,
    saveCloseBranch: (String) -> Unit,
    cartNum: Int?
) {
    val navController = LocalNavController.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val itemWidth = (screenWidth * 0.90f)

    LaunchedEffect(currentResturentBranch) {
        if (myStatus == 0 && currentResturentBranch.isNotEmpty()) {
            val selected = branches.find { it.id == currentResturentBranch.toIntOrNull() }
            if (selected != null) {
                selectedBranchState.value = selected.title
                onBranchSelected(selected.id)
                saveOpenBranch(selected.openTime)
                saveCloseBranch(selected.closeTime)
            }
        }
    }

    LaunchedEffect(branches, myStatus, getBranchId, currentResturentBranch) {
        if (branches.isNotEmpty()) {
            if (myStatus == 0) {
                if (getBranchId == 0 && currentResturentBranch.isEmpty()) {
                    selectedBranchState.value = branches[0].title
                    saveOpenBranch(branches[0].openTime)
                    saveCloseBranch(branches[0].closeTime)
                    checkOnZeroOrNot(0)
                    onBranchSelected(branches[0].id)
                } else if (currentResturentBranch.isEmpty()) {
                    selectedBranchState.value = branches[0].title
                    onBranchSelected(branches[0].id)
                    saveOpenBranch(branches[0].openTime)
                    saveCloseBranch(branches[0].closeTime)
                }
            } else {
                if (getBranchId != 0) {
                    val selected = branches.find { it.id == getBranchId }
                    if (selected != null) {
                        selectedBranchState.value = selected.title
                        onBranchSelected(selected.id)
                        saveOpenBranch(selected.openTime)
                        saveCloseBranch(selected.closeTime)
                    } else {
                        saveBranchId(0)
                        selectedBranchState.value = branches[0].title
                        onBranchSelected(branches[0].id)
                        saveBranchId(branches[0].id)
                        saveOpenBranch(branches[0].openTime)
                        saveCloseBranch(branches[0].closeTime)
                    }
                } else {
                    if (currentResturentBranch.isEmpty()) {
                        selectedBranchState.value = branches[0].title
                        onBranchSelected(branches[0].id)
                        saveBranchId(branches[0].id)
                        saveOpenBranch(branches[0].openTime)
                        saveCloseBranch(branches[0].closeTime)
                    }
                }
            }
        }
    }


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
            cardCount = cartNum,
            onCartPressed = { onCartPressed() },
            showStatus = true,
            onClickChangStatus = onStatusChanged,
            myStatus = myStatus,
            goToMap = { navToMap() },
            makePickup = makePickup
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
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
                                text = if (!status) stringResource(R.string.delevery_to) else stringResource(
                                    R.string.pickup_from
                                ),
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
                                text = selectedBranchState.value,
                                color = Secondry,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable {
                                    if (myStatus == 1) {
                                        onShowPickupDialog()
                                    } else {
                                        navToMap()
                                    }
                                })
                            IconButton(
                                onClick = {
                                    if (myStatus == 1) {
                                        onShowPickupDialog()
                                    } else {
                                        navToMap()
                                    }
                                }, modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Branch",
                                    tint = Primary
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(15.dp))
                OffersList(offers = offers, navToMealDetais = { mealId ->
                    val route = goToScreenMealDeataisWithString(mealId)
                    navController.navigate(route)
                }, navToMenu = {
                    navController.navigate(Screen.MenueScreen.route)
                })
            }


                item {
                    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            stringResource(R.string.categories),
                            color = Secondry,
                            fontSize = 17.sp,
                            modifier = Modifier.padding(top = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }




            homeMenus.forEach { homeMenu ->

                item {
                    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            homeMenu.title,
                            color = Secondry,
                            fontSize = 17.sp,
                            modifier = Modifier.padding(top = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(homeMenu.meals, key = { meal -> meal.id }) { meal ->

                            Box(
                                modifier = Modifier.width(itemWidth)
                            ) {
                                FeaturedWedgits(
                                    meal = meal, onItemClick = {
                                        val route = goToScreenMealDetails(meal)
                                        navController.navigate(route)
                                    })
                            }


                        }
                    }
                    Spacer(modifier = Modifier.height(9.dp))
                }
            }

            if (bestSelling.isNotEmpty()) {
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
            }

            items(bestSelling, key = { meal -> meal.id }) { meal ->
                FeaturedWedgits(
                    meal = meal, onItemClick = {
                        val route = goToScreenMealDetails(meal)
                        navController.navigate(route)
                    })
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (ads.isNotEmpty()) {
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
            }

            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(ads, key = { meal -> meal.id }) { meal ->
                        AdsWdegit(
                            meal = meal, onItemClick = {
                                val route = goToScreenMealDeataisWithString(meal.mealId ?: "")
                                navController.navigate(route)
                            })
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

