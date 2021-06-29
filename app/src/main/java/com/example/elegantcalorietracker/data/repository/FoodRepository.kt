package com.example.elegantcalorietracker.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.api.RemoteDataSource
import com.example.elegantcalorietracker.data.database.FoodDatabase
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.data.model.ListType
import com.example.elegantcalorietracker.utils.ConnectionChecker
import com.example.elegantcalorietracker.utils.FoodNotFoundException
import com.example.elegantcalorietracker.utils.NoConnectionException
import java.util.*

/**
 * Single source of truth of the app
 *
 * Every communication with the application local and remote data sources is
 * mediated by this class
 */
class FoodRepository(val context: Context) {

    // Shared Preferences
    private val sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context)

    // Shared Preferences Keys
    private val datePreferencesKey =
        context.getString(R.string.date_preferences_key)
    private val goalPreferencesKey =
        context.getString(R.string.goal_preferences_key)

    // Local Data Source
    private val localDataSource = FoodDatabase.getInstance(context).foodDao

    // Remote Data Source
    private val remoteDataSource = RemoteDataSource.httpClient

    // Variable that stores the current day of the week as an integer
    private val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    // Public suspend methods

    /**
     * Searches for the [query] on the [remoteDataSource]
     *
     * If no internet connection is found, throws [NoConnectionException], and
     * if the search turns up empty, throws [FoodNotFoundException]
     *
     * Returns a [List] of [Food] and saves it to the [localDataSource],
     * setting each [Food.listType] to the specified [listType]
     */
    suspend fun searchFood(query: String, listType: Int): List<Food> {
        if (!ConnectionChecker.isOnline()) throw NoConnectionException()
        val list = remoteDataSource.getFoodList(query).items
        when {
            list.isEmpty() -> throw FoodNotFoundException()
            else -> {
                for (food in list) {
                    food.listType = listType
                }
                localDataSource.insertAll(list)
                return list
            }
        }
    }

    /** Adds the [food] to the [localDataSource] */
    suspend fun addFood(food: Food) {
        localDataSource.insertOne(food)
    }

    /** Deletes the [food] from the [localDataSource] */
    suspend fun deleteFood(food: Food) {
        localDataSource.delete(food)
    }

    /** If the [food] is saved in the [localDataSource], replace it and return
     * it
     */
    suspend fun editFood(food: Food): Food {
        localDataSource.update(food)
        return food
    }

    /** Clear all the [Food] objects in the [localDataSource] that don't have
     * its [ListType] set as [ListType.HISTORY]
     */
    suspend fun clearFoods() {
        localDataSource.clearAllExceptHistory()
    }

    /** Clear all the [Food] objects in the [localDataSource] that have
     * its [ListType] set as [ListType.HISTORY]
     */
    suspend fun clearHistory() {
        localDataSource.clearHistory()
    }

    /** Returns a [List] of all the [Food] objects saved in the
     * [localDataSource] that don't have its [ListType] set as
     * [ListType.HISTORY]
     */
    suspend fun getAll(): List<Food> {
        return localDataSource.getAllExceptHistory()
    }

    // Public synchronous methods

    /**
     * Retrieves the value stored in the [goalPreferencesKey] saved in the
     * default [sharedPreferences]
     */
    fun getSavedGoal(): Int {
        val savedGoal = sharedPreferences.getString(
            goalPreferencesKey,
            "2000"
        )
        return savedGoal?.toInt() ?: 2000
    }

    /**
     * Sets the value of the [goalPreferencesKey] saved in the default
     * [sharedPreferences]
     */
    fun setSavedGoal(newSavedGoal: Int) {
        sharedPreferences.edit()
            .putString(goalPreferencesKey, newSavedGoal.toString())
            .apply()
    }

    /**
     * Assigns [today] to the value of the [datePreferencesKey] saved in the
     * default [sharedPreferences]
     */
    fun saveDate() {
        sharedPreferences.edit().putInt(datePreferencesKey, today).apply()
    }

    /**
     * Returns whether or not the value of [datePreferencesKey] is equal to
     * [today]
     */
    fun isSavedDateToday(): Boolean {
        val savedDay = sharedPreferences.getInt(datePreferencesKey, 0)
        return today == savedDay
    }

    /**
     * Returns a [LiveData] with the sum of all [Food.calories] of the every
     * [Food] object saved in the [localDataSource] that don't have its
     * [ListType] set to [ListType.HISTORY]
     */
    fun getKcal(): LiveData<Double> {
        return localDataSource.getKcal()
    }

    /**
     * Returns a [LiveData] with a [List] of every [Food] object saved in the
     * [localDataSource] that have its [ListType] set to the specified [listType]
     */
    fun getFoodList(listType: Int): LiveData<List<Food>> {
        return localDataSource.get(listType)
    }
}
