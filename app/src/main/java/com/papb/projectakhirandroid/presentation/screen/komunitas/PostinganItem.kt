package com.papb.projectakhirandroid.presentation.screen.komunitas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.papb.projectakhirandroid.ui.theme.DIMENS_250dp
import coil.compose.rememberAsyncImagePainter
import com.papb.projectakhirandroid.R // Import resource
import com.papb.projectakhirandroid.domain.model.Post
import com.papb.projectakhirandroid.ui.theme.* // Import theme/dimens

@Composable
fun PostinganItem(
    post: Post, // Menerima objek Post utuh
    currentUserName: String, // Username pengguna yang sedang login (untuk pengecekan isOwner)
    navController: NavController
) {
    // Memeriksa apakah postingan ini milik pengguna yang sedang login
    val isOwner = post.owner == currentUserName

    // State untuk Dropdown Menu (Menu Opsi Lainnya)
    var showMenu by remember { mutableStateOf(false) }

    // Logic Profile Picture: Prioritaskan post.ownerAvatarUrl, jika null fallback ke drawable
    val painter = if (post.ownerAvatarUrl != null) {
        rememberAsyncImagePainter(
            model = post.ownerAvatarUrl,
            placeholder = painterResource(id = R.drawable.profileimage),
            error = painterResource(id = R.drawable.profileimage)
        )
    } else {
        painterResource(id = R.drawable.profileimage)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // TODO: Navigasi ke Detail Postingan
                println("Menuju detail postingan: ${post.id}")
            },
        shape = RoundedCornerShape(DIMENS_12dp),
        elevation = DIMENS_4dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // =========================================================
            //               HEADER POST (Profil & Opsi)
            // =========================================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = DIMENS_16dp, vertical = DIMENS_12dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gambar Profil (Update: Menggunakan Coil jika ada URL)
                Image(
                    painter = painter,
                    contentDescription = "Foto Profil ${post.owner}",
                    modifier = Modifier
                        .size(DIMENS_40dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(DIMENS_8dp))

                // Nama Pengguna & Tipe Post
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.owner ?: "Anonim", // Tampilkan nama pemilik
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = TEXT_SIZE_16sp,
                        color = Black
                    )
                    Text(
                        text = post.type.uppercase(), // Tampilkan tipe post (RESEP/TIPS)
                        fontSize = TEXT_SIZE_12sp,
                        color = Green,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Tombol Opsi (Hanya tampil jika pemilik)
                if (isOwner) {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "Opsi Postingan")
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(onClick = {
                                // TODO: Logika Edit Post
                                showMenu = false
                                println("Edit Post: ${post.id}")
                            }) {
                                Text("Edit")
                            }
                            Divider()
                            DropdownMenuItem(onClick = {
                                // TODO: Logika Hapus Post
                                showMenu = false
                                println("Hapus Post: ${post.id}")
                            }) {
                                Text("Hapus", color = Color.Red)
                            }
                        }
                    }
                }
            } // End Row Header


            // =========================================================
            //                      GAMBAR POST
            // =========================================================
            // Tampilkan gambar hanya jika imageUrl tidak null
            if (post.imageUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = post.imageUrl),
                    contentDescription = post.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DIMENS_250dp)
                )
            } else {
                // Tampilkan placeholder jika tidak ada gambar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DIMENS_200dp)
                        .background(Color.LightGray.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tidak Ada Gambar", color = Color.Gray)
                }
            }


            // =========================================================
            //                    KETERANGAN POST
            // =========================================================
            Column(modifier = Modifier.padding(DIMENS_16dp)) {
                // Judul
                Text(
                    text = post.title,
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = TEXT_SIZE_18sp,
                    color = Black
                )
                Spacer(modifier = Modifier.height(DIMENS_4dp))

                // Deskripsi Singkat
                Text(
                    text = post.description.take(100) + if (post.description.length > 100) "..." else "",
                    fontSize = TEXT_SIZE_14sp,
                    color = Color.DarkGray
                )
                // Tombol Baca Selengkapnya
                Text(
                    text = "Baca Selengkapnya",
                    fontSize = TEXT_SIZE_14sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Green,
                    modifier = Modifier
                        .padding(top = DIMENS_4dp)
                        .clickable {
                            // TODO: Navigasi ke Detail Postingan
                            println("Membaca selengkapnya...")
                        }
                )
            }
        }
    }
}
