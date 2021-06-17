package com.example.elegantcalorietracker.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.elegantcalorietracker.data.api.CalorieNinjasApi
import com.example.elegantcalorietracker.data.database.FoodDatabase
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.utils.ConnectionChecker
import com.example.elegantcalorietracker.utils.FoodNotFound
import com.example.elegantcalorietracker.utils.NoConnection

class FoodRepository(val context: Context) {
    private val db = FoodDatabase.getInstance(context).foodDao
    suspend fun searchFood(query: String, mealType: Int): List<Food> {
        if (!ConnectionChecker.isOnline(context)) throw NoConnection()
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
