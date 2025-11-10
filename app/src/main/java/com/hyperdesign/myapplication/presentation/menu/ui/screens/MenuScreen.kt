import com.hyperdesign.myapplication.presentation.home.ui.wedgit.FeaturedWedgits


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hyperdesign.myapplication.domain.Entity.Meal
import com.hyperdesign.myapplication.domain.Entity.MenuEntity
import com.hyperdesign.myapplication.presentation.common.wedgits.ShowAuthentaionDialge
import com.hyperdesign.myapplication.presentation.main.navcontroller.LocalNavController
import com.hyperdesign.myapplication.presentation.main.navcontroller.Screen
import com.hyperdesign.myapplication.presentation.main.navcontroller.goToScreenMealDetails
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry
import com.hyperdesign.myapplication.presentation.menu.mvi.MenuIntents
import com.hyperdesign.myapplication.presentation.menu.mvi.MenuViewModel
import com.hyperdesign.myapplication.presentation.menu.ui.widgets.MenuHeader
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MenuScreen(menuViewModel: MenuViewModel = koinViewModel()) {
    val navController = LocalNavController.current
    val menuState by menuViewModel.menuState.collectAsStateWithLifecycle()
    var menuTabs by remember { mutableStateOf(listOf<MenuEntity>()) }

    val showAuthDialoge by menuViewModel.showAuthDialoge

    val branchId by remember { mutableStateOf(menuViewModel.tokenManager.getBranchId()) }

    LaunchedEffect(menuState) {
        menuTabs = menuState.menuData?.menus.orEmpty()
    }

    LaunchedEffect(branchId) {
        menuViewModel.handleIntent(MenuIntents.getMenus(branchId?:0))
    }

    Box(modifier = Modifier.fillMaxSize()) {


        MenuScreenContent(
            onBackPressed = { navController.popBackStack() },
            menuTabs = menuTabs,
            onCartPressed = {
                    navController.navigate(Screen.CartScreen.route)

            }
        )

        if (showAuthDialoge) {
            ShowAuthentaionDialge(onNavToLogin = {
                navController.navigate(Screen.LoginInScreen.route) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                menuViewModel.showAuthDialoge.value = false

            }, onCancel = {
                menuViewModel.showAuthDialoge.value = false

            })
        }

        if (menuState.isLoading) {
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
fun MenuScreenContent(
    onBackPressed: () -> Unit,
    menuTabs: List<MenuEntity>,
    onCartPressed:()->Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current


    // Create a list of items to display in the LazyColumn (headers and meals)
    val items = mutableListOf<Any>()
    menuTabs.forEach { menu ->
        items.add(menu) // Add menu header
        items.addAll(menu.meals) // Add meals for this menu
    }

    // Map to track the index of each menu header in the LazyColumn
    val menuHeaderIndices = menuTabs.mapIndexed { index, menu ->
        menu.id to items.indexOf(menu)
    }.toMap()

    // Scroll to the selected tab's section
    LaunchedEffect(selectedTab) {
        if (menuTabs.isNotEmpty()) {
            val menuId = menuTabs[selectedTab.coerceIn(0, menuTabs.size - 1)].id
            val targetIndex = menuHeaderIndices[menuId] ?: 0
            coroutineScope.launch {
                lazyListState.animateScrollToItem(targetIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MenuHeader(
            onBackPressed = onBackPressed,
            onCartPressed = {onCartPressed()},
            cardCount = "22",
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                selectedTab = tab
            },
            menuTabs = menuTabs
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            items(items, key = { item ->
                when (item) {
                    is MenuEntity -> "menu_${item.id}"
                    is Meal -> "meal_${item.id}"
                    else -> item.hashCode().toString()
                }
            }) { item ->
                when (item) {
                    is MenuEntity -> {
                        Text(
                            text = item.title,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Secondry
                        )
                    }
                    is Meal -> {
                        FeaturedWedgits(
                            meal = item,
                            onItemClick = {
                                val route = goToScreenMealDetails(item)
                                navController.navigate(route)
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}
