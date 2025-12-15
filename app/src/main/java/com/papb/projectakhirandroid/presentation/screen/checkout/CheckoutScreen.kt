package com.papb.projectakhirandroid.presentation.screen.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import com.papb.projectakhirandroid.navigation.screen.Screen
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.papb.projectakhirandroid.presentation.common.content.ContentCart
import com.papb.projectakhirandroid.ui.theme.*

@Composable
fun CheckoutScreen(
    navController: NavController,
    checkoutViewModel: CheckoutViewModel = hiltViewModel()
) {
    val productCartList by checkoutViewModel.productCartList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Checkout") })
        },
        bottomBar = {
            Button(
                onClick = { navController.navigate(Screen.Invoice.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DIMENS_16dp)
            ) {
                Text(text = "Bayar")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(DIMENS_16dp)
        ) {
            Text(
                text = "Ringkasan Pesanan",
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = TEXT_SIZE_18sp,
                color = Black
            )

            Spacer(modifier = Modifier.height(DIMENS_16dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(productCartList) { item ->
                    // Menggunakan komponen dari keranjang untuk tampilan yang konsisten
                    ContentCart(productItem = item)
                    Divider()
                }
            }
        }
    }
}