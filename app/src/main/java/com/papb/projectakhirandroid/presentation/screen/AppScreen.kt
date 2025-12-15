package com.papb.projectakhirandroid.presentation.screen

sealed class Screen(val route: String) {
    // Rute Umum
    data object Details : Screen("details_screen/{productId}")
    data object Search : Screen("search_screen?query={query}")
    data object ProductList : Screen("product_list_screen/{title}")
    data object Checkout : Screen("checkout_screen")
    data object Invoice : Screen("invoice_screen")
    data object EditProfile : Screen("edit_profile_screen")

    // Rute Komunitas dengan Argument Tipe
    data object AddPost : Screen("add_post_screen/{type}") {
        fun createRoute(type: String) = "add_post_screen/$type"
    }

    // Fungsi untuk membuat rute Detail dengan parameter
    fun createRouteWithProductId(productId: Int): String {
        return "details_screen/$productId"
    }
}