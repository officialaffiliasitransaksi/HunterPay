package com.example.data.dao

import androidx.room.*
import com.example.data.model.UserProfile
import com.example.data.model.Transaction
import com.example.data.model.TravelBooking
import kotlinx.coroutines.flow.Flow

@Dao
interface HunterPayDao {

    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileSync(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM travel_bookings ORDER BY id DESC")
    fun getAllBookings(): Flow<List<TravelBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: TravelBooking)
    
    @androidx.room.Transaction
    suspend fun updateBalanceAndLog(newBalance: Double, transaction: Transaction) {
        val currentProfile = getUserProfileSync()
        if (currentProfile != null) {
            insertUserProfile(currentProfile.copy(balance = newBalance))
        }
        insertTransaction(transaction)
    }

    @androidx.room.Transaction
    suspend fun updateBalanceLogAndBooking(newBalance: Double, transaction: Transaction, booking: TravelBooking) {
        val currentProfile = getUserProfileSync()
        if (currentProfile != null) {
            insertUserProfile(currentProfile.copy(balance = newBalance))
        }
        insertTransaction(transaction)
        insertBooking(booking)
    }
}
