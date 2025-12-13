package com.papb.projectakhirandroid.presentation.common.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.presentation.common.card.ProductCard
import com.papb.projectakhirandroid.ui.theme.* import com.papb.projectakhirandroid.R


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListContentProduct(
    modifier: Modifier = Modifier,
    title: String,
    products: List<ProductItem>,
    navController: NavController,
    onClickToCart: (ProductItem) -> Unit,
    onClickSeeAll: () -> Unit = {},
    isVerticalList: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
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

        // KONTEN PRODUK
        if (isVerticalList) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = DIMENS_8dp, vertical = DIMENS_8dp),
                horizontalArrangement = Arrangement.spacedBy(DIMENS_8dp),
                verticalArrangement = Arrangement.spacedBy(DIMENS_8dp),
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
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(DIMENS_12dp),
                contentPadding = PaddingValues(horizontal = DIMENS_16dp)
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