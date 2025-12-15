package com.papb.projectakhirandroid.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.papb.projectakhirandroid.navigation.screen.BottomNavItemScreen
import com.papb.projectakhirandroid.navigation.screen.Screen
import com.papb.projectakhirandroid.presentation.screen.about.AboutScreen
import com.papb.projectakhirandroid.presentation.screen.cart.CartScreen
import com.papb.projectakhirandroid.presentation.screen.checkout.CheckoutScreen
import com.papb.projectakhirandroid.presentation.screen.collection.AddCollectionScreen
import com.papb.projectakhirandroid.presentation.screen.collection.CollectionScreen
import com.papb.projectakhirandroid.presentation.screen.detail.DetailScreen
import com.papb.projectakhirandroid.presentation.screen.editprofile.EditProfileScreen
import com.papb.projectakhirandroid.presentation.screen.explore.ExploreScreen
import com.papb.projectakhirandroid.presentation.screen.home.HomeScreen
import com.papb.projectakhirandroid.presentation.screen.home.HomeViewModel
import com.papb.projectakhirandroid.presentation.screen.home.clickToCart
import com.papb.projectakhirandroid.presentation.screen.invoice.InvoiceScreen
import com.papb.projectakhirandroid.presentation.screen.komunitas.AddPostScreen
import com.papb.projectakhirandroid.presentation.screen.komunitas.KomunitasScreen
import com.papb.projectakhirandroid.presentation.screen.productlist.ProductListScreen
import com.papb.projectakhirandroid.presentation.screen.search.SearchScreen
import com.papb.projectakhirandroid.utils.Constants

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomNavItemScreen.Home.route,
        modifier = modifier
    ) {

        // ==== Bottom Navigation ====
        composable(BottomNavItemScreen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(BottomNavItemScreen.Explore.route) {
            ExploreScreen(navController = navController)
        }
        composable(BottomNavItemScreen.Cart.route) {
            CartScreen(navController = navController)
        }
        composable(BottomNavItemScreen.Komunitas.route) {
            KomunitasScreen(navController = navController)
        }
        composable(BottomNavItemScreen.About.route) {
            AboutScreen(navController = navController, onLogout = onLogout)
        }

        // ==== Edit Profile ====
        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }

        // ==== Add / Edit Post ====
        composable(
            route = "add_post_screen/{postType}?postId={postId}",
            arguments = listOf(
                navArgument("postType") { type = NavType.StringType },
                navArgument("postId") { type = NavType.LongType; defaultValue = 0L }
            )
        ) { backStackEntry ->
            val postType = backStackEntry.arguments?.getString("postType") ?: "resep"
            val postId = backStackEntry.arguments?.getLong("postId") ?: 0L
            AddPostScreen(
                navController = navController,
                postType = postType,
                postId = postId
            )
        }

        // ==== Checkout & Invoice ====
        composable(Screen.Checkout.route) {
            CheckoutScreen(navController = navController)
        }
        composable(Screen.Invoice.route) {
            InvoiceScreen(navController = navController)
        }

        // ==== Search ====
        composable(
            route = Screen.Search.route,
            arguments = listOf(navArgument("query") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) {
            SearchScreen()
        }

        // ==== Product List ====
        composable(
            route = Screen.ProductList.route,
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { entry ->
            val title = entry.arguments?.getString("title") ?: "Products"
            val homeViewModel: HomeViewModel = hiltViewModel()
            val context = LocalContext.current
            ProductListScreen(
                navController = navController,
                title = title,
                onClickToCart = { productItem ->
                    clickToCart(context, productItem, homeViewModel)
                }
            )
        }

        // ==== Details Nested Graph ====
        detailsNavGraph(navController = navController)
        // ==== Collection Nested Graph ====
        collectionNavGraph(navController = navController)
    }
}

// ==== Details Graph ====
fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = Screen.Details.route
    ) {
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument(Constants.PRODUCT_ARGUMENT_KEY) {
                type = NavType.IntType
            })
        ) {
            DetailScreen()
        }
    }
}

// ==== Collection Graph ====
fun NavGraphBuilder.collectionNavGraph(navController: NavHostController) {
    navigation(
        route = "collection_graph",
        startDestination = Screen.Collection.route
    ) {
        composable(route = Screen.Collection.route) {
            CollectionScreen(navController = navController)
        }
        composable(route = Screen.AddCollection.route) {
            AddCollectionScreen(navController = navController)
        }
    }
}