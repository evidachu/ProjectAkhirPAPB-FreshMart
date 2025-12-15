package com.papb.projectakhirandroid.navigation.graph

// FIX: Import the Screen object to resolve the reference
import com.papb.projectakhirandroid.navigation.screen.Screen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.papb.projectakhirandroid.presentation.screen.collection.AddCollectionScreen
import com.papb.projectakhirandroid.presentation.screen.collection.CollectionScreen

fun NavGraphBuilder.collectionGraph(navController: NavController) {
    navigation(
        route = "collection_graph",
        startDestination = Screen.Collection.route // This will now be recognized
    ) {
        composable(route = Screen.Collection.route) { // This will now be recognized
            CollectionScreen(navController = navController)
        }
        composable(route = Screen.AddCollection.route) { // This will now be recognized
            AddCollectionScreen(navController = navController)
        }
    }
}
