package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplaneTicket
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Launch
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
import com.example.ui.components.BookingReceiptDialog
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.theme.CoolTeal40
import com.example.ui.theme.DeepBlue40
import com.example.ui.theme.EnergeticOrange40
import com.example.data.model.TravelBooking
import com.example.ui.viewmodel.HunterPayViewModel

@Composable
fun BookingsScreen(
    viewModel: HunterPayViewModel,
    onNavigateBack: () -> Unit
) {
    val bookingsList by viewModel.bookings.collectAsState()
    var selectedBookingForReceipt by remember { mutableStateOf<TravelBooking?>(null) }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Pesanan Saya 🎫",
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
            if (bookingsList.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.ConfirmationNumber,
                    message = "Belum Ada Pesanan Aktif",
                    subMessage = "Tiket penerbangan, voucher hotel, atau tiket kereta Anda akan muncul di sini setelah Anda memesannya."
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(bookingsList) { booking ->
                        BookingItemCard(
                            booking = booking,
                            formatPrice = { viewModel.formatRupiah(it) },
                            onOpenReceipt = { selectedBookingForReceipt = booking }
                        )
                    }
                }
            }
        }

        // Show receipt pass when tapped
        selectedBookingForReceipt?.let { b ->
            BookingReceiptDialog(
                booking = b,
                onDismiss = { selectedBookingForReceipt = null }
            )
        }
    }
}

@Composable
fun BookingItemCard(
    booking: TravelBooking,
    formatPrice: (Double) -> String,
    onOpenReceipt: () -> Unit
) {
    val (icon: ImageVector, bannerColor: Color, label: String) = when (booking.type) {
        "FLIGHT" -> Triple(Icons.Default.FlightTakeoff, DeepBlue40, "Penerbangan")
        "HOTEL" -> Triple(Icons.Default.Hotel, CoolTeal40, "Staycation Hotel")
        else -> Triple(Icons.Default.Train, EnergeticOrange40, "Tiket Kereta")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenReceipt),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Card banner header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bannerColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = label, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 11.sp)
                }

                Text(
                    text = booking.status,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp
                )
            }

            // Main body
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = booking.title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Jadwal: " + booking.datetime,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Tamu: " + booking.paxName,
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Dibayar", fontSize = 10.sp, color = Color.Gray)
                        Text(text = formatPrice(booking.amountPaid), fontWeight = FontWeight.Black, fontSize = 14.sp, color = EnergeticOrange40)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("E-Tiket", fontSize = 12.sp, color = DeepBlue40, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.Launch, contentDescription = null, tint = DeepBlue40, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}
