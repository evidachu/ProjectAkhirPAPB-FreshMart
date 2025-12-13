package com.papb.projectakhirandroid.presentation.screen.detail

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.domain.model.Review
import com.papb.projectakhirandroid.presentation.common.SpacerDividerContent
import com.papb.projectakhirandroid.presentation.component.RatingBar
import com.papb.projectakhirandroid.ui.theme.Black
import com.papb.projectakhirandroid.ui.theme.DIMENS_12dp
import com.papb.projectakhirandroid.ui.theme.DIMENS_16dp
import com.papb.projectakhirandroid.ui.theme.DIMENS_1dp
import com.papb.projectakhirandroid.ui.theme.DIMENS_24dp
import com.papb.projectakhirandroid.ui.theme.DIMENS_353dp
import com.papb.projectakhirandroid.ui.theme.DIMENS_40dp
import com.papb.projectakhirandroid.ui.theme.DIMENS_4dp
import com.papb.projectakhirandroid.ui.theme.DIMENS_6dp
import com.papb.projectakhirandroid.ui.theme.DIMENS_8dp
import com.papb.projectakhirandroid.ui.theme.GilroyFontFamily
import com.papb.projectakhirandroid.ui.theme.GrayBackground
import com.papb.projectakhirandroid.ui.theme.GrayBackgroundSecond
import com.papb.projectakhirandroid.ui.theme.GrayBorderStroke
import com.papb.projectakhirandroid.ui.theme.GraySecondTextColor
import com.papb.projectakhirandroid.ui.theme.Green
import com.papb.projectakhirandroid.ui.theme.TEXT_SIZE_10sp
import com.papb.projectakhirandroid.ui.theme.TEXT_SIZE_12sp
import com.papb.projectakhirandroid.ui.theme.TEXT_SIZE_14sp
import com.papb.projectakhirandroid.ui.theme.TEXT_SIZE_16sp
import com.papb.projectakhirandroid.ui.theme.TEXT_SIZE_18sp
import com.papb.projectakhirandroid.ui.theme.TEXT_SIZE_24sp
import com.papb.projectakhirandroid.utils.showToastShort
import java.text.DecimalFormat

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = hiltViewModel(),
) {
    val mContext = LocalContext.current
    val selectedProduct by detailViewModel.selectedProduct.collectAsState()
    var quantity by remember { mutableStateOf(1) }

    Scaffold { padding ->
        Column {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
                    .padding(padding)
            ) {
                selectedProduct?.let { productItem ->
                    DetailContentImageHeader(productItem = productItem)

                    Spacer(modifier = Modifier.height(DIMENS_24dp))

                    DetailContentDescription(productItem = productItem) { newQuantity ->
                        quantity = newQuantity
                    }
                }
            }

            Column {
                selectedProduct?.let {
                    DetailButtonAddCart(
                        productItem = it,
                        quantity = quantity,
                        onClickToCart = { productItem, qty ->
                            mContext.showToastShort("Berhasil Masuk Keranjang: ${productItem.title} ($qty item)")
                            // Update quantity sebelum masuk cart
                            detailViewModel.addCart(productItem.copy(isCart = true, quantity = qty))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DetailContentImageHeader(
    productItem: ProductItem
) {
    Card(
        shape = RoundedCornerShape(bottomEnd = DIMENS_24dp, bottomStart = DIMENS_24dp),
        backgroundColor = GrayBackground,
        modifier = Modifier
            .blur(DIMENS_1dp)
            .fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = productItem.image),
            contentDescription = stringResource(id = R.string.image_product),
            modifier = Modifier.height(DIMENS_353dp)
        )
    }
}

@Composable
fun DetailContentDescription(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
    onQuantityChange: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf(1) }
    var reviews by remember { mutableStateOf(productItem.reviews) }
    var averageRating by remember { mutableStateOf(productItem.review) }

    Column(
        modifier = modifier.padding(start = DIMENS_16dp, end = DIMENS_16dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = productItem.title,
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    fontSize = TEXT_SIZE_24sp
                )

                Spacer(modifier = Modifier.height(DIMENS_6dp))

                Text(
                    text = productItem.unit,
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = GraySecondTextColor,
                    fontSize = TEXT_SIZE_12sp,
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_favorite_border),
                contentDescription = stringResource(R.string.image_favorite),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(DIMENS_8dp))

        // Price and Quantity Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Quantity Controller
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (quantity > 1) {
                            quantity--
                            onQuantityChange(quantity)
                        }
                    }
                ) {
                    Text(
                        text = "-",
                        fontSize = TEXT_SIZE_24sp,
                        color = if (quantity > 1) Green else GraySecondTextColor
                    )
                }

                Card(
                    shape = RoundedCornerShape(DIMENS_12dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GrayBorderStroke),
                    modifier = Modifier.padding(horizontal = DIMENS_8dp)
                ) {
                    Text(
                        text = quantity.toString(),
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TEXT_SIZE_18sp,
                        color = Black, // Changed to Black
                        modifier = Modifier.padding(horizontal = DIMENS_16dp, vertical = DIMENS_8dp)
                    )
                }

                IconButton(
                    onClick = {
                        quantity++
                        onQuantityChange(quantity)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = Green
                    )
                }
            }

            Text(
                text = "Rp ${(productItem.price * quantity).toInt()}",
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Bold,
                color = Black,
                fontSize = TEXT_SIZE_24sp
            )
        }

        SpacerDividerContent()

        Text(
            text = stringResource(R.string.product_detail),
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = Black,
            fontSize = TEXT_SIZE_16sp,
        )

        Spacer(modifier = Modifier.height(DIMENS_8dp))

        Text(
            text = productItem.description,
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.Medium,
            color = GraySecondTextColor,
            fontSize = TEXT_SIZE_12sp,
        )

        Spacer(modifier = Modifier.height(DIMENS_16dp))
        SpacerDividerContent()

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.nutritions),
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Black,
                fontSize = TEXT_SIZE_16sp,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            Card(
                shape = RoundedCornerShape(DIMENS_6dp),
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = productItem.nutritions,
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = GraySecondTextColor,
                    fontSize = TEXT_SIZE_10sp,
                    modifier = Modifier
                        .background(color = GrayBackgroundSecond)
                        .padding(DIMENS_4dp)
                )
            }

            Spacer(modifier = Modifier.width(DIMENS_8dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.arrow_right)
            )
        }

        SpacerDividerContent()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.review),
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Black,
                fontSize = TEXT_SIZE_16sp,
                modifier = Modifier.weight(1f)
            )

            RatingBar(rating = averageRating)

            Spacer(modifier = Modifier.width(DIMENS_8dp))

            Text(
                text = "${DecimalFormat("#.0").format(averageRating)}/5",
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Black,
                fontSize = TEXT_SIZE_14sp
            )

            Spacer(modifier = Modifier.width(DIMENS_4dp))

            Text(
                text = "(${reviews.size} ulasan)",
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Normal,
                color = GraySecondTextColor,
                fontSize = TEXT_SIZE_12sp
            )

            Spacer(modifier = Modifier.width(DIMENS_8dp))

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.arrow_right)
            )
        }

        SpacerDividerContent()

        CustomerReviewSection(onReviewSubmitted = { newReview ->
            reviews = reviews + newReview
            val totalRating = reviews.sumOf { it.rating }
            averageRating = totalRating.toDouble() / reviews.size
        })

        Spacer(modifier = Modifier.height(DIMENS_16dp))

        reviews.forEach { review ->
            ReviewItem(review = review)
            Spacer(modifier = Modifier.height(DIMENS_8dp))
        }
    }
}

