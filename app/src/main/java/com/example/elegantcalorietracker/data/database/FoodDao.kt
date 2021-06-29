package com.example.elegantcalorietracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.elegantcalorietracker.data.model.Food

/**
 * Data access object used by [FoodDatabase]
 *
 * Has all the methods for reading and manipulating [FoodDatabase]
 */
@Dao
interface FoodDao {
    // Suspend methods
    @Insert(onConflict = REPLACE)
    suspend fun insert(foodList: List<Food>)

    @Insert(onConflict = REPLACE)
    suspend fun insertOne(food: Food)

    @Update
    suspend fun update(food: Food)

    @Delete
    suspend fun delete(food: Food)

    @Query("DELETE FROM saved_foods_table WHERE list_type != 4")
    suspend fun clear()

    @Query("DELETE FROM saved_foods_table WHERE list_type = 4")
    suspend fun clearHistory()

    @Query("SELECT * FROM saved_foods_table WHERE list_type != 4")
    suspend fun getAll(): List<Food>

    // Non-suspend methods
    @Query("SELECT * FROM saved_foods_table WHERE list_type = :listType")
    fun get(listType: Int): LiveData<List<Food>>

    @Query("SELECT TOTAL(calories) FROM saved_foods_table WHERE list_type != 4")
    fun getKcal(): LiveData<Double>
}
