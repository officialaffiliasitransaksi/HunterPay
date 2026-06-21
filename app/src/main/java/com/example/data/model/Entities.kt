package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val balance: Double,
    val points: Int,
    val bcaVa: String,
    val mandiriVa: String,
    val briVa: String
)

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "FLIGHT", "HOTEL", "TRAIN", "PLN", "BPJS", "PULSA", "TOPUP"
    val amount: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val refNo: String,
    val status: String // "SUCCESS", "PENDING", "FAILED"
)

@Entity(tableName = "travel_bookings")
data class TravelBooking(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "FLIGHT", "HOTEL", "TRAIN"
    val title: String, // e.g. "Jakarta (CGK) to Bali (DPS)"
    val detailInfo: String, // e.g. "Garuda Indonesia - Boeing 737\nDepart: Mon, 25 Jun 10:00" or "Grand Indonesia Suite Room\nCheck-in: 28 Jun"
    val datetime: String, // display date/time
    val paxName: String, // passenger or guest name
    val amountPaid: Double,
    val bookingRef: String,
    val status: String // "UPCOMING", "COMPLETED"
)
