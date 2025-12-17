package com.papb.projectakhirandroid.presentation.common.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.ui.theme.*
import com.papb.projectakhirandroid.R


@Composable
fun ListContentCart(
    modifier: Modifier = Modifier,
    cartProducts: List<ProductItem>,
    onClickDeleteCart: (ProductItem) -> Unit
) {
    if (cartProducts.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = DIMENS_32dp),
            verticalArrangement = Arrangement.spacedBy(DIMENS_8dp)
        ) {
            items(cartProducts) { items ->
                ContentCart(productItem = items,
                    onClickDeleteCart = { productItem ->
                        onClickDeleteCart.invoke(productItem)
                    }
                )
            }
        }
    } else {
        EmptyContent()
    }
}

@Composable
fun ContentCart(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
    onClickDeleteCart: (ProductItem) -> Unit
) {
    Column {
        Divider(modifier = Modifier.height(DIMENS_1dp), color = GrayBorderStroke)

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = DIMENS_8dp)
        ) {
            
            // Logic for displaying Image: URL vs Placeholder
            if (productItem.image.isNullOrEmpty()) {
                Image(
                    modifier = Modifier
                        .size(width = DIMENS_64dp, height = DIMENS_64dp)
                        .padding(start = DIMENS_8dp),
                    painter = painterResource(id = R.drawable.product1), // Placeholder
                    contentDescription = stringResource(id = R.string.image_product),
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
                    modifier = Modifier
                        .size(width = DIMENS_64dp, height = DIMENS_64dp)
                        .padding(start = DIMENS_8dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                    .padding(start = DIMENS_16dp),
            ) {
                Text(
                    text = productItem.title,
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    fontSize = TEXT_SIZE_16sp
                )

                Spacer(modifier = Modifier.height(DIMENS_4dp))

                // Menampilkan Unit dan Quantity
                Text(
                    text = "${productItem.unit} (${productItem.quantity}x)",
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = GraySecondTextColor,
                    fontSize = TEXT_SIZE_12sp,
                )
            }

            // Menampilkan Total Harga (Harga x Quantity)
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Rp ${(productItem.price * productItem.quantity).toInt()}",
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Bold,
                color = Black,
                fontSize = TEXT_SIZE_18sp,
            )

            Image(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = DIMENS_16dp, end = DIMENS_16dp)
                    .clickable {
                        onClickDeleteCart.invoke(productItem)
                    },
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.image_delete),
                colorFilter = ColorFilter.tint(color = Color.DarkGray)
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentCartPreview() {
    ContentCart(
        productItem = ProductItem(
            id = 1,
            title = "Organic Bananas",
            description = "Apples are nutritious. Apples may be good for weight loss. apples may be good for your heart. As part of a healtful and varied diet.",
            image = null, // Set null for preview
            unit = "7pcs, Priceg",
            price = 4.99,
            quantity = 2, // Simulasi 2 item
            nutritions = "100gr",
            review = 4.0,
            category = "Buah & Sayur"
        ),
        onClickDeleteCart = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ListContentCartPreview() {
    ListContentCart(cartProducts = emptyList(), onClickDeleteCart = {})
}
