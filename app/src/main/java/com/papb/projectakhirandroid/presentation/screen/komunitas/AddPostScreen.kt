package com.papb.projectakhirandroid.presentation.screen.komunitas

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.papb.projectakhirandroid.ui.theme.*
import com.papb.projectakhirandroid.utils.Constants
import com.papb.projectakhirandroid.utils.ImageUtils
import com.papb.projectakhirandroid.utils.Utils
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun AddPostScreen(
    navController: NavController,
    postType: String,
    postId: Long = 0L,
    viewModel: KomunitasViewModel = hiltViewModel()
) {
    val existingPost = viewModel.posts.collectAsState().value.find { it.id == postId }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var existingImageUrl by remember { mutableStateOf<String?>(null) }
    
    // State untuk upload gambar
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()

    // Setup Photo Picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            coroutineScope.launch {
                val file = ImageUtils.uriToTempFile(context, uri)
                selectedImageFile = file
                if (file == null) {
                     Utils.displayToast(context, "Gagal memproses gambar")
                }
            }
        }
    }

    LaunchedEffect(existingPost) {
        if (existingPost != null) {
            title = existingPost.title
            description = existingPost.description
            existingImageUrl = existingPost.imageUrl 
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
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = DIMENS_16dp, vertical = DIMENS_16dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(DIMENS_16dp)
            ) {

                // 1. JUDUL INPUT FIELD
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            if (it.length <= Constants.MAX_POST_TITLE_LENGTH) title = it
                        },
                        label = {
                            Text(
                                text = "Judul Postingan (Maks ${Constants.MAX_POST_TITLE_LENGTH} karakter)",
                                color = Black
                            )
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

                // 2. DESKRIPSI INPUT FIELD
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            if (it.length <= Constants.MAX_POST_DESCRIPTION_LENGTH) description = it
                        },
                        label = {
                            Text("Isi Postingan (Maks ${Constants.MAX_POST_DESCRIPTION_LENGTH} karakter)",
                            color = Black
                            )
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

                // 3. UPLOAD GAMBAR SECTION
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Gambar (Opsional)",
                            fontFamily = GilroyFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Black,
                            fontSize = TEXT_SIZE_14sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Preview Gambar
                        if (selectedImageUri != null) {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = "Selected Image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                // Tombol Hapus Gambar
                                Button(
                                    onClick = {
                                        selectedImageUri = null
                                        selectedImageFile = null
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                                ) {
                                    Text("Hapus", color = Color.White, fontSize = 12.sp)
                                }
                            }
                        } else if (existingImageUrl != null) {
                             // Preview Gambar Existing (jika edit)
                             Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                                AsyncImage(
                                    model = existingImageUrl,
                                    contentDescription = "Existing Image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                // Saat edit, jika user pilih gambar baru, gambar lama otomatis terganti
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Pilih gambar baru untuk mengganti", color = Color.Gray, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Tombol Pilih Gambar
                        OutlinedButton(
                            onClick = { 
                                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Green)
                        ) {
                            Text("Pilih Gambar dari Galeri")
                        }
                    }
                }

                // 4. TOMBOL KIRIM
                item {
                    Button(
                        onClick = {
                            if (title.isNotBlank() && description.isNotBlank()) {
                                if (postId == 0L) {
                                    viewModel.createPost(
                                        title = title,
                                        description = description,
                                        type = postType,
                                        imageFile = selectedImageFile
                                    )
                                    Utils.displayToast(context, "Memposting...")
                                } else {
                                    viewModel.updatePost(
                                        id = postId, 
                                        title = title,
                                        description = description,
                                        existingImageUrl = existingImageUrl,
                                        newImageFile = selectedImageFile
                                    )
                                    Utils.displayToast(context, "Memperbarui...")
                                }
                                navController.popBackStack()
                            } else {
                                Utils.displayToast(context, "Tolong lengkapi judul dan deskripsi.")
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DIMENS_48dp),
                        shape = RoundedCornerShape(DIMENS_8dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Green)
                    ) {
                        Text(
                            text = if (isLoading) "Loading..." else if (postId == 0L) "Kirim Postingan" else "Update Postingan",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = TEXT_SIZE_16sp
                        )
                    }
                }
            }
            
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Green
                )
            }
        }
    }
}
