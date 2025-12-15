package com.papb.projectakhirandroid.presentation.screen.home

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi // Import OptIn
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.navigation.screen.Screen
import com.papb.projectakhirandroid.presentation.common.content.ListContentProduct
import com.papb.projectakhirandroid.presentation.component.SearchViewBar
import com.papb.projectakhirandroid.presentation.component.SliderBanner
import com.papb.projectakhirandroid.ui.theme.DIMENS_24dp
import com.papb.projectakhirandroid.utils.DataDummy
import com.papb.projectakhirandroid.utils.showToastShort

@OptIn(ExperimentalPagerApi::class) // Ganti dengan OptIn
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val mContext = LocalContext.current
    val searchQuery by homeViewModel.searchQuery
    val allProducts by homeViewModel.productList.collectAsState()

    HomeContent(
        modifier = modifier,
        navController = navController,
        searchQuery = searchQuery,
        allProducts = allProducts,
        onSearchValueChange = {
            if (it.isNotEmpty()) navController.navigate(Screen.Search.route)
        },
        onClickToCart = { productItem ->
            clickToCart(mContext, productItem, homeViewModel)
        }
    )
}

@OptIn(ExperimentalPagerApi::class) // Ganti dengan OptIn
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    searchQuery: String,
    allProducts: List<ProductItem>,
    onSearchValueChange: (String) -> Unit,
    onClickToCart: (ProductItem) -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            SearchViewBar(
                hint = stringResource(id = R.string.search_store),
                query = searchQuery,
                onValueChange = onSearchValueChange
            )

            SliderBanner()

            Spacer(modifier = Modifier.height(DIMENS_24dp))

            // REKOMENDASI
            val rekomendasiTitle = "Rekomendasi"
            ListContentProduct(
                title = rekomendasiTitle,
                // Batasi produk menjadi 5
                products = allProducts.take(5),
                navController = navController,
                onClickToCart = onClickToCart,
                onClickSeeAll = {
                    navController.navigate(Screen.ProductList.passTitle(rekomendasiTitle))
                }
            )

            Spacer(modifier = Modifier.height(DIMENS_24dp))

            // TERLARIS
            val terlarisTitle = "Terlaris"
            ListContentProduct(
                title = terlarisTitle,
                // Batasi produk menjadi 5
                products = allProducts.sortedByDescending { it.id }.take(5),
                navController = navController,
                onClickToCart = onClickToCart,
                onClickSeeAll = {
                    // Navigasi dengan judul "Terlaris"
                    navController.navigate(Screen.ProductList.passTitle(terlarisTitle))
                }
            )

            Spacer(modifier = Modifier.height(DIMENS_24dp))
        }
    }
}

fun clickToCart(context: Context, productItem: ProductItem, viewModel: HomeViewModel) {
    // Memberikan feedback ke pengguna
    context.showToastShort("Berhasil Masuk Keranjang: ${productItem.title}")
    // Menambahkan produk ke keranjang
    viewModel.addCart(productItem.copy(isCart = true))
}

// Preview HeaderLocationHome dihapus

@OptIn(ExperimentalPagerApi::class) // Ganti dengan OptIn
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Menggunakan DataDummy untuk preview
    HomeContent(
        navController = rememberNavController(),
        searchQuery = "",
        allProducts = DataDummy.generateDummyProduct(),
        onSearchValueChange = {},
        onClickToCart = {}
    )
}
