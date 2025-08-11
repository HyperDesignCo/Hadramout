package com.hyperdesign.myapplication.presentation.main.navcontroller


import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hyperdesign.myapplication.presentation.main.theme.ui.Secondry

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFF15A25), Color(0xFFFCB203))
    )

    NavigationBar(containerColor = Color.White) {
        bottomNavScreens.forEach { screen ->
            val isSelected = currentRoute == screen.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.HomeScreen.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    screen.icon?.let { iconRes ->
                        if (isSelected) {
                            // Gradient icon
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = screen.title?.let { stringResource(id = it) },
                                modifier = Modifier
                                    .size(24.dp)
                                    .graphicsLayer(alpha = 0.99f)
                                    .drawWithCache {
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(brush = selectedGradient, blendMode = BlendMode.SrcAtop)
                                        }
                                    }
                            )
                        } else {
                            // Default icon
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = screen.title?.let { stringResource(id = it) },
                                tint = Secondry
                            )
                        }
                    }
                },
                label = {
                    screen.title?.let {
                        if (isSelected) {
                            // Gradient text
                            Text(
                                text = stringResource(id = it),
                                modifier = Modifier
                                    .graphicsLayer(alpha = 0.99f)
                                    .drawWithCache {
                                        val brush = selectedGradient
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(brush = brush, blendMode = BlendMode.SrcAtop)
                                        }
                                    }
                            )
                        } else {
                            // Default text
                            Text(
                                text = stringResource(id = it),
                                color = Secondry
                            )
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color.Unspecified,
                    selectedTextColor = Color.Unspecified,
                    unselectedIconColor = Secondry,
                    unselectedTextColor = Secondry
                )
            )
        }
    }
}