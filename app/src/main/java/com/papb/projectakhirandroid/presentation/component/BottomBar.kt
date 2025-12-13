package com.papb.projectakhirandroid.presentation.component

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.papb.projectakhirandroid.navigation.screen.BottomNavItemScreen
import com.papb.projectakhirandroid.ui.theme.Green

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    // Daftar navigasi yang sudah diperbaiki, memasukkan Komunitas
    val navigationItems = listOf(
        BottomNavItemScreen.Home,
        BottomNavItemScreen.Explore,
        BottomNavItemScreen.Cart,
        BottomNavItemScreen.Komunitas, // <-- Item Komunitas
        BottomNavItemScreen.About
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val bottomBarDestination = navigationItems.any { it.route == currentRoute }

    if (bottomBarDestination) {
        BottomNavigation(
            backgroundColor = Color.White,
            contentColor = Color.Black,
            modifier = modifier
        ) {
            navigationItems.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(imageVector = item.icon, contentDescription = item.title)
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontWeight = FontWeight.SemiBold,
                            color = if (currentRoute == item.route) {
                                Green
                            } else Color.Black.copy(0.4f)
                        )
                    },
                    selectedContentColor = Green,
                    unselectedContentColor = Color.Black.copy(0.4f),
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            // Ini adalah logika untuk menghindari duplikasi item di back stack
                            navController.graph.startDestinationRoute?.let { screenRoute ->
                                popUpTo(screenRoute) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

        }
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(navController = rememberNavController())
}