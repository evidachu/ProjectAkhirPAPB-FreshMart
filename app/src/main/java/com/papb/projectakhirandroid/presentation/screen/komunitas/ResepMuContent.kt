package com.papb.projectakhirandroid.presentation.screen.komunitas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.papb.projectakhirandroid.domain.model.Post
import com.papb.projectakhirandroid.ui.theme.* @Composable
fun ResepMuContent(
    currentUserName: String,
    navController: NavController,
    posts: List<Post>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // ✅ KEMBALIKAN: contentPadding ke nilai dasar. Padding Bottom akan ditangani oleh MainScreen.kt
        contentPadding = PaddingValues(horizontal = DIMENS_16dp, vertical = DIMENS_8dp),
        verticalArrangement = Arrangement.spacedBy(DIMENS_16dp)
    ) {
        // Judul dan Deskripsi Khusus Resep Mu
        item {
            // ... (Kode Judul/Deskripsi)
        }

        // Iterasi menggunakan data Post yang diterima
        items(posts, key = { it.id }) { post ->
            PostinganItem(
                post = post,
                currentUserName = currentUserName,
                navController = navController
            )
        }

        // ❌ HAPUS SPACER BERLEBIHAN DI SINI!
        // Biarkan MainScreen yang memberi padding.
    }
}