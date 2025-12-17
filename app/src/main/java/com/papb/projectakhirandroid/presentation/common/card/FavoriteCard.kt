package com.papb.projectakhirandroid.presentation.common.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.ui.theme.*
import com.papb.projectakhirandroid.R


@Composable
fun FavoriteCard(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .padding(DIMENS_16dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // LOGIKA GAMBAR: Cek apakah URL ada. Jika ya, load dari internet (Coil). Jika tidak, pakai placeholder.
            if (productItem.image.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.product1), // Default Placeholder
                    contentDescription = stringResource(id = R.string.image_product),
                    modifier = Modifier.size(80.dp), // Beri ukuran tetap agar rapi
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(productItem.image)
                        .crossfade(true)
                        .placeholder(R.drawable.product1)
                        .error(R.drawable.product1)
                        .build(),
                    contentDescription = stringResource(id = R.string.image_product),
                    modifier = Modifier.size(80.dp), // Beri ukuran tetap agar rapi
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.padding(start = DIMENS_16dp))

            Column(
                modifier = Modifier
                    .weight(1F)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = productItem.title,
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    fontSize = TEXT_SIZE_16sp,
                )

                Spacer(modifier = Modifier.height(DIMENS_6dp))

                Text(
                    text = productItem.unit,
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = GraySecondTextColor,
                )
            }

            Row(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Rp ${productItem.price.toInt()}",
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    fontSize = TEXT_SIZE_16sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.arrow_right)
                )
            }
        }

        Spacer(modifier = Modifier.height(DIMENS_20dp))

        Divider(modifier = Modifier.height(DIMENS_1dp), color = GrayBorderStroke)
    }
}

@Preview
@Composable
fun FavoriteCardPreview() {
    FavoriteCard(
        productItem = ProductItem(
            id = 1,
            title = "Organic Bananas",
            description = "",
            image = null, // Set null untuk preview agar tidak error type mismatch
            unit = "7pcs, Priceg",
            price = 4.99,
            nutritions = "100gr",
            review = 4.0,
            category = "Buah & Sayur"
        )
    )
}
