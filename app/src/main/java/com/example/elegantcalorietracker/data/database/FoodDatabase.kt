package com.example.elegantcalorietracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.elegantcalorietracker.data.model.Food

/**
 * Room Database with [Food] data class as the entity and [FoodDao] as the dao
 */
@Database(entities = [Food::class], version = 24, exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {
    abstract val foodDao: FoodDao

    companion object {
        @Volatile
        private var INSTANCE: FoodDatabase? = null
        fun getInstance(context: Context): FoodDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FoodDatabase::class.java,
                        "saved_foods_table"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
