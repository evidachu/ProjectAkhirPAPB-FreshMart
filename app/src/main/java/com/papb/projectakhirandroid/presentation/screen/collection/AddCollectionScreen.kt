package com.papb.projectakhirandroid.presentation.screen.collection

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.papb.projectakhirandroid.ui.theme.Green
import com.papb.projectakhirandroid.utils.ImageUtils
import com.papb.projectakhirandroid.utils.showToastShort
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun AddCollectionScreen(
    navController: NavController
) {
    // Explicitly get the ViewModel scoped to the collection graph
    val collectionGraphEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry("collection_graph")
    }
    val viewModel: CollectionViewModel = hiltViewModel(collectionGraphEntry)
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) }
    
    val isLoading by viewModel.isLoading.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            coroutineScope.launch {
                val file = ImageUtils.uriToTempFile(context, uri)
                selectedImageFile = file
                if (file == null) {
                    context.showToastShort("Gagal memproses gambar")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Koleksi") },
                backgroundColor = Green,
                contentColor = Color.Black
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Koleksi", color = Color.Black) }, 
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Green,
                    focusedBorderColor = Green,
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            // Image Upload Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Gambar Koleksi (Opsional)",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

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
                } else {
                    OutlinedButton(
                        onClick = { 
                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Green)
                    ) {
                        Text("Pilih Gambar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val isButtonEnabled = name.isNotBlank() && !isLoading

            Button(
                onClick = {
                    viewModel.addCollection(name, selectedImageFile)
                    navController.popBackStack()
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Green)
            ) {
                Text(if (isLoading) "Menyimpan..." else "Simpan Koleksi", color = Color.White)
            }
        }
    }
}
