package com.example.data.database

import android.content.Context
import androidx.room.Room
import com.example.data.model.UserProfile
import com.example.data.repository.HunterPayRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseProvider {
    private var database: HunterPayDatabase? = null
    private var repository: HunterPayRepository? = null

    fun getRepository(context: Context): HunterPayRepository {
        if (repository == null) {
            val db = database ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HunterPayDatabase::class.java,
                    "hunterpay_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                database = instance
                instance
            }
            repository = HunterPayRepository(db.hunterPayDao())
            
            // Seed a helpful default profile on first startup
            CoroutineScope(Dispatchers.IO).launch {
                val repo = repository!!
                if (repo.getUserProfileSync() == null) {
                    repo.saveUserProfile(
                        UserProfile(
                            name = "Yudha Hunter",
                            balance = 2500000.0, // Seed 2.5 million Rupiah
                            points = 150,
                            bcaVa = "3901082649031201",
                            mandiriVa = "8902882649031201",
                            briVa = "1029082649031201"
                        )
                    )
                }
            }
        }
        return repository!!
    }
}
