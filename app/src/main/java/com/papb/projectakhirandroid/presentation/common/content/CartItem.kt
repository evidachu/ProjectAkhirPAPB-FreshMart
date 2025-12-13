package com.papb.projectakhirandroid.presentation.common.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.ui.theme.*

@Composable
fun ContentCart(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
    onClickDeleteCart: ((ProductItem) -> Unit)? = null
) {
    Column {
        Divider(modifier = Modifier.height(DIMENS_1dp), color = GrayBorderStroke)

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = DIMENS_8dp)
        ) {
            Image(
                modifier = Modifier
                    .size(width = DIMENS_64dp, height = DIMENS_64dp)
                    .padding(start = DIMENS_8dp),
                painter = painterResource(id = productItem.image),
                contentDescription = stringResource(id = R.string.image_product)
            )

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

            if (onClickDeleteCart != null) {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = DIMENS_16dp, end = DIMENS_16dp)
                        .clickable { onClickDeleteCart.invoke(productItem) },
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.image_delete),
                    colorFilter = ColorFilter.tint(color = Color.DarkGray)
                )
            }
        }
    }
}