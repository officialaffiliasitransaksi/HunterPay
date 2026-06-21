package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wifi
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
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.theme.CoolTeal40
import com.example.ui.theme.DeepBlue40
import com.example.ui.theme.EnergeticOrange40
import com.example.ui.viewmodel.HotelOption
import com.example.ui.viewmodel.HunterPayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelsScreen(
    viewModel: HunterPayViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToBookings: () -> Unit
) {
    val activeCity by viewModel.searchHotelCity.collectAsState()
    val checkInDate by viewModel.searchHotelCheckIn.collectAsState()
    val checkInNights by viewModel.searchHotelNights.collectAsState()
    val hotelsList by viewModel.hotelResults.collectAsState()

    var showBookingDialog by remember { mutableStateOf<HotelOption?>(null) }
    var guestName by remember { mutableStateOf("Yudha Hunter") }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Hotel Lobby HunterPay",
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
            // City Quick Selector Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                viewModel.hotelCities.forEach { city ->
                    val isSelected = activeCity == city
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            viewModel.setHotelCity(city)
                            viewModel.searchHotels()
                        },
                        label = { Text(city, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DeepBlue40,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Check details card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = CoolTeal40)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Menginap di $activeCity", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NightlightRound, contentDescription = null, tint = EnergeticOrange40, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$checkInNights Malam", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            // Section Label
            Text(
                text = "Rekomendasi Hotel Mewah di $activeCity",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            if (hotelsList.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.Business,
                    message = "Hotel tidak tersedia",
                    subMessage = "Silakan coba cari atau klik kota pihan lainnya."
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(hotelsList) { hotel ->
                        HotelOptionCard(
                            hotel = hotel,
                            onBook = { showBookingDialog = hotel }
                        )
                    }
                }
            }
        }

        // Booking confirmation Dialog
        showBookingDialog?.let { hotel ->
            val totalBill = hotel.pricePerNight * checkInNights
            Dialog(onDismissRequest = { showBookingDialog = null }) {
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
                            text = "Konfirmasi Hotel Booking 🏨",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = hotel.name,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = DeepBlue40
                        )
                        Text(
                            text = hotel.location,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Text(
                            text = "Detail Pesanan:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(text = "Check-In: $checkInDate ($checkInNights Malam)", fontSize = 13.sp)
                        Text(text = "Kamar: Superior Double Bed", fontSize = 13.sp)
                        Text(
                            text = "Total Bayar: ${viewModel.formatRupiah(totalBill)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = EnergeticOrange40,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = guestName,
                            onValueChange = { guestName = it },
                            label = { Text("Nama Tamu Utama") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showBookingDialog = null }) {
                                Text("Batal")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    viewModel.bookHotelRoom(hotel, checkInNights, guestName) { success ->
                                        if (success) {
                                            showBookingDialog = null
                                            onNavigateToBookings()
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

@Composable
fun HotelOptionCard(
    hotel: HotelOption,
    onBook: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = hotel.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(hotel.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = EnergeticOrange40,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = hotel.location, fontSize = 11.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Facilities bullet chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                hotel.amenities.take(3).forEach { amenity ->
                    Row(
                        modifier = Modifier
                            .background(CoolTeal40.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Wifi, contentDescription = null, tint = CoolTeal40, modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = amenity, fontSize = 9.sp, color = CoolTeal40, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Mulai dari", fontSize = 10.sp, color = Color.Gray)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "Rp " + String.format("%,.0f", hotel.pricePerNight),
                            color = EnergeticOrange40,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(text = " / malam", fontSize = 10.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 2.dp))
                    }
                }

                Button(
                    onClick = onBook,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepBlue40),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Pesan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
