// File: app/src/main/java/com/papb/projectakhirandroid/navigation/graph/MainNavGraph.kt

package com.papb.projectakhirandroid.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.papb.projectakhirandroid.navigation.screen.BottomNavItemScreen
import com.papb.projectakhirandroid.navigation.screen.Screen
import com.papb.projectakhirandroid.presentation.screen.about.AboutScreen
import com.papb.projectakhirandroid.presentation.screen.cart.CartScreen
import com.papb.projectakhirandroid.presentation.screen.checkout.CheckoutScreen
import com.papb.projectakhirandroid.presentation.screen.detail.DetailScreen
import com.papb.projectakhirandroid.presentation.screen.editprofile.EditProfileScreen
import com.papb.projectakhirandroid.presentation.screen.explore.ExploreScreen
import com.papb.projectakhirandroid.presentation.screen.home.HomeViewModel
import com.papb.projectakhirandroid.presentation.screen.home.HomeScreen
import com.papb.projectakhirandroid.presentation.screen.home.clickToCart
import com.papb.projectakhirandroid.presentation.screen.invoice.InvoiceScreen
import com.papb.projectakhirandroid.presentation.screen.komunitas.AddPostScreen
import com.papb.projectakhirandroid.presentation.screen.komunitas.KomunitasScreen
import com.papb.projectakhirandroid.presentation.screen.productlist.ProductListScreen
import com.papb.projectakhirandroid.presentation.screen.search.SearchScreen
import com.papb.projectakhirandroid.utils.Constants.PRODUCT_ARGUMENT_KEY

// 🚨 IMPORT TAMBAHAN UNTUK SHARED VIEWMODEL
import com.papb.projectakhirandroid.presentation.screen.komunitas.KomunitasViewModel
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry

// ✅ PERBAIKAN: Import Graph agar MAIN dan DETAILS dikenali
import com.papb.projectakhirandroid.navigation.graph.Graph

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

        // --- Komunitas Screen (Induk - Inisialisasi Shared ViewModel) ---
        composable(route = BottomNavItemScreen.Komunitas.route) { backStackEntry ->
            // ✅ FIX 1: Inisialisasi ViewModel terikat ke NavBackStackEntry ini
            val komunitasViewModel: KomunitasViewModel = hiltViewModel(backStackEntry)

            KomunitasScreen(
                navController = navController,
                viewModel = komunitasViewModel // ⬅️ Teruskan instance
            )
        }

        composable(route = BottomNavItemScreen.About.route) {
            AboutScreen(navController = navController)
        }

        // ==== Edit Profile ====
        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }

        // --- ADD/EDIT POST SCREEN (Anak - Mengambil Shared ViewModel) ---
        composable(
            route = "${Screen.AddPost.route}?postType={postType}&postId={postId}",
            arguments = listOf(
                navArgument("postType") {
                    type = NavType.StringType
                    defaultValue = "resep"
                    nullable = true
                },
                navArgument("postId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { backStackEntry ->
            val postType = backStackEntry.arguments?.getString("postType") ?: "resep"
            val postId = backStackEntry.arguments?.getLong("postId") ?: 0L

            // ✅ FIX 2: Ambil instance ViewModel yang SAMA dari rute Komunitas
            val parentEntry: NavBackStackEntry = remember(backStackEntry) {
                // Mendapatkan NavBackStackEntry dari KomunitasScreen
                navController.getBackStackEntry(BottomNavItemScreen.Komunitas.route)
            }

            // Menggunakan hiltViewModel dengan parentEntry memastikan instance yang dishare
            val sharedViewModel: KomunitasViewModel = hiltViewModel(parentEntry)

            AddPostScreen(
                navController = navController,
                postType = postType,
                postId = postId,
                viewModel = sharedViewModel // ⬅️ Teruskan instance yang dishare
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