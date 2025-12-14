// File: app/src/main/java/com/papb/projectakhirandroid/navigation/graph/RootNavigationGraph.kt

package com.papb.projectakhirandroid.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.papb.projectakhirandroid.navigation.graph.Graph
import com.papb.projectakhirandroid.navigation.graph.* import com.papb.projectakhirandroid.presentation.screen.MainScreen


@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH
    ) {
        // 1. Auth Graph (Login, Register)
        authNavGraph(navController)

        // 2. Main Graph (Bottom Nav)
        composable(route = Graph.MAIN) {
            MainScreen()
        }
    }
}