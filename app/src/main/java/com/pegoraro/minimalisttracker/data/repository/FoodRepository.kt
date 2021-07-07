package com.pegoraro.minimalisttracker.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.preference.PreferenceManager
import com.pegoraro.minimalisttracker.R
import com.pegoraro.minimalisttracker.data.api.RemoteDataSource
import com.pegoraro.minimalisttracker.data.database.FoodDatabase
import com.pegoraro.minimalisttracker.data.model.Food
import com.pegoraro.minimalisttracker.data.model.ListType
import com.pegoraro.minimalisttracker.utils.ConnectionChecker
import com.pegoraro.minimalisttracker.utils.FoodNotFoundException
import com.pegoraro.minimalisttracker.utils.NoConnectionException
import java.util.Calendar

/**
 * Single source of truth of the app
 *
 * Every communication with the application local and remote data sources is
 * mediated by this class
 *
 * @param context The application context used to initialize [localDataSource]
 */
class FoodRepository(val context: Context) {

    // Shared Preferences
    private val sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context)

    // Shared Preferences Keys
    private val apiPreferencesKey = context.getString(R.string.api_preferences_key)
    private val datePreferencesKey = context.getString(R.string.date_preferences_key)
    private val goalPreferencesKey = context.getString(R.string.goal_preferences_key)

    // Data Sources
    private val localDataSource = FoodDatabase.getInstance(context).foodDao
    private val remoteDataSource = RemoteDataSource.httpClient

    // Variable that stores the current day of the week as an integer
    private val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    // API key, either the default one or the one set by the user in the app settings
    private val defaultApiKey = context.getString(R.string.api_key)
    private val apiKey =
        sharedPreferences.getString(apiPreferencesKey, defaultApiKey) ?: defaultApiKey

    // Public suspend methods

    /**
     * Searches for the [query] on the [remoteDataSource]
     * @return The [List] of [Food] returned from the [remoteDataSource]
     * @throws NoConnectionException if no internet connection is found
     * @throws FoodNotFoundException if the search turns up empty
     */
    suspend fun searchFoodsThatMatchQuery(query: String): List<Food> {
        if (!ConnectionChecker.isOnline()) throw NoConnectionException()
        val list = remoteDataSource.getFoodList(apiKey, query).items
        if (list.isEmpty()) throw FoodNotFoundException()
        return list
    }

    suspend fun addFoodsToDatabase(foods: List<Food>) {
        localDataSource.insertFoods(foods)
    }

    suspend fun addFoodToDatabase(food: Food) {
        localDataSource.insertFood(food)
    }

    suspend fun deleteFoodFromTheDatabase(food: Food) {
        localDataSource.deleteFood(food)
    }

    suspend fun editFoodFromTheDatabase(food: Food): Food {
        localDataSource.updateFood(food)
        return food
    }

    suspend fun clearNonHistoryFoods() {
        localDataSource.clearAllExceptHistoryFoods()
    }

    suspend fun clearOnlyHistoryFoods() {
        localDataSource.clearOnlyHistoryFoods()
    }

    suspend fun getAllExceptHistoryFoods(): List<Food> {
        return localDataSource.getAllExceptHistoryFoods()
    }

    // Public synchronous methods

    /**
     * Returns a [LiveData] with a [List] of every [Food] object saved in the
     * [localDataSource] that have its [ListType] set to the specified [listType]
     */
    fun getAllFoodsWithListType(listType: ListType): LiveData<List<Food>> {
        return localDataSource.getAllFoodsWithListType(listType.ordinal)
    }

    fun getSavedGoalFromPreferences(): Int {
        val savedGoal = sharedPreferences.getString(
            goalPreferencesKey,
            "2000"
        )
        return savedGoal?.toInt() ?: 2000
    }

    fun setSavedGoalFromPreferences(newSavedGoal: Int) {
        sharedPreferences.edit()
            .putString(goalPreferencesKey, newSavedGoal.toString())
            .apply()
    }

    fun saveTodayDateInPreferences() {
        sharedPreferences.edit().putInt(datePreferencesKey, today).apply()
    }

    fun isSavedDateToday(): Boolean {
        val savedDay = sharedPreferences.getInt(datePreferencesKey, 0)
        return today == savedDay
    }

    /**
     * Returns a [LiveData] with the sum of all [Food.calories] of every [Food]
     * object saved in the [localDataSource] that don't have its [ListType] set
     * to [ListType.HISTORY]
     *
     * @return a [LiveData] object of type [Double]
     */
    fun getKcalSum(): LiveData<Double> {
        return localDataSource.getKcalSum()
    }
}
