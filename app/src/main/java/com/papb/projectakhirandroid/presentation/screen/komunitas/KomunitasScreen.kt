package com.papb.projectakhirandroid.presentation.screen.komunitas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.domain.model.Post
import com.papb.projectakhirandroid.ui.theme.* // Asumsi ini berisi DIMENS, TEXT_SIZE, dan Colors
import com.papb.projectakhirandroid.utils.Utils

// Definisi Tab
private val tabTitles = listOf("Resep", "Tips Dapur")

@Composable
fun KomunitasScreen(
    navController: NavController,
    viewModel: KomunitasViewModel = hiltViewModel()
) {
    // Mengambil daftar postingan dari ViewModel
    val posts by viewModel.posts.collectAsState()
    val context = LocalContext.current

    // State untuk mengelola Tab yang sedang dipilih
    var selectedTabIndex by remember { mutableStateOf(0) } // 0 = Resep, 1 = Tips Dapur

    // Filter posts berdasarkan tab yang dipilih
    val currentPostType = if (selectedTabIndex == 0) "resep" else "tips"
    val filteredPosts = posts.filter { it.type == currentPostType }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Komunitas FreshMart",
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Black
                    )
                },
                backgroundColor = Color.White,
                elevation = DIMENS_4dp
            )
        },
        // Floating Action Button untuk Tambah Postingan
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navigasi ke AddPostScreen dengan tipe yang sesuai (postId=0L untuk ADD)
                    val postTypeNav = if (selectedTabIndex == 0) "resep" else "tips"
                    navController.navigate("add_post/$postTypeNav?postId=0")
                },
                backgroundColor = Green,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Postingan")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Bagian Tab
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                backgroundColor = Color.White,
                edgePadding = DIMENS_16dp,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Green,
                        height = DIMENS_2dp
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                title,
                                fontFamily = GilroyFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = if (selectedTabIndex == index) Green else GraySecondTextColor
                            )
                        }
                    )
                }
            }

            // Konten Tab (Daftar Postingan)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = DIMENS_16dp)
                    .padding(top = DIMENS_16dp),
                verticalArrangement = Arrangement.spacedBy(DIMENS_16dp)
            ) {

                items(filteredPosts) { post ->
                    PostItem(
                        post = post,
                        onEdit = { postToEdit ->
                            // Mengirim ID bertipe Long
                            navController.navigate("add_post_screen/${postToEdit.type}?postId=${postToEdit.id}")
                        },
                        onDelete = { postToDelete ->
                            viewModel.deletePost(postToDelete)
                            Utils.displayToast(context, "Postingan '${postToDelete.title}' dihapus!")
                        }
                    )
                }

                // ✅ PERBAIKAN: Membungkus Spacer dalam 'item {}' untuk LazyColumn
                item {
                    Spacer(modifier = Modifier.height(DIMENS_64dp))
                }
            }
        }
    }
}

@Composable
fun PostItem(post: Post, onEdit: (Post) -> Unit, onDelete: (Post) -> Unit) {
    Card(
        shape = RoundedCornerShape(DIMENS_12dp),
        elevation = DIMENS_4dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(DIMENS_16dp)) {
            // Header Postingan (Owner, Tipe, Aksi Edit/Delete)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_picture_placeholder),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(DIMENS_40dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(DIMENS_8dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = post.owner, fontWeight = FontWeight.Bold, fontSize = TEXT_SIZE_14sp, color = Color.Black)
                    // Mengubah 'Tips Diet' menjadi 'Tips Dapur' agar konsisten dengan Tab
                    val typeText = if (post.type == "resep") "Resep" else "Tips Dapur"
                    Text(text = typeText, fontWeight = FontWeight.Medium, fontSize = TEXT_SIZE_12sp, color = GraySecondTextColor)
                }

                // Aksi Edit dan Delete
                IconButton(onClick = { onEdit(post) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Post", tint = Green)
                }
                IconButton(onClick = { onDelete(post) }) {
                    Icon(painterResource(id = R.drawable.ic_delete), contentDescription = "Delete Post", tint = Color.Red)
                }
            }

            // Gambar Postingan (Jika ada)
            post.imageUrl?.let { uri ->
                Spacer(modifier = Modifier.height(DIMENS_12dp))
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Gambar Postingan",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DIMENS_200dp)
                        .clip(RoundedCornerShape(DIMENS_8dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(DIMENS_12dp))

            // Judul dan Deskripsi
            Text(text = post.title, fontWeight = FontWeight.Bold, fontSize = TEXT_SIZE_16sp, color = Color.Black)
            Spacer(modifier = Modifier.height(DIMENS_4dp))
            Text(text = post.description, fontWeight = FontWeight.Normal, fontSize = TEXT_SIZE_14sp, color = GraySecondTextColor)
        }
    }
}