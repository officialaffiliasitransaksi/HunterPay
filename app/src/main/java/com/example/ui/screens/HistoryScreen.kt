package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.PhonelinkRing
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppHeader
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.theme.CoolTeal40
import com.example.ui.theme.DeepBlue40
import com.example.ui.theme.EnergeticOrange40
import com.example.data.model.Transaction
import com.example.ui.viewmodel.HunterPayViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: HunterPayViewModel,
    onNavigateBack: () -> Unit
) {
    val txList by viewModel.transactions.collectAsState()

    Scaffold(
        topBar = {
            AppHeader(
                title = "Riwayat Transaksi",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (txList.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.History,
                    message = "Belum Ada Transaksi",
                    subMessage = "Lakukan topup saldo atau pembelian tiket untuk melihat histori pengeluaran Anda."
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(txList) { tx ->
                        TransactionItemCard(
                            tx = tx,
                            formatPrice = { viewModel.formatRupiah(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItemCard(
    tx: Transaction,
    formatPrice: (Double) -> String
) {
    val isTopUp = tx.type == "TOPUP"
    val amountColor = if (isTopUp) Color(0xFF10B981) else Color(0xFFEF4444)
    val amountSign = if (isTopUp) "+" else ""

    val (icon: ImageVector, iconContainerColor: Color, iconTint: Color) = when (tx.type) {
        "TOPUP" -> Triple(Icons.Default.AddCard, Color(0xFFD1FAE5), Color(0xFF059669))
        "FLIGHT" -> Triple(Icons.Default.FlightTakeoff, Color(0xFFE0F2FE), Color(0xFF0284C7))
        "HOTEL" -> Triple(Icons.Default.Hotel, Color(0xFFFEE2E2), Color(0xFFDC2626))
        "TRAIN" -> Triple(Icons.Default.Train, Color(0xFFFEF3C7), Color(0xFFD97706))
        "PULSA" -> Triple(Icons.Default.PhonelinkRing, Color(0xFFE0F2FE), CoolTeal40)
        "PLN" -> Triple(Icons.Default.OfflineBolt, Color(0xFFFFF7ED), Color(0xFFEA580C))
        else -> Triple(Icons.Default.ReceiptLong, Color(0xFFF3F4F6), Color(0xFF4B5563))
    }

    val timestampDate = Date(tx.timestamp)
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    val dateDisplay = sdf.format(timestampDate)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Type icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(iconContainerColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = tx.type,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = tx.description,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Ref: ${tx.refNo} • $dateDisplay",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

            // Price statement
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$amountSign${formatPrice(tx.amount)}",
                    color = amountColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
                
                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success tick",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = tx.status,
                        fontSize = 9.sp,
                        color = Color(0xFF10B981),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
