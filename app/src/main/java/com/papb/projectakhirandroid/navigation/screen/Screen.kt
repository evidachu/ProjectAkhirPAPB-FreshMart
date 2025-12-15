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

    // Komunitas / Add Post
    object AddPost : Screen("add_post_screen/{postType}") {
        fun createRoute(postType: String, postId: Long = 0L): String {
            return "add_post_screen/$postType?postId=$postId"
        }
    }

    // Product list (kategori / see all)
    object ProductList : Screen("product_list_screen/{title}") {
        fun passTitle(title: String) = "product_list_screen/$title"
    }

    // Product details
    object Details : Screen("details_screen/{productId}") {
        fun passProductId(productId: Int) = "details_screen/$productId"
    }

    // Checkout & Invoice
    object Checkout : Screen("checkout_screen")
    object Invoice : Screen("invoice_screen")

    // Collection
    object AddCollection : Screen("add_collection_screen")
}
