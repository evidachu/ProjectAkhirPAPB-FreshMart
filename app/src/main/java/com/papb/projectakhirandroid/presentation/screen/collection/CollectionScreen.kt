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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.papb.projectakhirandroid.R
import com.papb.projectakhirandroid.domain.model.CollectionItem
import com.papb.projectakhirandroid.ui.theme.*

@Composable
fun CollectionScreen(
    navController: NavController
) {
    val collectionGraphEntry = remember(navController.currentBackStackEntry) {
        try {
            navController.getBackStackEntry("collection_graph")
        } catch (e: Exception) {
            null
        }
    }

    val viewModel: CollectionViewModel = if (collectionGraphEntry != null) {
        hiltViewModel(collectionGraphEntry)
    } else {
        hiltViewModel()
    }

    val collections by viewModel.collections.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (collections.isEmpty() && !isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
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
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(collections) { collection ->
                        CollectionCard(collection = collection, onDelete = { viewModel.deleteCollection(it) })
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
                if (collection.imageUrl.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_picture_placeholder), // Fallback
                        contentDescription = collection.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(collection.imageUrl)
                            .crossfade(true)
                            .placeholder(R.drawable.profile_picture_placeholder)
                            .error(R.drawable.profile_picture_placeholder)
                            .build(),
                        contentDescription = collection.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    )
                }
                
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
