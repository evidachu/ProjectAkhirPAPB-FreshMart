package com.papb.projectakhirandroid.presentation.common.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.navigation.screen.Screen
import com.papb.projectakhirandroid.ui.theme.*

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
    navController: NavController,
    onClickToCart: (ProductItem) -> Unit
) {
    Card(
        shape = RoundedCornerShape(DIMENS_12dp),
        border = BorderStroke(width = 1.dp, color = GrayBorderStroke),
        modifier = modifier
            .width(DIMENS_174dp)
            .clickable {
                navController.navigate(Screen.Details.passProductId(productId = productItem.id))
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DIMENS_12dp)
        ) {
            // Logic for displaying Image: URL vs Placeholder
            if (productItem.image.isNullOrEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.product1), // Default placeholder
                    contentDescription = stringResource(R.string.image_product),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .height(DIMENS_80dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(productItem.image)
                        .crossfade(true)
                        .placeholder(R.drawable.product1) // Placeholder while loading
                        .error(R.drawable.product1) // Fallback if error
                        .build(),
                    contentDescription = stringResource(R.string.image_product),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .height(DIMENS_80dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(DIMENS_24dp))

            Text(
                text = productItem.title,
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Bold,
                color = Black,
                fontSize = TEXT_SIZE_16sp,
                maxLines = 1 // Limit title lines
            )

            Spacer(modifier = Modifier.height(DIMENS_6dp))

            Text(
                text = productItem.unit,
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Medium,
                color = GraySecondTextColor,
                fontSize = TEXT_SIZE_12sp
            )

            Spacer(modifier = Modifier.height(DIMENS_20dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Rp ${productItem.price.toInt()}",
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    fontSize = TEXT_SIZE_18sp
                )

                Button(
                    modifier = Modifier.size(DIMENS_46dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                    shape = RoundedCornerShape(DIMENS_14dp),
                    contentPadding = PaddingValues(DIMENS_10dp),
                    onClick = {
                        onClickToCart.invoke(productItem)
                    }
                )
                {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Default.Add,
                        tint = Color.White,
                        contentDescription = stringResource(id = R.string.add)
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemProductPreview() {
    ProductCard(
        productItem = ProductItem(
            id = 1,
            title = "Organic Bananas",
            description = "",
            image = null, // Preview with null image
            unit = "7pcs, Priceg",
            price = 4.99,
            nutritions = "100gr",
            review = 4.0,
            category = "Buah & Sayur"
        ),
        navController = rememberNavController(),
        onClickToCart = {}
    )
}
