package com.papb.projectakhirandroid.presentation.screen.komunitas

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.papb.projectakhirandroid.ui.theme.* // Import tema Anda
import androidx.compose.ui.unit.dp // Pastikan ini diimpor

// Enum untuk merepresentasikan tab yang aktif
enum class KomunitasTab {
    RESEP_MU,
    TIPS_DAPUR
}

@Composable
fun KomunitasScreen(
    navController: NavController = rememberNavController() // Tambahkan navController jika Anda perlu navigasi dari sini
) {
    // State untuk melacak tab yang sedang aktif
    var selectedTab by remember { mutableStateOf(KomunitasTab.RESEP_MU) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Aksi saat tombol plus diklik, misal navigasi ke AddPostScreen */ },
                backgroundColor = Green, // Gunakan warna Green dari tema Anda
                contentColor = Color.White,
                modifier = Modifier.padding(bottom = DIMENS_16dp, end = DIMENS_16dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Postingan"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End // Posisikan di kanan bawah
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Penting untuk menyesuaikan padding dengan Scaffold
        ) {
            // Header untuk tab "Resep Mu" dan "Tips Dapur"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DIMENS_16dp)
                    .clip(RoundedCornerShape(DIMENS_12dp)) // Bentuk RoundedCorner
                    .background(GrayBackground), // Background keseluruhan tab
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tab "Resep Mu"
                TabItem(
                    text = "Resep Mu",
                    isSelected = selectedTab == KomunitasTab.RESEP_MU,
                    onClick = { selectedTab = KomunitasTab.RESEP_MU },
                    modifier = Modifier.weight(1f) // Ambil setengah lebar
                )
                // Tab "Tips Dapur"
                TabItem(
                    text = "Tips Dapur",
                    isSelected = selectedTab == KomunitasTab.TIPS_DAPUR,
                    onClick = { selectedTab = KomunitasTab.TIPS_DAPUR },
                    modifier = Modifier.weight(1f) // Ambil setengah lebar
                )
            }

            // Konten yang akan berubah sesuai tab yang dipilih
            Crossfade(targetState = selectedTab, label = "KomunitasTabContent") { tab ->
                when (tab) {
                    KomunitasTab.RESEP_MU -> ResepMuContent()
                    KomunitasTab.TIPS_DAPUR -> TipsDapurContent()
                }
            }
        }
    }
}

@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(DIMENS_12dp))
            .background(if (isSelected) Green else Color.Transparent) // Warna background tab
            .clickable(onClick = onClick)
            .padding(vertical = DIMENS_8dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = GilroyFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) Color.White else Black,
            fontSize = TEXT_SIZE_16sp
        )
    }
}

@Composable
fun ResepMuContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Daftar Resep Pengguna Anda", fontFamily = GilroyFontFamily)
    }
}

@Composable
fun TipsDapurContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Kumpulan Tips dan Trik Dapur", fontFamily = GilroyFontFamily)
    }
}

@Preview(showBackground = true)
@Composable
fun KomunitasScreenPreview() {
    KomunitasScreen()
}