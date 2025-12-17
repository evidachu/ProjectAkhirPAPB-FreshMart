package com.papb.projectakhirandroid.presentation.screen.collection

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.papb.projectakhirandroid.ui.theme.Green

@Composable
fun AddCollectionScreen(
    navController: NavController
) {
    // Explicitly get the ViewModel scoped to the collection graph
    val collectionGraphEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry("collection_graph")
    }
    val viewModel: CollectionViewModel = hiltViewModel(collectionGraphEntry)

    var name by remember { mutableStateOf("") }

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

            val isButtonEnabled = name.isNotBlank()

            Button(
                onClick = {
                    viewModel.addCollection(name)
                    navController.popBackStack()
                },
                enabled = isButtonEnabled
            ) {
                Text("Simpan")
            }
        }
    }
}
