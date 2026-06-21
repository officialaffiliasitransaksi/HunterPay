package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplaneTicket
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Person
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
import com.example.ui.navigation.Screen
import com.example.ui.theme.CoolTeal40
import com.example.ui.theme.DeepBlue40
import com.example.ui.theme.EnergeticOrange40
import com.example.ui.viewmodel.FlightOption
import com.example.ui.viewmodel.HunterPayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightsScreen(
    viewModel: HunterPayViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToBookings: () -> Unit
) {
    var expandedOrigin by remember { mutableStateOf(false) }
    var expandedDest by remember { mutableStateOf(false) }

    val origin by viewModel.searchFlightOrigin.collectAsState()
    val destination by viewModel.searchFlightDest.collectAsState()
    val travelDate by viewModel.searchFlightDate.collectAsState()
    val flightCabin by viewModel.searchFlightCabin.collectAsState()
    val flightsList by viewModel.flightResults.collectAsState()

    var showPassengerDialog by remember { mutableStateOf<FlightOption?>(null) }
    var passengerName by remember { mutableStateOf("Yudha Hunter") }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Tiket Pesawat HunterPay",
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
            // Search Fields Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Origin selection
                    Text(
                        text = "Kota Asal",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedOrigin = true }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.FlightTakeoff, contentDescription = null, tint = DeepBlue40)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = origin, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        }
                        DropdownMenu(
                            expanded = expandedOrigin,
                            onDismissRequest = { expandedOrigin = false }
                        ) {
                            viewModel.flightOrigins.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city) },
                                    onClick = {
                                        viewModel.setFlightOrigin(city)
                                        expandedOrigin = false
                                        viewModel.searchFlights()
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Destination selection
                    Text(
                        text = "Kota Tujuan",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedDest = true }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.FlightLand, contentDescription = null, tint = CoolTeal40)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = destination, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        }
                        DropdownMenu(
                            expanded = expandedDest,
                            onDismissRequest = { expandedDest = false }
                        ) {
                            viewModel.flightDestinations.forEach { city ->
                                DropdownMenuItem(
                                    text = { Text(city) },
                                    onClick = {
                                        viewModel.setFlightDest(city)
                                        expandedDest = false
                                        viewModel.searchFlights()
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tanggal Pergi", fontSize = 11.sp, color = Color.Gray)
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.DateRange, contentDescription = null, tint = CoolTeal40, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = travelDate, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Kelas Kabin", fontSize = 11.sp, color = Color.Gray)
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Class, contentDescription = null, tint = DeepBlue40, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = flightCabin, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Text(
                text = "Penerbangan Tersedia (${flightsList.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            if (flightsList.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.AirplaneTicket,
                    message = "Penerbangan Tidak Ditemukan",
                    subMessage = "Silakan coba ubah pilihan kota asal atau kota tujuan Anda."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    items(flightsList) { flight ->
                        FlightOptionCard(
                            flight = flight,
                            originCode = origin.takeLast(4).take(3),
                            destCode = destination.takeLast(4).take(3),
                            onSelect = { showPassengerDialog = flight }
                        )
                    }
                }
            }
        }

        // Passenger Details Dialog
        showPassengerDialog?.let { flight ->
            Dialog(onDismissRequest = { showPassengerDialog = null }) {
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
                            text = "Konfirmasi Tiket Penerbangan ✈️",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "${flight.airline} (${flight.code})",
                            fontWeight = FontWeight.Bold,
                            color = DeepBlue40
                        )
                        Text(
                            text = "$origin ke $destination",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Jam: ${flight.departureTime} - ${flight.arrivalTime} (${travelDate})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Total Harga: ${viewModel.formatRupiah(flight.price)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = EnergeticOrange40
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = passengerName,
                            onValueChange = { passengerName = it },
                            label = { Text("Nama Lengkap Penumpang") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showPassengerDialog = null }) {
                                Text("Batal")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    viewModel.bookFlightTicket(flight, passengerName) { success ->
                                        if (success) {
                                            showPassengerDialog = null
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
fun FlightOptionCard(
    flight: FlightOption,
    originCode: String,
    destCode: String,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = flight.airline,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DeepBlue40
                )
                Text(
                    text = flight.code,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = flight.departureTime,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = originCode,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Langsung", fontSize = 10.sp, color = CoolTeal40, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = Icons.Default.AirplaneTicket,
                        contentDescription = "route line",
                        tint = CoolTeal40.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                    Text("1j 55m", fontSize = 10.sp, color = Color.Gray)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = flight.arrivalTime,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = destCode,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Luggage, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Basi: 20kg", fontSize = 11.sp, color = Color.Gray)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Rp " + String.format("%,.0f", flight.price),
                        color = EnergeticOrange40,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = onSelect,
                        colors = ButtonDefaults.buttonColors(containerColor = DeepBlue40),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Pilih", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
