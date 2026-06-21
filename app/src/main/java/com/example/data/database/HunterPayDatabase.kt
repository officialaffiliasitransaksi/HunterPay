package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.dao.HunterPayDao
import com.example.data.model.UserProfile
import com.example.data.model.Transaction
import com.example.data.model.TravelBooking

@Database(
    entities = [UserProfile::class, Transaction::class, TravelBooking::class],
    version = 1,
    exportSchema = false
)
abstract class HunterPayDatabase : RoomDatabase() {
    abstract fun hunterPayDao(): HunterPayDao
}