@Composable
fun CustomerReviewSection(onReviewSubmitted: (Review) -> Unit) {
    var rating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri?.toString()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Beri Ulasan",
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = Black,
            fontSize = TEXT_SIZE_16sp,
        )
        Spacer(modifier = Modifier.height(DIMENS_8dp))

        // Interactive Rating Bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            (1..5).forEach { index ->
                IconButton(onClick = { rating = index }) {
                    Icon(
                        painter = painterResource(id = if (index <= rating) R.drawable.ic_star else R.drawable.ic_star_outline),
                        contentDescription = "rating",
                        tint = if (index <= rating) Color.Yellow else GraySecondTextColor
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(DIMENS_16dp))

        OutlinedTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            label = { Text("Tulis ulasan Anda...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(DIMENS_16dp))

        if (selectedImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(selectedImageUri),
                contentDescription = "Selected image preview",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(DIMENS_8dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(DIMENS_8dp))

        Row {
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Upload Foto")
            }
            Spacer(modifier = Modifier.width(DIMENS_8dp))
            Button(
                onClick = {
                    val newReview = Review(
                        id = (0..1000).random(),
                        username = "Pengguna",
                        userProfilePic = R.drawable.profile_picture_placeholder, // Replace with actual user data
                        rating = rating,
                        reviewText = reviewText,
                        reviewImage = selectedImageUri
                    )
                    onReviewSubmitted(newReview)
                    context.showToastShort("Ulasan Terkirim!")

                    // Reset fields
                    rating = 0
                    reviewText = ""
                    selectedImageUri = null
                },
            ) {
                Text("Kirim Ulasan")
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = review.userProfilePic),
            contentDescription = "User profile picture",
            modifier = Modifier
                .size(DIMENS_40dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(DIMENS_8dp))
        Column {
            Text(
                text = review.username,
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Bold,
                color = Black,
                fontSize = TEXT_SIZE_14sp
            )
            RatingBar(rating = review.rating.toDouble())
            Text(
                text = review.reviewText,
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Normal,
                color = Black,
                fontSize = TEXT_SIZE_12sp
            )
            review.reviewImage?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Review image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(DIMENS_8dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun DetailButtonAddCart(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
    quantity: Int,
    onClickToCart: (ProductItem, Int) -> Unit
) {
    Button(
        shape = RoundedCornerShape(DIMENS_24dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Green),
        modifier = modifier
            .fillMaxWidth()
            .padding(DIMENS_16dp),
        onClick = { onClickToCart.invoke(productItem, quantity) }
    ) {
        Text(
            text = stringResource(R.string.add_to_cart),
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = TEXT_SIZE_18sp,
            color = Color.White,
            modifier = Modifier.padding(top = DIMENS_8dp, bottom = DIMENS_8dp)
        )
    }
}


@Preview
@Composable
fun DetailScreenImageHeaderPreview() {
    DetailContentImageHeader(
        ProductItem(
            id = 1,
            title = "Organic Bananas",
            description = "Apples are nutritious. Apples may be good for weight loss. apples may be good for your heart. As part of a healtful and varied diet.",
            image = R.drawable.product2,
            unit = "7pcs, Priceg",
            price = 4.99,
            nutritions = "100gr",
            review = 4.0
        )
    )
}

@Preview
@Composable
fun DetailContentDescriptionPreview() {
    DetailContentDescription(
        productItem = ProductItem(
            id = 1,
            title = "Organic Bananas",
            description = "Apples are nutritious. Apples may be good for weight loss. apples may be good for your heart. As part of a healtful and varied diet.",
            image = R.drawable.product2,
            unit = "7pcs, Priceg",
            price = 4.99,
            nutritions = "100gr",
            review = 4.0,
            reviews = listOf(
                Review(1, "John Doe", R.drawable.profile_picture_placeholder, 4, "Great product!"),
                Review(2, "Jane Smith", R.drawable.profile_picture_placeholder, 5, "Amazing quality!")
            )
        ),
        onQuantityChange = {}
    )
}
