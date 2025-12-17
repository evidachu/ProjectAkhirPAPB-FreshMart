package com.papb.projectakhirandroid.presentation.screen.editprofile

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.presentation.screen.about.ProfileViewModel
import com.papb.projectakhirandroid.ui.theme.GilroyFontFamily
import com.papb.projectakhirandroid.ui.theme.Green
import com.papb.projectakhirandroid.ui.theme.TEXT_SIZE_18sp
import com.papb.projectakhirandroid.utils.showToastShort

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val initialName by profileViewModel.name.collectAsState()
    val initialEmail by profileViewModel.email.collectAsState()
    val initialImageUri by profileViewModel.profileImageUri.collectAsState()

    var name by remember(initialName) { mutableStateOf(initialName) }
    var email by remember(initialEmail) { mutableStateOf(initialEmail) }
    var imageUri by remember(initialImageUri) { mutableStateOf(initialImageUri) }

    val context = LocalContext.current

    // 1. Launcher untuk membuka galeri (GetContent)
    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    // 2. State dan Launcher untuk Izin (Permission)
    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val permissionState = rememberPermissionState(permission = permissionToRequest)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable { 
                    // 3. Logika Pengecekan Izin saat gambar di-klik
                    if (permissionState.status.isGranted) {
                        imagePickerLauncher.launch("image/*")
                    } else {
                        permissionState.launchPermissionRequest()
                    }
                 },
            contentAlignment = Alignment.Center
        ) {
            val imagePainter = rememberAsyncImagePainter(
                model = imageUri,
                placeholder = painterResource(id = R.drawable.profile_picture_placeholder),
                error = painterResource(id = R.drawable.profile_picture_placeholder)
            )
            Image(
                painter = imagePainter,
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Green,
                focusedBorderColor = Green,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.Black) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = Green,
                focusedBorderColor = Green,
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            profileViewModel.saveProfile(name, email, imageUri)
            context.showToastShort("Profil berhasil diperbarui")
            navController.popBackStack()
        }) {
            Text(
                text = "Simpan Perubahan",
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = TEXT_SIZE_18sp,
                color = Color.Black
            )
        }
    }
}
