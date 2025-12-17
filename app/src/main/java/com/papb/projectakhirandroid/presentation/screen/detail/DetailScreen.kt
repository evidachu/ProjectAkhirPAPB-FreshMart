package com.papb.projectakhirandroid.presentation.screen.detail

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.domain.model.ProductItem
import com.papb.projectakhirandroid.domain.model.Review
import com.papb.projectakhirandroid.presentation.common.SpacerDividerContent
import com.papb.projectakhirandroid.presentation.component.RatingBar
import com.papb.projectakhirandroid.ui.theme.*
import com.papb.projectakhirandroid.utils.ImageUtils
import com.papb.projectakhirandroid.utils.showToastShort
import kotlinx.coroutines.launch
import java.io.File
import java.text.DecimalFormat

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = hiltViewModel(),
) {
    val mContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val selectedProduct by detailViewModel.selectedProduct.collectAsState()
    val reviews by detailViewModel.reviews.collectAsState()
    val isLoading by detailViewModel.isLoading.collectAsState()
    val currentUserId by detailViewModel.currentUserId.collectAsState()
    
    var quantity by remember { mutableStateOf(1) }
    var reviewToEdit by remember { mutableStateOf<Review?>(null) }

    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
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

                        DetailContentDescription(
                            productItem = productItem,
                            reviews = reviews,
                            currentUserId = currentUserId,
                            reviewToEdit = reviewToEdit,
                            onEditReview = { reviewToEdit = it },
                            onDeleteReview = { detailViewModel.deleteReview(it) },
                            onCancelEdit = { reviewToEdit = null },
                            onSubmitReview = { rating, text, imageFile ->
                                if (reviewToEdit == null) {
                                    detailViewModel.submitReview(productItem.id, rating, text, imageFile)
                                    mContext.showToastShort("Ulasan Terkirim!")
                                } else {
                                    detailViewModel.updateReview(
                                        reviewId = reviewToEdit!!.id,
                                        productId = productItem.id,
                                        rating = rating,
                                        reviewText = text,
                                        existingImageUrl = reviewToEdit!!.reviewImageUrl,
                                        newImageFile = imageFile
                                    )
                                    mContext.showToastShort("Ulasan Diperbarui!")
                                    reviewToEdit = null
                                }
                            },
                            onQuantityChange = { newQuantity ->
                                quantity = newQuantity
                            }
                        )
                    }
                }

                Column {
                    selectedProduct?.let {
                        DetailButtonAddCart(
                            productItem = it,
                            quantity = quantity,
                            onClickToCart = { productItem, qty ->
                                mContext.showToastShort("Berhasil Masuk Keranjang: ${productItem.title} ($qty item)")
                                detailViewModel.addCart(productItem.copy(isCart = true, quantity = qty))
                            }
                        )
                    }
                }
            }
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
        if (productItem.image.isNullOrEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.product1),
                contentDescription = stringResource(id = R.string.image_product),
                modifier = Modifier.height(DIMENS_353dp)
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
                modifier = Modifier.height(DIMENS_353dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun DetailContentDescription(
    modifier: Modifier = Modifier,
    productItem: ProductItem,
    reviews: List<Review>,
    currentUserId: String?,
    reviewToEdit: Review?,
    onEditReview: (Review) -> Unit,
    onDeleteReview: (Review) -> Unit,
    onCancelEdit: () -> Unit,
    onSubmitReview: (Int, String, File?) -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    var quantity by remember { mutableStateOf(1) }

    val averageRating = if (reviews.isNotEmpty()) reviews.map { it.rating }.average() else 0.0

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
                    color = Color.Black,
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                        color = Color.Black,
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
                color = Color.Black,
                fontSize = TEXT_SIZE_24sp
            )
        }

        SpacerDividerContent()

        Text(
            text = stringResource(R.string.product_detail),
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
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
                color = Color.Black,
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
                color = Color.Black,
                fontSize = TEXT_SIZE_16sp,
                modifier = Modifier.weight(1f)
            )

            RatingBar(rating = averageRating)

            Spacer(modifier = Modifier.width(DIMENS_8dp))

            Text(
                text = "${DecimalFormat("#.0").format(averageRating)}/5",
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
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

        CustomerReviewSection(
            reviewToEdit = reviewToEdit,
            onReviewSubmitted = onSubmitReview,
            onCancelEdit = onCancelEdit
        )

        Spacer(modifier = Modifier.height(DIMENS_16dp))

        reviews.forEach { review ->
            ReviewItem(
                review = review,
                isOwner = review.userId == currentUserId,
                onEditClick = { onEditReview(review) },
                onDeleteClick = { onDeleteReview(review) }
            )
            Spacer(modifier = Modifier.height(DIMENS_8dp))
        }
    }
}

