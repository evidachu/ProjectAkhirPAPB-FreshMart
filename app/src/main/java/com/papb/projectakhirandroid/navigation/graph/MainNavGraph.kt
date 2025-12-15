package com.papb.projectakhirandroid.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.papb.projectakhirandroid.navigation.screen.BottomNavItemScreen
import com.papb.projectakhirandroid.navigation.screen.Screen
import com.papb.projectakhirandroid.presentation.screen.about.AboutScreen
import com.papb.projectakhirandroid.presentation.screen.cart.CartScreen
import com.papb.projectakhirandroid.presentation.screen.checkout.CheckoutScreen
import com.papb.projectakhirandroid.presentation.screen.detail.DetailScreen
import com.papb.projectakhirandroid.presentation.screen.editprofile.EditProfileScreen
import com.papb.projectakhirandroid.presentation.screen.explore.ExploreScreen
import com.papb.projectakhirandroid.presentation.screen.home.HomeScreen
import com.papb.projectakhirandroid.presentation.screen.home.HomeViewModel
import com.papb.projectakhirandroid.presentation.screen.home.clickToCart
import com.papb.projectakhirandroid.presentation.screen.invoice.InvoiceScreen
import com.papb.projectakhirandroid.presentation.screen.komunitas.KomunitasScreen
import com.papb.projectakhirandroid.presentation.screen.productlist.ProductListScreen
import com.papb.projectakhirandroid.presentation.screen.search.SearchScreen
import com.papb.projectakhirandroid.utils.Constants.PRODUCT_ARGUMENT_KEY

// 🔹 Pastikan object Graph ADA
// object Graph { const val MAIN = "..."; const val DETAILS = "..." }

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomNavItemScreen.Home.route
    ) {

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
            KomunitasScreen()
        }

        composable(BottomNavItemScreen.About.route) {
            AboutScreen(
                navController = navController,
                onLogout = onLogout
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(navController = navController)
        }

        composable(Screen.Invoice.route) {
            InvoiceScreen(navController = navController)
        }

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

        composable(
            route = Screen.ProductList.route,
            arguments = listOf(navArgument("title") {
                type = NavType.StringType
            })
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

        // 🔥 DETAIL GRAPH (TIDAK ERROR)
        detailsNavGraph()
    }
}

/* ============================= */
/* ===== DETAILS NAV GRAPH ===== */
/* ============================= */

fun NavGraphBuilder.detailsNavGraph() {
    navigation(
        route = Graph.DETAILS,
        startDestination = Screen.Details.route
    ) {
        composable(
            route = Screen.Details.route,
            arguments = listOf(
                navArgument(PRODUCT_ARGUMENT_KEY) {
                    type = NavType.IntType
                }
            )
        ) {
            DetailScreen()
        }
    }
}
