package com.papb.projectakhirandroid.presentation.screen.komunitas

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.papb.projectakhirandroid.domain.model.Post
import com.papb.projectakhirandroid.ui.theme.*
import com.papb.projectakhirandroid.utils.Constants
import com.papb.projectakhirandroid.utils.Utils

@Composable
fun AddPostScreen(
    navController: NavController,
    postType: String,
    // ✅ PERBAIKAN: Menggunakan Long untuk konsistensi ID
    postId: Long = 0L,
    viewModel: KomunitasViewModel = hiltViewModel()
) {
    // ⚠️ GANTI DENGAN DATA USER ASLI YANG SUDAH LOGIN
    val currentUserName = "user_yang_sedang_login"

    // Mencari postingan yang akan diedit dari ViewModel. postId sekarang Long.
    val existingPost = viewModel.posts.collectAsState().value.find { it.id == postId }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    // Mengisi form jika dalam mode EDIT
    LaunchedEffect(existingPost) {
        if (existingPost != null) {
            title = existingPost.title
            description = existingPost.description
            selectedImageUri = existingPost.imageUrl
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (postId == 0L) "Tambah Postingan Baru" else "Edit Postingan",
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Black,
                        fontSize = TEXT_SIZE_20sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali", tint = Black)
                    }
                },
                backgroundColor = Color.White,
                elevation = DIMENS_4dp
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = DIMENS_16dp, vertical = DIMENS_16dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(DIMENS_16dp)
        ) {

            // 1. INPUT GAMBAR (Placeholder)
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DIMENS_200dp)
                        .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(DIMENS_12dp))
                        .clickable {
                            Utils.displayToast(context, "Fitur upload gambar belum diimplementasikan.")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "Gambar Postingan",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = "Pilih Gambar",
                            modifier = Modifier.size(DIMENS_64dp),
                            tint = Color.Gray
                        )
                    }
                }
            }

            // 2. JUDUL INPUT FIELD
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        if (it.length <= Constants.MAX_POST_TITLE_LENGTH) title = it
                    },
                    label = {
                        Text("Judul Postingan (Maks ${Constants.MAX_POST_TITLE_LENGTH} karakter)")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Green,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Green,
                        focusedLabelColor = Green,
                        unfocusedLabelColor = Color.Gray,
                        textColor = Black
                    ),
                    textStyle = TextStyle(fontSize = TEXT_SIZE_16sp, fontFamily = GilroyFontFamily),
                    singleLine = true
                )
            }

            // 3. DESKRIPSI INPUT FIELD
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        if (it.length <= Constants.MAX_POST_DESCRIPTION_LENGTH) description = it
                    },
                    label = {
                        Text("Isi Postingan (Maks ${Constants.MAX_POST_DESCRIPTION_LENGTH} karakter)")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DIMENS_150dp),
                    singleLine = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Green,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Green,
                        focusedLabelColor = Green,
                        unfocusedLabelColor = Color.Gray,
                        textColor = Black
                    ),
                    textStyle = TextStyle(fontSize = TEXT_SIZE_14sp, fontFamily = GilroyFontFamily)
                )
            }

            // 4. TOMBOL KIRIM
            item {
                Button(
                    onClick = {
                        if (title.isNotBlank() && description.isNotBlank() && currentUserName.isNotBlank()) {

                            val postToSubmit = existingPost?.copy(
                                title = title,
                                description = description,
                                imageUrl = selectedImageUri,
                            ) ?: Post(
                                // ID bertipe Long
                                id = postId,
                                type = postType,
                                title = title,
                                description = description,
                                imageUrl = selectedImageUri,
                                owner = currentUserName
                            )

                            viewModel.savePost(postToSubmit)
                            Utils.displayToast(context, if (postId == 0L) "Postingan Dibuat!" else "Postingan Diperbarui!")
                            navController.popBackStack()
                        } else {
                            Utils.displayToast(context, "Tolong lengkapi semua bidang.")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DIMENS_48dp),
                    shape = RoundedCornerShape(DIMENS_8dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Green)
                ) {
                    Text(
                        text = if (postId == 0L) "Kirim Postingan" else "Update Postingan",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = TEXT_SIZE_16sp
                    )
                }
            }
        }
    }
}