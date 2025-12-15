package com.papb.projectakhirandroid.navigation.screen

sealed class Screen(val route: String) {

    /* ================= GRAPH ================= */

    object Root : Screen("root_graph")
    object Auth : Screen("auth_graph")
    object Main : Screen("main_graph")

    /* ================= AUTH ================= */

    object Login : Screen("login_screen")
    object Register : Screen("register_screen")

    /* ================= MAIN ================= */

    object Home : Screen("home_screen")
    object Search : Screen("search_screen")
    object Collection : Screen("collection_screen")
    object EditProfile : Screen("edit_profile_screen")

    // Rute Komunitas: Tambahkan AddPost
    object AddPost : Screen("add_post_screen/{postType}") {
        fun createRoute(postType: String): String = "add_post_screen/$postType"
    }

    // Rute baru untuk list produk berdasarkan kategori atau \"See All\"
    object ProductList : Screen("product_list_screen/{title}") {
        fun passTitle(title: String) = "product_list_screen/$title"
    }

    object Details : Screen("details_screen/{productId}") {
        fun passProductId(productId: Int): String = "details_screen/$productId"
    }

    object Checkout : Screen("checkout_screen")
    object Invoice : Screen("invoice_screen")

    object EditProfile : Screen("edit_profile_screen")

    object Collection : Screen("collection_screen")

    object AddCollection : Screen("add_collection_screen")
}