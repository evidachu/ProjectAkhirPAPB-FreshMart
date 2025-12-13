package com.papb.projectakhirandroid.navigation.screen

sealed class Screen(val route: String) {

    object Splash : Screen("splash_screen")

    object OnBoarding : Screen("on_boarding_screen")

    object Search : Screen("search_screen")

    // Rute baru untuk list produk berdasarkan kategori atau "See All"
    object ProductList : Screen("product_list_screen/{title}") {
        fun passTitle(title: String): String = "product_list_screen/$title"
    }

    object Details : Screen("details_screen/{productId}") {
        fun passProductId(productId: Int): String = "details_screen/$productId"
    }

    object Checkout : Screen("checkout_screen")

    object Invoice : Screen("invoice_screen")

}