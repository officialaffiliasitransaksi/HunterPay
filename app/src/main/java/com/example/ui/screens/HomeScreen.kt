package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppHeader
import com.example.ui.components.PromoSlider
import com.example.ui.components.QuickTopUpDialog
import com.example.ui.components.WalletCard
import com.example.ui.navigation.Screen
import com.example.ui.theme.CoolTeal40
import com.example.ui.theme.DeepBlue40
import com.example.ui.theme.EnergeticOrange40
import com.example.ui.theme.WhiteGradientStart
import com.example.ui.theme.WhiteGradientEnd
import androidx.compose.ui.graphics.Brush
import com.example.ui.viewmodel.HunterPayViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HunterPayViewModel,
    onNavigate: (String) -> Unit
) {
    val profileState by viewModel.userProfile.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    var showTopUpDialog by remember { mutableStateOf(false) }

    val balanceStr = profileState?.let { viewModel.formatRupiah(it.balance) } ?: "Rp 0"
    val pointsStr = profileState?.points?.toString() ?: "0"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0284C7), // Rich Sky Blue
                        Color(0xFF38BDF8), // Vibrant Light Sky Blue
                        Color(0xFFF1F5F9), // Light Slate
                        Color(0xFFF1F5F9)
                    ),
                    startY = 0f,
                    endY = 1400f
                )
            )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 1. Search Bar & Status Row
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Search bar capsule
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .background(Color.White, RoundedCornerShape(22.dp))
                                .clickable { viewModel.postUiNotification("Ingin bepergian? Cari tiket langsung di menu bawah! 🐦") }
                                .padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DON'T WORRY, NO RUGI 🐦",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0284C7)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Notification Icon
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                .clip(CircleShape)
                                .clickable { viewModel.postUiNotification("Semua notifikasi Anda bersih! 🔔") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                            // Red badge
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-2).dp, y = (2).dp)
                                    .size(8.dp)
                                    .background(Color.Red, CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Chat/AI Icon
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                .clip(CircleShape)
                                .clickable { onNavigate(Screen.Chat.route) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SmartToy,
                                contentDescription = "Chat with HunterPAI",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                // 2. Custom Slogan & Air Vector Header
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Satu Aplikasi Untuk\nSemua Kebutuhan Liburan",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 25.sp,
                            lineHeight = 32.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Custom 3D Airplane and Vector Clouds overlay
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Left cloud blur
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .offset(x = 36.dp, y = 10.dp)
                                    .size(70.dp, 24.dp)
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            )
                            // Right cloud blur
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .offset(x = (-36).dp, y = (-15).dp)
                                    .size(90.dp, 28.dp)
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                            )

                            // Flying Airplane Vector
                            Icon(
                                imageVector = Icons.Default.FlightTakeoff,
                                contentDescription = "Airplane decoration",
                                tint = Color.White,
                                modifier = Modifier.size(76.dp)
                            )
                        }
                    }
                }

                // 3. Premium Services Mockup Card (Exact 5x2 Mockup Layout!)
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp, horizontal = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // ROW 1: Large Colorful Circular Backgrounds
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // 1. Pesawat (Cyan)
                                MockupGridIconLarge(
                                    icon = Icons.Default.FlightTakeoff,
                                    label = "Tiket\nPesawat",
                                    circleColor = Color(0xFF00C4FF),
                                    onClick = { onNavigate(Screen.Flights.route) }
                                )
                                // 2. Hotel (Navy/Royal)
                                MockupGridIconLarge(
                                    icon = Icons.Default.Hotel,
                                    label = "Hotel",
                                    circleColor = Color(0xFF1E3A8A),
                                    onClick = { onNavigate(Screen.Hotels.route) }
                                )
                                // 3. Xperience (Rose Pink)
                                MockupGridIconLarge(
                                    icon = Icons.Default.LocalActivity,
                                    label = "Xperience",
                                    circleColor = Color(0xFFFF4E6A),
                                    onClick = { viewModel.postUiNotification("Layanan Tiket Event & Xperience segera hadir di HunterPay! 🎡") }
                                )
                                // 4. Kereta Api (Orange)
                                MockupGridIconLarge(
                                    icon = Icons.Default.Train,
                                    label = "Tiket Kereta\nApi",
                                    circleColor = Color(0xFFFF9F00),
                                    onClick = { onNavigate(Screen.Trains.route) }
                                )
                                // 5. Bus & Travel (Green)
                                MockupGridIconLarge(
                                    icon = Icons.Default.DirectionsBus,
                                    label = "Tiket Bus\n& Travel",
                                    circleColor = Color(0xFF00C853),
                                    onClick = { viewModel.postUiNotification("Layanan Tiket Bus akan segera hadir di HunterPay! 🚌") }
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // ROW 2: Minimalist Colored Icons without circles
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // 1. Cruises (Red)
                                MockupGridIconMini(
                                    icon = Icons.Default.DirectionsBoat,
                                    label = "Cruises",
                                    iconColor = Color(0xFFEF4444),
                                    onClick = { viewModel.postUiNotification("Layanan Tiket Kapal Pesiar Cruises segera hadir! 🚢") }
                                )
                                // 2. Paket Wisata (Purple)
                                MockupGridIconMini(
                                    icon = Icons.Default.BeachAccess,
                                    label = "Paket\nWisata",
                                    iconColor = Color(0xFFA855F7),
                                    onClick = { viewModel.postUiNotification("Dapatkan paket liburan hemat kustom lewat tanya PAI! 🏖️") }
                                )
                                // 3. Antar-Jemput (Teal)
                                MockupGridIconMini(
                                    icon = Icons.Default.FlightLand,
                                    label = "Antar-\nJemput Ba...",
                                    iconColor = Color(0xFF0D9488),
                                    onClick = { viewModel.postUiNotification("Layanan penjemputan bandara segera terintegrasi! 🚕") }
                                )
                                // 4. Mandiri Mobil (Blue)
                                MockupGridIconMini(
                                    icon = Icons.Default.DirectionsCar,
                                    label = "Mobil",
                                    iconColor = Color(0xFF0284C7),
                                    onClick = { viewModel.postUiNotification("Sewa mobil murah handal segera dirilis! 🚗") }
                                )
                                // 5. TPayLater (Navy)
                                MockupGridIconMini(
                                    icon = Icons.Default.CreditCard,
                                    label = "TPayLater",
                                    iconColor = Color(0xFF1E3A8A),
                                    onClick = { viewModel.postUiNotification("Dapatkan kemudahan bayar liburan nanti dengan TPayLater! 💳") }
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Sliding indicator dots
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color(0xFF0284C7), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color(0xFFCBD5E1), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color(0xFFCBD5E1), CircleShape)
                                )
                            }
                        }
                    }
                }

                // 4. Wallet Overview Block (Preserving 100% Core Business Features!)
                item {
                    WalletCard(
                        balanceStr = balanceStr,
                        pointsStr = pointsStr,
                        onTopUpClick = { showTopUpDialog = true },
                        onHistoryClick = { onNavigate(Screen.History.route) },
                        onAskAiClick = { onNavigate(Screen.Chat.route) }
                    )
                }

                // 5. Special Promotional Neon Lemon-Green Coupon Banner (EXACT Mockup Banner!)
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .clickable {
                                viewModel.postUiNotification("Kupon JALANYUK Berhasil Disalin! 🎉 Gunakan kodenya saat memesan tiket liburan Anda.")
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF82CD47)) // Elegant neon lemony lime-green
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Hadiah Spesial Pengguna Baru 🎁",
                                color = Color.Black,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Diskon s.d. 8%*",
                                color = Color.Black,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Duotone Pill Capsule Code Tag
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF0284C7)), // sky blue ribbon block
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Kode Kupon",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFF1E3A8A)) // deep royal blue right block
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "JALANYUK",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }

                                Text(
                                    text = "*S&K berlaku",
                                    color = Color.Black.copy(alpha = 0.6f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // 6. Special Promotions Slider Carousel
                item {
                    PromoSlider(
                        onPromoClick = { promoMsg ->
                            viewModel.viewModelScope.launch {
                                viewModel.sendChatMessage("Saya tertarik dengan promo: $promoMsg. Beritahu saya cara mengklaimnya!")
                                onNavigate(Screen.Chat.route)
                            }
                        }
                    )
                }

                // 7. Recent Transactions Segment
                item {
                    Text(
                        text = "Aktivitas Terbaru 🧾",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
                    )
                }

                if (transactions.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(24.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Belum ada transaksi terbaru.",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                } else {
                    items(transactions.take(3)) { tx ->
                        val icon = when (tx.type) {
                            "TOPUP" -> Icons.Default.Wallet
                            "FLIGHT" -> Icons.Default.FlightTakeoff
                            "HOTEL" -> Icons.Default.Hotel
                            "TRAIN" -> Icons.Default.Train
                            "PULSA" -> Icons.Default.PhonelinkRing
                            "PLN" -> Icons.Default.OfflineBolt
                            "BPJS" -> Icons.Default.HealthAndSafety
                            else -> Icons.Default.Payment
                        }
                        val iconBg = when (tx.type) {
                            "TOPUP" -> Color(0xFFE8F5E9)
                            "FAILED" -> Color(0xFFFFEBEE)
                            else -> Color(0xFFF0F9FF)
                        }
                        val iconColor = when (tx.type) {
                            "TOPUP" -> Color(0xFF2E7D32)
                            "FAILED" -> Color(0xFFC62828)
                            else -> Color(0xFF0284C7)
                        }
                        val formattedAmt = (if (tx.type == "TOPUP") "+ " else "- ") + viewModel.formatRupiah(tx.amount)
                        val amtColor = if (tx.type == "TOPUP") Color(0xFF2E7D32) else Color.Black

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(iconBg, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = iconColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = tx.description,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "Ref: ${tx.refNo}",
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = formattedAmt,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = amtColor
                                )
                            }
                        }
                    }
                }

                // Extra spacing before footer
                item { Spacer(modifier = Modifier.height(10.dp)) }

                // 8. Security advisory footer banner
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = DeepBlue40.copy(alpha = 0.06f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Shield SEC",
                                tint = DeepBlue40,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Keamanan Finansial Terjamin",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = DeepBlue40
                                )
                                Text(
                                    text = "Semua transaksi dilindungi dengan keamanan enkripsi ganda HunterSecure.",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Show fast topup dialog when requested
        if (showTopUpDialog) {
            QuickTopUpDialog(
                onDismiss = { showTopUpDialog = false },
                onConfirm = { amount, bank ->
                    viewModel.topupHunterPay(amount, bank)
                    showTopUpDialog = false
                }
            )
        }
    }
}

@Composable
fun GridIconItem(
    icon: ImageVector,
    label: String,
    containerColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(88.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(containerColor, CircleShape)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
fun MockupGridIconLarge(
    icon: ImageVector,
    label: String,
    circleColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(68.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(circleColor, CircleShape)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 13.sp,
            maxLines = 2
        )
    }
}

@Composable
fun MockupGridIconMini(
    icon: ImageVector,
    label: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(68.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF475569),
            textAlign = TextAlign.Center,
            lineHeight = 13.sp,
            maxLines = 2
        )
    }
}
