package com.papb.projectakhirandroid.presentation.screen.komunitas

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.papb.projectakhirandroid.ui.theme.* // =========================================================================
//                  KOMPONEN 1: UploadImageArea
// =========================================================================

@Composable
fun UploadImageArea(
    selectedImageUri: Uri?,
    onImagePicked: () -> Unit,
    onRemoveImage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            // Asumsi DIMENS_160dp, DIMENS_200dp, DIMENS_12dp, DIMENS_2dp, dan GilroyFontFamily sudah terimpor di theme.*
            .height(if (selectedImageUri == null) DIMENS_160dp else DIMENS_200dp),
        shape = RoundedCornerShape(DIMENS_12dp),
        elevation = DIMENS_2dp
    ) {
        if (selectedImageUri == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        DIMENS_2dp,
                        Color.LightGray.copy(alpha = 0.7f),
                        RoundedCornerShape(DIMENS_12dp)
                    )
                    .clickable { onImagePicked() },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.CloudUpload,
                    contentDescription = "Upload Gambar",
                    modifier = Modifier.size(DIMENS_48dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.height(DIMENS_8dp))
                Text(
                    "Ketuk untuk Upload Gambar Masakan Anda",
                    color = Color.Gray,
                    fontFamily = GilroyFontFamily
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                val painter = rememberAsyncImagePainter(model = selectedImageUri)

                Image(
                    painter = painter,
                    contentDescription = "Gambar Terpilih",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = onRemoveImage,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(DIMENS_8dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Hapus Gambar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

// =========================================================================
//                  KOMPONEN 2: Preview
// =========================================================================

@Preview(showBackground = true)
@Composable
fun AddPostScreenPreview() {
    // Preview ini sekarang ditempatkan di sini
    AddPostScreen(navController = rememberNavController(), postType = "resep")
}