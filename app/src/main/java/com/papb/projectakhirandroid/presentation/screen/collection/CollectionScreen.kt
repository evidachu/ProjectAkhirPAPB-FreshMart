package com.papb.projectakhirandroid.presentation.screen.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import com.papb.projectakhirandroid.navigation.screen.Screen
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.ui.theme.*

@Composable
fun CollectionScreen(
    navController: NavController
) {
    val collectionGraphEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry("collection_graph")
    }
    val viewModel: CollectionViewModel = hiltViewModel(collectionGraphEntry)

    val collections by viewModel.collections.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Koleksi Saya") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali", tint = Black)
                    }
                },
                backgroundColor = Color.White,
                elevation = DIMENS_4dp,
                contentColor = Black
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddCollection.route) },
                backgroundColor = Green
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Koleksi", tint = Color.Black)
            }
        }
    ) { padding ->
        if (collections.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Koleksi Anda masih kosong.\nSilakan tambahkan koleksi baru.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(collections) { collection ->
                    CollectionCard(collection = collection, onDelete = { viewModel.deleteCollection(it) })
                }
            }
        }
    }
}

@Composable
fun CollectionCard(collection: CollectionItem, onDelete: (CollectionItem) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) { 
        Box {
            Column {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = collection.imageUri,
                        error = painterResource(id = R.drawable.profile_picture_placeholder) // Fallback image
                    ),
                    contentDescription = collection.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                )
                Text(
                    text = collection.name,
                    modifier = Modifier.padding(8.dp),
                    fontFamily = GilroyFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }
            IconButton(
                onClick = { expanded = true },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "More options", tint = Color.White)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(onClick = { 
                        onDelete(collection)
                        expanded = false
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Hapus")
                        }
                    }
                }
            }
        }
    }
}
