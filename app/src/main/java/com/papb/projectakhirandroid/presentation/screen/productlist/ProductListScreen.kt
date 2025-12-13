package com.papb.projectakhirandroid.presentation.screen.productlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.presentation.common.content.ListContentProduct
import com.papb.projectakhirandroid.ui.theme.GilroyFontFamily
import com.papb.projectakhirandroid.ui.theme.TEXT_SIZE_18sp
import com.papb.projectakhirandroid.utils.DataDummy

@Composable
fun ProductListScreen(
    navController: NavController,
    title: String,
    onClickToCart: (ProductItem) -> Unit
) {
    // --- Simulasi Data & Filtering ---
    val allProducts = DataDummy.generateDummyProduct()

    val filteredProducts = when (title) {
        "Rekomendasi" -> allProducts.filter { it.id % 2 != 0 }
        "Terlaris" -> allProducts.sortedByDescending { it.id }
        else -> allProducts
    }
    // --- Akhir Simulasi Data ---

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = TEXT_SIZE_18sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.Black
                        )
                    }
                },
                backgroundColor = Color.White,
                elevation = 0.dp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Memanggil ListContentProduct dengan isVerticalList = true
            ListContentProduct(
                title = "", // Judul di TopAppBar
                products = filteredProducts,
                navController = navController,
                onClickToCart = onClickToCart,
                onClickSeeAll = {},
                isVerticalList = true // MENGAKTIFKAN TAMPILAN GRID 2 KOLOM VERTIKAL
            )
        }
    }
}
