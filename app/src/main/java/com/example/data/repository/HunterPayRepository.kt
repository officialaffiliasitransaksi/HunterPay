package com.example.data.repository

import com.example.data.dao.HunterPayDao
import com.example.data.model.UserProfile
import com.example.data.model.Transaction
import com.example.data.model.TravelBooking
import kotlinx.coroutines.flow.Flow

class HunterPayRepository(private val dao: HunterPayDao) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val transactions: Flow<List<Transaction>> = dao.getAllTransactions()
    val bookings: Flow<List<TravelBooking>> = dao.getAllBookings()

    suspend fun getUserProfileSync(): UserProfile? = dao.getUserProfileSync()

    suspend fun saveUserProfile(profile: UserProfile) {
        dao.insertUserProfile(profile)
    }

    suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransaction(transaction)
    }

    suspend fun insertBooking(booking: TravelBooking) {
        dao.insertBooking(booking)
    }

    suspend fun topUpBalance(amount: Double, refNo: String, description: String) {
        val current = getUserProfileSync()
        val currentBalance = current?.balance ?: 0.0
        val currentPoints = current?.points ?: 0
        val newBalance = currentBalance + amount
        // Earn some points too!
        val newPoints = currentPoints + (amount / 10000).toInt()
        
        val updatedProfile = current?.copy(balance = newBalance, points = newPoints) 
            ?: UserProfile(
                name = "Yudha Hunter",
                balance = newBalance,
                points = newPoints,
                bcaVa = "3901082649031201",
                mandiriVa = "8902882649031201",
                briVa = "1029082649031201"
            )
        
        val topupTx = Transaction(
            type = "TOPUP",
            amount = amount,
            description = description,
            refNo = refNo,
            status = "SUCCESS"
        )
        dao.updateBalanceAndLog(newBalance, topupTx)
        // Ensure profile exists or is updated
        dao.insertUserProfile(updatedProfile)
    }

    suspend fun makePayment(amount: Double, txType: String, description: String, refNo: String): Boolean {
        val currentProfile = getUserProfileSync() ?: return false
        if (currentProfile.balance < amount) {
            return false // Insufficient balance
        }
        val newBalance = currentProfile.balance - amount
        val paymentTx = Transaction(
            type = txType,
            amount = -amount,
            description = description,
            refNo = refNo,
            status = "SUCCESS"
        )
        dao.updateBalanceAndLog(newBalance, paymentTx)
        return true
    }

    suspend fun makePaymentAndBook(
        amount: Double,
        txType: String,
        description: String,
        refNo: String,
        booking: TravelBooking
    ): Boolean {
        val currentProfile = getUserProfileSync() ?: return false
        if (currentProfile.balance < amount) {
            return false // Insufficient balance
        }
        val newBalance = currentProfile.balance - amount
        val newPoints = currentProfile.points + (amount / 50000).toInt() // Earn points
        
        val paymentTx = Transaction(
            type = txType,
            amount = -amount,
            description = description,
            refNo = refNo,
            status = "SUCCESS"
        )
        
        // Update profile balance & points, add transaction and add flight/hotel ticket
        dao.insertUserProfile(currentProfile.copy(balance = newBalance, points = newPoints))
        dao.insertTransaction(paymentTx)
        dao.insertBooking(booking)
        return true
    }
}
