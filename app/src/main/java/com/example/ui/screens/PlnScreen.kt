package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.AppHeader
import com.example.ui.theme.CoolTeal40
import com.example.ui.theme.DeepBlue40
import com.example.ui.theme.EnergeticOrange40
import com.example.ui.viewmodel.HunterPayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlnScreen(
    viewModel: HunterPayViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val custId by viewModel.plnCustId.collectAsState()
    val selectedAmount by viewModel.plnSelectedAmount.collectAsState()

    var showConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Listrik PLN HunterPay",
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
            // ID Input
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ID Pelanggan / Nomor Meter ⚡",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = custId,
                        onValueChange = { viewModel.updatePlnCustId(it.filter { char -> char.isDigit() }) },
                        label = { Text("Contoh: 140987654321") },
                        placeholder = { Text("Masukkan 11-12 digit ID") },
                        leadingIcon = { Icon(Icons.Default.OfflineBolt, contentDescription = null, tint = EnergeticOrange40) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pilih Nominal Token Listrik:",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Grid of amounts
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(viewModel.plnOptions) { amount ->
                    val isSelected = selectedAmount == amount
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectPlnAmount(amount)
                            }
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) DeepBlue40 else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) DeepBlue40.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Rp " + String.format("%,.0f", amount),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (isSelected) DeepBlue40 else Color.Black
                            )
                            Text(
                                text = "Pilih",
                                fontSize = 11.sp,
                                color = if (isSelected) DeepBlue40 else Color.Gray,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Pay button
            Button(
                onClick = {
                    if (custId.length >= 8) {
                        showConfirmDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = custId.length >= 8,
                colors = ButtonDefaults.buttonColors(containerColor = DeepBlue40)
            ) {
                Text(
                    text = "Beli Token PLN (Rp " + String.format("%,.0f", selectedAmount) + ")",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Token buy dialog
        if (showConfirmDialog) {
            Dialog(onDismissRequest = { showConfirmDialog = false }) {
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
                            text = "Konfirmasi Beli Token PLN ⚡",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(text = "Rincian Transaksi:", fontSize = 12.sp, color = Color.Gray)
                        Text(text = "Produk: Token Listrik PLN", fontWeight = FontWeight.Bold)
                        Text(text = "ID Pelanggan: $custId", fontWeight = FontWeight.Bold, color = DeepBlue40)
                        Text(text = "Nama Pelanggan: SIMULASI DEVI-PLN", fontSize = 13.sp)
                        Text(
                            text = "Total Nominal: ${viewModel.formatRupiah(selectedAmount)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = EnergeticOrange40,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showConfirmDialog = false }) {
                                Text("Batal")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    viewModel.payPlnBill { success ->
                                        if (success) {
                                            showConfirmDialog = false
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
