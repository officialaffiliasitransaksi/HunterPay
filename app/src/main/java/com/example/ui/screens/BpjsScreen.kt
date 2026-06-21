package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
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
fun BpjsScreen(
    viewModel: HunterPayViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val memberId by viewModel.bpjsMemberId.collectAsState()
    val monthsPeriod by viewModel.bpjsPeriods.collectAsState()

    var showConfirmDialog by remember { mutableStateOf(false) }

    val billPrice = viewModel.bpjsRatePerMonth * monthsPeriod

    Scaffold(
        topBar = {
            AppHeader(
                title = "BPJS Kesehatan HunterPay",
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
            // BPJS Card Inputs
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Nomor Kartu BPJS Kesehatan 🩺",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = memberId,
                        onValueChange = { viewModel.updateBpjsId(it.filter { char -> char.isDigit() }) },
                        label = { Text("Contoh: 0001234567890") },
                        placeholder = { Text("Masukkan 13 digit nomor kartu") },
                        leadingIcon = { Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = CoolTeal40) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Premium Billing Period Selector
            Text(
                text = "Pilih Periode Pembayaran (Bulan):",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(1, 2, 3, 6).forEach { m ->
                    val isSelected = monthsPeriod == m
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.selectBpjsPeriods(m) }
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) DeepBlue40 else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) DeepBlue40.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$m Bulan",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isSelected) DeepBlue40 else Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = CoolTeal40.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Informasi BPJS",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = CoolTeal40
                    )
                    Text(
                        text = "• Layanan: Kelas 1 Premi Kesehatan Mandiri\n• Tarif: Rp 150.000 / Bulan Per Anggota\n• Periode: $monthsPeriod Bulan",
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (memberId.length >= 8) {
                        showConfirmDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = memberId.length >= 8,
                colors = ButtonDefaults.buttonColors(containerColor = DeepBlue40)
            ) {
                Text(
                    text = "Bayar BPJS - " + viewModel.formatRupiah(billPrice),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Checkout Dialog
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
                            text = "Konfirmasi Bayar BPJS 🩺",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(text = "Rincian Premi Kesehatan:", fontSize = 12.sp, color = Color.Gray)
                        Text(text = "No Kartu BPJS: $memberId", fontWeight = FontWeight.Bold, color = DeepBlue40)
                        Text(text = "Nama Anggota: SIMULASI RENDY-BPJS", fontSize = 13.sp)
                        Text(text = "Durasi Tagihan: $monthsPeriod Bulan", fontSize = 13.sp)
                        Text(
                            text = "Total Pembayaran: ${viewModel.formatRupiah(billPrice)}",
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
                                    viewModel.payBpjsBill { success ->
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
