package com.papb.projectakhirandroid.presentation.screen.invoice

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.papb.projectakhirandroid.navigation.screen.BottomNavItemScreen
import com.papb.projectakhirandroid.presentation.common.content.ContentCart
import com.papb.projectakhirandroid.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun InvoiceScreen(
    navController: NavController,
    invoiceViewModel: InvoiceViewModel = hiltViewModel()
) {
    val productCartList by invoiceViewModel.productCartList.collectAsState()
    val totalPrice by invoiceViewModel.totalPrice.collectAsState()
    val currentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
    val invoiceNumber = "INV-${System.currentTimeMillis() / 1000}"

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Invoice") })
        },
        bottomBar = {
            Button(
                onClick = {
                    navController.navigate(BottomNavItemScreen.Home.route) {
                        popUpTo(BottomNavItemScreen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DIMENS_16dp)
            ) {
                Text(text = "Kembali ke Beranda")
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
                modifier = Modifier.fillMaxWidth(),
                text = "Pembayaran Berhasil!",
                fontFamily = GilroyFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = TEXT_SIZE_24sp,
                color = Green,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(DIMENS_16dp))

            // --- Invoice Card ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = DIMENS_4dp,
                shape = RoundedCornerShape(DIMENS_8dp)
            ) {
                Column(
                    modifier = Modifier.padding(DIMENS_16dp)
                ) {
                    // Invoice Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "INVOICE",
                            fontFamily = GilroyFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = TEXT_SIZE_24sp
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = invoiceNumber, fontSize = TEXT_SIZE_12sp)
                            Text(text = "Date: $currentDate", fontSize = TEXT_SIZE_12sp)
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = DIMENS_16dp))

                    // Rincian Pesanan
                    Text(
                        text = "Rincian Pesanan:",
                        fontFamily = GilroyFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = TEXT_SIZE_18sp,
                        color = Black
                    )

                    Spacer(modifier = Modifier.height(DIMENS_8dp))

                    // Daftar Item
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(DIMENS_1dp, GrayBorderStroke, RoundedCornerShape(DIMENS_4dp))
                            .padding(DIMENS_8dp)
                    ) {
                        productCartList.forEach { item ->
                            ContentCart(productItem = item)
                            if (productCartList.last() != item) {
                                Divider()
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(DIMENS_24dp))

                    // Total Pembayaran
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Pembayaran",
                            fontFamily = GilroyFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = TEXT_SIZE_18sp,
                            color = Black
                        )
                        Text(
                            text = "Rp ${totalPrice.toInt()}",
                            fontFamily = GilroyFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = TEXT_SIZE_24sp,
                            color = Green
                        )
                    }
                }
            }
        }
    }
}