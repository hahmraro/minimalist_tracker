package com.example.elegantcalorietracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.data.model.ListType

/**
 * Data access object used by [FoodDatabase]
 *
 * Has all the methods for reading and manipulating [FoodDatabase]
 */
@Dao
interface FoodDao {

    // Suspend methods

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(foodList: List<Food>)

    @Insert(onConflict = REPLACE)
    suspend fun insertOne(food: Food)

    @Update
    suspend fun update(food: Food)

    @Delete
    suspend fun delete(food: Food)

    /**
     * Deletes every food of which [ListType] doesn't equals [ListType.HISTORY]
     */
    @Query("DELETE FROM saved_foods_table WHERE list_type != 4")
    suspend fun clearAllExceptHistory()

    /**
     * Deletes only the foods of which [ListType] equals [ListType.HISTORY]
     */
    @Query("DELETE FROM saved_foods_table WHERE list_type = 4")
    suspend fun clearHistory()

    /**
     * Retrieves every food of which [ListType] doesn't equals [ListType.HISTORY]
     *
     * @return a [List] of [Food]
     */
    @Query("SELECT * FROM saved_foods_table WHERE list_type != 4")
    suspend fun getAllExceptHistory(): List<Food>

    // Non-suspend methods

    /**
     * Retrieves all the foods that match the specified [ListType]
     *
     * @param listType an integer that represents a [ListType.ordinal]
     */
    @Query("SELECT * FROM saved_foods_table WHERE list_type = :listType")
    fun get(listType: Int): LiveData<List<Food>>

    /**
     * Retrieves the sum of the [Food.calories] of all the foods which
     * [ListType] isn't [ListType.HISTORY]
     *
     * @return a [LiveData] object of type [Double] that represents the sum
     * of the [Food.calories]
     */
    @Query("SELECT TOTAL(calories) FROM saved_foods_table WHERE list_type != 4")
    fun getKcal(): LiveData<Double>
}
