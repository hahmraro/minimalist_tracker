package com.example.elegantcalorietracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
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
    suspend fun insertFoods(foodList: List<Food>)

    @Insert(onConflict = REPLACE)
    suspend fun insertFood(food: Food)

    @Update
    suspend fun updateFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Query("DELETE FROM saved_foods_table WHERE list_type != 4")
    suspend fun clearAllExceptHistoryFoods()

    @Query("DELETE FROM saved_foods_table WHERE list_type = 4")
    suspend fun clearOnlyHistoryFoods()

    @Query("SELECT * FROM saved_foods_table WHERE list_type != 4")
    suspend fun getAllExceptHistoryFoods(): List<Food>

    // Non-suspend methods

    @Query("SELECT * FROM saved_foods_table WHERE list_type = :listType")
    fun getAllFoodsWithListType(listType: Int): LiveData<List<Food>>

    /**
     * Retrieves the sum of the [Food.calories] of all the foods which
     * [ListType] isn't [ListType.HISTORY]
     *
     * @return a [LiveData] object of type [Double] that represents the sum
     * of the [Food.calories]
     */
    @Query("SELECT TOTAL(calories) FROM saved_foods_table WHERE list_type != 4")
    fun getKcalSum(): LiveData<Double>
}
