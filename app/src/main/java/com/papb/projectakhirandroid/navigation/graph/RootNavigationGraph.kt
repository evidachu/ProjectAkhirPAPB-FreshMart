package com.papb.projectakhirandroid.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.papb.projectakhirandroid.navigation.screen.Screen
import com.papb.projectakhirandroid.presentation.screen.MainScreen
import com.papb.projectakhirandroid.presentation.screen.login.LoginScreen
import com.papb.projectakhirandroid.presentation.screen.register.RegisterScreen

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT, 
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        composable(route = Screen.Main.route) {
            MainScreen()
        }
    }
}