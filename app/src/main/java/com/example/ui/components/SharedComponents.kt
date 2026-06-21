package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.TravelBooking
import com.example.ui.theme.CoolTeal40
import com.example.ui.theme.DeepBlue40
import com.example.ui.theme.EnergeticOrange40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null
) {
    val primaryColor = DeepBlue40 // M3 Purple #6750A4
    TopAppBar(
        title = {
            if (onBackClick != null) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = primaryColor
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(primaryColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "HP",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "HunterPay",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = primaryColor,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = "TRAVEL & PAYMENTS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = Color(0xFF49454F),
                            letterSpacing = 0.8.sp
                        )
                    }
                }
            }
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = primaryColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = primaryColor,
            navigationIconContentColor = primaryColor,
            actionIconContentColor = primaryColor
        ),
        actions = {
            if (actions != null) {
                actions()
            }
        }
    )
}

@Composable
fun WalletCard(
    balanceStr: String,
    pointsStr: String,
    onTopUpClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onAskAiClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DeepBlue40, Color(0xFF0369A1))
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "HunterPay Wallet",
                        color = Color(0xFFE0F2FE),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = balanceStr,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = "Points",
                        tint = Color(0xFFFACC15),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$pointsStr Koin",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Color.White.copy(alpha = 0.12f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WalletButton(
                    icon = Icons.Default.AddCard,
                    label = "Isi Saldo",
                    onClick = onTopUpClick
                )
                WalletButton(
                    icon = Icons.Default.QrCodeScanner,
                    label = "Bayar",
                    onClick = onHistoryClick
                )
                WalletButton(
                    icon = Icons.Default.SmartToy,
                    label = "Tanya PAI",
                    onClick = onAskAiClick
                )
            }
        }
    }
}

@Composable
fun WalletButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PromoSlider(
    onPromoClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Eksklusif Untukmu 🎁",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1C1B1F)
            )
            TextButton(onClick = { onPromoClick("Semua Promo HunterPay") }) {
                Text(
                    text = "Lihat semua",
                    color = DeepBlue40,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Large Premium Promo item inspired directly by the "Professional Polish" template
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(132.dp)
                .clickable { onPromoClick("Summer Getaway: Save up to 45% on stays in Bali & Lombok!") },
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color(0xFFBAE6FD)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Summer Getaway! 🏖️",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF0369A1)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Main ke Bali & Lombok diskon s.d. 45% pakai HunterPay Wallet.",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            maxLines = 2
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { onPromoClick("Liburan ke Bali Cashback hingga 45%! Hubungi Customer Care.") },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepBlue40),
                        modifier = Modifier.height(28.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text("Pesan Sekarang", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFBAE6FD)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BeachAccess,
                        contentDescription = "Pantai",
                        tint = DeepBlue40,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyPlaceholder(
    icon: ImageVector,
    message: String,
    subMessage: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    RoundedCornerShape(36.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subMessage,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun QuickTopUpDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double, String) -> Unit
) {
    val VaOptions = listOf("BCA Virtual Account", "Mandiri Virtual Account", "BRI Virtual Account")
    var selectedVa by remember { mutableStateOf(VaOptions[0]) }
    var inputAmount by remember { mutableStateOf("100000") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(24.dp)
            ) {
                Text(
                    text = "Isi Saldo HunterPay 💳",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = inputAmount,
                    onValueChange = { inputAmount = it.filter { char -> char.isDigit() } },
                    label = { Text("Jumlah Isi Saldo (Rp)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Pilih Metode Pembayaran VA:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(6.dp))

                VaOptions.forEach { va ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedVa = va }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedVa == va),
                            onClick = { selectedVa = va }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = va, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amt = inputAmount.toDoubleOrNull() ?: 0.0
                            if (amt >= 10000.0) {
                                onConfirm(amt, selectedVa)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepBlue40)
                    ) {
                        Text("Konfirmasi")
                    }
                }
            }
        }
    }
}

@Composable
fun BookingReceiptDialog(
    booking: TravelBooking,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color(0xFFFAFAFA))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Boarding pass header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (booking.type == "FLIGHT") "M-BOARDING PASS" else if (booking.type == "HOTEL") "HOTEL VOUCHER" else "TRAIN BOARDING PASS",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = DeepBlue40
                        )
                        Text(
                            text = "Aplikasi HunterPay",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(CoolTeal40.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = booking.status,
                            color = DeepBlue40,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = booking.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Rincian Tiket:",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = booking.detailInfo,
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Nama Tamu/Penumpang", fontSize = 11.sp, color = Color.Gray)
                        Text(booking.paxName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Kode Booking", fontSize = 11.sp, color = Color.Gray)
                        Text(booking.bookingRef, fontWeight = FontWeight.Black, fontSize = 16.sp, color = EnergeticOrange40)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // Dummy Barcode generator
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .height(50.dp)
                            .background(Color.White)
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Drawing simulated lines of a barcode!
                        val barColor = Color.Black
                        for (i in 0..16) {
                            val barWidth = if (i % 3 == 0) 3.dp else if (i % 2 == 0) 6.dp else 1.dp
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(barWidth)
                                    .background(barColor)
                            )
                            Spacer(modifier = Modifier.width(if (i % 4 == 0) 4.dp else 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${booking.bookingRef}-${System.currentTimeMillis() % 100000}",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepBlue40)
                ) {
                    Text("Tutup", color = Color.White)
                }
            }
        }
    }
}