@Composable
fun CustomerReviewSection(
    reviewToEdit: Review?,
    onReviewSubmitted: (Int, String, File?) -> Unit,
    onCancelEdit: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var rating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) }

    // Menggunakan Photo Picker modern (PickVisualMedia) yang lebih stabil
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            coroutineScope.launch {
                // Memproses file di background thread
                val file = ImageUtils.uriToTempFile(context, uri)
                selectedImageFile = file
                
                if (file == null) {
                    context.showToastShort("Gagal memproses gambar. Coba gambar lain.")
                }
            }
        }
    }
    
    // Use LaunchedEffect to update state when reviewToEdit changes
    LaunchedEffect(reviewToEdit) {
        if (reviewToEdit != null) {
            rating = reviewToEdit.rating
            reviewText = reviewToEdit.reviewText
            selectedImageUri = null
            selectedImageFile = null
        } else {
            // Reset if null (cancelled or finished)
            rating = 0
            reviewText = ""
            selectedImageUri = null
            selectedImageFile = null
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (reviewToEdit == null) "Beri Ulasan" else "Edit Ulasan Anda",
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            fontSize = TEXT_SIZE_16sp,
        )
        Spacer(modifier = Modifier.height(DIMENS_8dp))

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
            label = { Text("Tulis ulasan Anda...", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(DIMENS_16dp))

        // Tampilkan gambar yang sudah dipilih (baru)
        if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(DIMENS_8dp)),
                contentScale = ContentScale.Crop
            )
            TextButton(onClick = { 
                selectedImageUri = null
                selectedImageFile = null
            }) {
                Text("Hapus Gambar", color = Color.Red)
            }
        } else if (reviewToEdit?.reviewImageUrl != null) {
             // Tampilkan gambar existing jika sedang edit dan belum pilih gambar baru
             AsyncImage(
                model = reviewToEdit.reviewImageUrl,
                contentDescription = "Existing review image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(DIMENS_8dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(DIMENS_8dp))

        Row {
            OutlinedButton(
                onClick = { 
                    // Membuka Photo Picker hanya untuk gambar
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            ) {
                Text(text = if (selectedImageUri == null) "Upload Foto" else "Ganti Foto", color = Color.Black)
            }

            Spacer(modifier = Modifier.width(DIMENS_8dp))
            
            Button(
                onClick = {
                    if (rating > 0 && reviewText.isNotBlank()) {
                        onReviewSubmitted(rating, reviewText, selectedImageFile)
                        // Reset form if creating new
                        if (reviewToEdit == null) {
                            rating = 0
                            reviewText = ""
                            selectedImageUri = null
                            selectedImageFile = null
                        }
                    } else {
                         if (rating == 0) context.showToastShort("Mohon beri rating bintang")
                         else if (reviewText.isBlank()) context.showToastShort("Mohon isi ulasan")
                    }
                },
            ) {
                Text(if (reviewToEdit == null) "Kirim Ulasan" else "Update", color = Color.Black)
            }
            
            if (reviewToEdit != null) {
                Spacer(modifier = Modifier.width(DIMENS_8dp))
                OutlinedButton(onClick = onCancelEdit) {
                    Text("Batal", color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun ReviewItem(
    review: Review,
    isOwner: Boolean = false,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    // Logic Profile Picture: Prioritaskan post.ownerAvatarUrl, jika null fallback ke drawable
    val painter = if (review.userProfilePicUrl != null) {
        rememberAsyncImagePainter(
            model = review.userProfilePicUrl,
            placeholder = painterResource(id = R.drawable.profileimage),
            error = painterResource(id = R.drawable.profileimage)
        )
    } else {
        painterResource(id = R.drawable.profileimage)
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        // User Profile Picture
        Image(
            painter = painter,
            contentDescription = "User profile picture",
            modifier = Modifier
                .size(DIMENS_40dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(DIMENS_16dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = review.username,
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = TEXT_SIZE_14sp
                    )
                    RatingBar(rating = review.rating.toDouble())
                }
                
                if (isOwner) {
                    Row {
                        IconButton(onClick = onEditClick, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Green)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = onDeleteClick, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                }
            }
            
            Text(
                text = review.reviewText,
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                fontSize = TEXT_SIZE_12sp
            )
            
            // Review Image (Read Only)
            review.reviewImageUrl?.let {
                Spacer(modifier = Modifier.height(8.dp))
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .crossfade(true)
                        .build(),
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
            image = null, // Set to null for preview
            unit = "7pcs, Priceg",
            price = 4.99,
            nutritions = "100gr",
            review = 4.0,
            category = "Buah & Sayur"
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
            image = null, // Set to null for preview
            unit = "7pcs, Priceg",
            price = 4.99,
            nutritions = "100gr",
            review = 4.0,
            category = "Buah & Sayur"
        ),
        reviews = listOf(
            Review(1, 101, "u1", "John Doe", null, 4, "Great product!"),
            Review(2, 101, "u2", "Jane Smith", null, 5, "Amazing quality!")
        ),
        currentUserId = "u1",
        reviewToEdit = null,
        onEditReview = {},
        onDeleteReview = {},
        onCancelEdit = {},
        onSubmitReview = { _, _, _ -> },
        onQuantityChange = {}
    )
}
