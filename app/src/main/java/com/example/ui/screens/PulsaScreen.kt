package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.AppHeader
import com.example.ui.navigation.Screen
import com.example.ui.theme.CoolTeal40
import com.example.ui.theme.DeepBlue40
import com.example.ui.theme.EnergeticOrange40
import com.example.ui.viewmodel.HunterPayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PulsaScreen(
    viewModel: HunterPayViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val phoneNum by viewModel.pulsaPhone.collectAsState()
    val providerName by viewModel.pulsaProvider.collectAsState()

    var showConfirmDialog by remember { mutableStateOf<Pair<String, Double>?>(null) }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Pulsa & Paket Data",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            // Input phone number
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Masukkan Nomor Telepon 📱",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phoneNum,
                        onValueChange = { viewModel.updatePulsaPhone(it.filter { char -> char.isDigit() }) },
                        label = { Text("Contoh: 08123456789") },
                        placeholder = { Text("08...") },
                        leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = DeepBlue40) },
                        trailingIcon = {
                            if (providerName.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .background(CoolTeal40, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = providerName,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pilih Denominasi Pulsa / Paket:",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Denom packages grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.pulsaPackages) { pkg ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (phoneNum.length >= 9) {
                                    showConfirmDialog = pkg
                                } else {
                                    // Handled via state checking in viewModel
                                }
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = pkg.first,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                color = DeepBlue40
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Harga: " + viewModel.formatRupiah(pkg.second),
                                color = EnergeticOrange40,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Bill Checkout Confirmation Dialog
        showConfirmDialog?.let { pkg ->
            Dialog(onDismissRequest = { showConfirmDialog = null }) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "Konfirmasi Pembelian Pulsa 🛍️",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Paket: ${pkg.first}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Nomor Tujuan: $phoneNum", fontSize = 14.sp)
                        Text(text = "Operator: $providerName", fontSize = 13.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Total Pembayaran: ${viewModel.formatRupiah(pkg.second)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = EnergeticOrange40
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showConfirmDialog = null }) {
                                Text("Batal")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    viewModel.payMobileBill(pkg.second, pkg.first) { success ->
                                        if (success) {
                                            showConfirmDialog = null
                                            onNavigateToHistory()
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DeepBlue40)
                            ) {
                                Text("Bayar Sekarang")
                            }
                        }
                    }
                }
            }
        }
    }
}
