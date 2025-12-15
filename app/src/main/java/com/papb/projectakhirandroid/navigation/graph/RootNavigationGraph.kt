package com.papb.projectakhirandroid.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.papb.projectakhirandroid.navigation.screen.Screen
import com.papb.projectakhirandroid.presentation.screen.MainScreen
import com.papb.projectakhirandroid.presentation.screen.login.LoginScreen
import com.papb.projectakhirandroid.presentation.screen.register.RegisterScreen

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    onLogout: () -> Unit
) {

    // 🔥 INI KUNCINYA
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(Graph.MAIN) {
                popUpTo(Graph.ROOT) { inclusive = true }
                launchSingleTop = true
            }
        } else {
            navController.navigate(Graph.AUTH) {
                popUpTo(Graph.ROOT) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = if (isLoggedIn) Graph.MAIN else Graph.AUTH
    ) {

        /* ================= AUTH GRAPH ================= */
        navigation(
            route = Graph.AUTH,
            startDestination = Screen.Login.route
        ) {
            composable(Screen.Login.route) {
                LoginScreen(navController)
            }
            composable(Screen.Register.route) {
                RegisterScreen(navController)
            }
        }

        /* ================= MAIN GRAPH ================= */
        navigation(
            route = Graph.MAIN,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                MainScreen(onLogout = onLogout)
            }
        }
    }
}