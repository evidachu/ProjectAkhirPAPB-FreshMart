// File: app/src/main/java/com/papb/projectakhirandroid/navigation/graph/AuthNavGraph.kt

package com.papb.projectakhirandroid.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.papb.projectakhirandroid.navigation.screen.Screen
// ✅ PERBAIKAN: Tambahkan import untuk LoginScreen
import com.papb.projectakhirandroid.presentation.screen.login.LoginScreen // <--- BARIS INI DITAMBAHKAN
// Jika LoginScreen ada di package yang berbeda, sesuaikan 'login.LoginScreen' dengan path yang benar

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.Login.route,
        route = Graph.AUTH
    ) {
        composable(route = Screen.Login.route) {
            // ✅ LoginScreen sekarang dikenali
            LoginScreen(navController = navController)
        }

        // ... rute Register lainnya
    }
}