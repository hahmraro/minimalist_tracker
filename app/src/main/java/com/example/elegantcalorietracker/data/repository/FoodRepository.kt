package com.example.elegantcalorietracker.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.api.CalorieNinjasApi
import com.example.elegantcalorietracker.data.database.FoodDatabase
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.utils.ConnectionChecker
import com.example.elegantcalorietracker.utils.FoodNotFound
import com.example.elegantcalorietracker.utils.NoConnection
import java.util.*

class FoodRepository(val context: Context) {

    private val sharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context)
    private val datePreferencesKey =
        context.getString(R.string.date_preferences_key)
    private val goalPreferencesKey =
        context.getString(R.string.goal_preferences_key)

    private val db = FoodDatabase.getInstance(context).foodDao
    private val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    suspend fun searchFood(query: String, mealType: Int): List<Food> {
        if (!ConnectionChecker.isOnline()) throw NoConnection()
        val list = CalorieNinjasApi.retrofitService.getFoodList(query).items
        when {
            list.isEmpty() -> throw FoodNotFound()
            else -> {
                for (food in list) {
                    food.listType = mealType
                }
                db.insert(list)
                return list
            }
        }
    }

    fun getSavedGoal(): MutableLiveData<Int> {
        val savedGoal = sharedPreferences.getInt(goalPreferencesKey, 2000)
        return MutableLiveData(savedGoal)
    }

    fun setSavedGoal(newSavedGoal: Int) {
        sharedPreferences.edit().putInt(goalPreferencesKey, newSavedGoal)
            .apply()
    }

    fun saveDate() {
        sharedPreferences.edit().putInt(datePreferencesKey, today).apply()
    }

    fun isSavedDateToday(): Boolean {
        val savedDay = sharedPreferences.getInt(datePreferencesKey, 0)
        return today == savedDay
    }

    suspend fun addFood(food: Food) {
        db.insertOne(food)
    }

    suspend fun deleteFood(food: Food) {
        db.delete(food)
    }

    suspend fun editFood(food: Food): Food {
        db.update(food)
        return food
    }

    suspend fun clearFoods() {
        db.clear()
    }

    suspend fun clearHistory() {
        db.clearHistory()
    }

    suspend fun getAll(): List<Food> {
        return db.getAll()
    }

    fun getKcal(): LiveData<Double> {
        return db.getKcal()
    }

    fun getFoodList(listType: Int): LiveData<List<Food>> {
        return db.get(listType)
    }
}
