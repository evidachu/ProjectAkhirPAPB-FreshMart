package com.papb.projectakhirandroid.presentation.common.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.ExperimentalFoundationApi // Digunakan untuk LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.presentation.common.card.ProductCard
import com.papb.projectakhirandroid.ui.theme.*
import com.papb.projectakhirandroid.R


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListContentProduct(
    modifier: Modifier = Modifier,
    title: String,
    products: List<ProductItem>,
    navController: NavController,
    onClickToCart: (ProductItem) -> Unit,
    onClickSeeAll: () -> Unit = {},
    // Parameter baru untuk mengontrol layout (False = Horizontal Row, True = Vertical Grid 2 Kolom)
    isVerticalList: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // --- Header (Hanya untuk Home Screen) ---
        if (title.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = DIMENS_16dp, end = DIMENS_16dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TEXT_SIZE_24sp,
                    color = Black
                )

                // Teks "Lihat Semua"
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable { onClickSeeAll() },
                    text = stringResource(id = R.string.see_all),
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = TEXT_SIZE_12sp,
                    color = Green
                )
            }
        }

        // --- KONTEN PRODUK ---
        if (isVerticalList) {
            // Scroll Vertikal: 2 produk per baris (LazyVerticalGrid)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 kolom per baris
                contentPadding = PaddingValues(horizontal = DIMENS_16dp, vertical = DIMENS_8dp),
                horizontalArrangement = Arrangement.spacedBy(DIMENS_16dp), // Jarak horizontal
                verticalArrangement = Arrangement.spacedBy(DIMENS_16dp), // Jarak vertikal
                modifier = Modifier.fillMaxSize()
            ) {
                items(products) { product ->
                    ProductCard(
                        productItem = product,
                        navController = navController,
                        onClickToCart = onClickToCart,
                    )
                }
            }
        } else {
            // Scroll Horizontal (default, untuk Home Screen)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(DIMENS_2dp),
                contentPadding = PaddingValues(DIMENS_8dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        productItem = product,
                        navController = navController,
                        onClickToCart = onClickToCart
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListContentProductPreview() {
    ListContentProduct(
        title = "Exclusive Offer",
        products = listOf(
            ProductItem(
                id = 1,
                title = "Organic Bananas",
                description = "",
                image = R.drawable.product10,
                unit = "7pcs, Priceg",
                price = 4.99,
                nutritions = "100gr",
                review = 4.0
            ),
        ),
        navController = rememberNavController(),
        onClickToCart = {}
    )
}
