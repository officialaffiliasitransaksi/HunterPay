package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsRailway
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Train
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
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.navigation.Screen
import com.example.ui.theme.CoolTeal40
import com.example.ui.theme.DeepBlue40
import com.example.ui.theme.EnergeticOrange40
import com.example.ui.viewmodel.HunterPayViewModel
import com.example.ui.viewmodel.TrainOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainsScreen(
    viewModel: HunterPayViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToBookings: () -> Unit
) {
    var expandedOrigin by remember { mutableStateOf(false) }
    var expandedDest by remember { mutableStateOf(false) }

    val origin by viewModel.searchTrainOrigin.collectAsState()
    val destination by viewModel.searchTrainDest.collectAsState()
    val travelDate by viewModel.searchTrainDate.collectAsState()
    val trainsList by viewModel.trainResults.collectAsState()

    var showPassengerDialog by remember { mutableStateOf<TrainOption?>(null) }
    var passengerName by remember { mutableStateOf("Yudha Hunter") }

    Scaffold(
        topBar = {
            AppHeader(
                title = "Tiket Kereta HunterPay",
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
            // Railway Form parameters
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Stasiun Asal", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedOrigin = true }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Train, contentDescription = null, tint = DeepBlue40)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = origin, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        }
                        DropdownMenu(expanded = expandedOrigin, onDismissRequest = { expandedOrigin = false }) {
                            viewModel.trainStations.forEach { stat ->
                                DropdownMenuItem(
                                    text = { Text(stat) },
                                    onClick = {
                                        viewModel.setTrainOrigin(stat)
                                        expandedOrigin = false
                                        viewModel.searchTrains()
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.LightGray.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Stasiun Tujuan", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedDest = true }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Train, contentDescription = null, tint = CoolTeal40)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = destination, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        }
                        DropdownMenu(expanded = expandedDest, onDismissRequest = { expandedDest = false }) {
                            viewModel.trainStations.forEach { stat ->
                                DropdownMenuItem(
                                    text = { Text(stat) },
                                    onClick = {
                                        viewModel.setTrainDest(stat)
                                        expandedDest = false
                                        viewModel.searchTrains()
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.LightGray.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tanggal Berangkat", fontSize = 11.sp, color = Color.Gray)
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
                            Text("Jumlah Penumpang", fontSize = 11.sp, color = Color.Gray)
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = DeepBlue40, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "1 Dewasa", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Text(
                text = "Kereta Api yang Tersedia",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            if (trainsList.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.DirectionsRailway,
                    message = "Jadwal Kereta Tidak Ditemukan",
                    subMessage = "Silakan coba ubah relasi stasiun asal atau tujuan."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    items(trainsList) { train ->
                        TrainOptionCard(
                            train = train,
                            onSelect = { showPassengerDialog = train }
                        )
                    }
                }
            }
        }

        // Checkout Passenger details Dialog
        showPassengerDialog?.let { train ->
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
                            text = "Konfirmasi Tiket Kereta 🚂",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = train.name,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = DeepBlue40
                        )
                        Text(
                            text = "$origin ke $destination",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Kelas: ${train.carriageClass} • Berangkat: ${train.departureTime}",
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Biaya Tiket: ${viewModel.formatRupiah(train.price)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = EnergeticOrange40,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = passengerName,
                            onValueChange = { passengerName = it },
                            label = { Text("Nama Penumpang Utama") },
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
                                    viewModel.bookTrainTicket(train, passengerName) { success ->
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
fun TrainOptionCard(
    train: TrainOption,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
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
                    text = train.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Box(
                    modifier = Modifier
                        .background(CoolTeal40.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = train.carriageClass,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CoolTeal40
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = train.departureTime, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "Berangkat", fontSize = 10.sp, color = Color.Gray)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DirectionsRailway, contentDescription = null, tint = CoolTeal40.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Rute Utama", fontSize = 11.sp, color = Color.Gray)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = train.arrivalTime, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "Tiba", fontSize = 10.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rp " + String.format("%,.0f", train.price),
                    color = EnergeticOrange40,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
                Button(
                    onClick = onSelect,
                    colors = ButtonDefaults.buttonColors(containerColor = DeepBlue40)
                ) {
                    Text("Pesan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
