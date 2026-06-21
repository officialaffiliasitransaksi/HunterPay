package com.example.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Beranda")
    object Flights : Screen("flights", "Tiket Pesawat")
    object Hotels : Screen("hotels", "Hotel")
    object Trains : Screen("trains", "Tiket Kereta")
    object Pulsa : Screen("pulsa", "Pulsa & Paket Data")
    object Pln : Screen("pln", "Listrik PLN")
    object Bpjs : Screen("bpjs", "BPJS Kesehatan")
    object Bookings : Screen("bookings", "Pesanan Saya")
    object History : Screen("history", "Riwayat")
    object Chat : Screen("chat", "HunterPAI")
}
