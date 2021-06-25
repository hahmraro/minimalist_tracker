package com.example.elegantcalorietracker.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.data.model.ListType
import com.example.elegantcalorietracker.data.repository.FoodRepository
import com.example.elegantcalorietracker.utils.sum
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

enum class ApiStatus { LOADING, ERROR, DONE }
enum class ModType { EDIT, ADD }

private const val TAG = "TrackerViewModel"

class TrackerViewModel(application: Application) :
    AndroidViewModel(application) {
    // Repository
    private val _repository = FoodRepository(application)

    // If the saved foods are from another day, clear the saved foods and
    // saves today's date
    init {
        viewModelScope.launch {
            _repository.apply {
                if (!isSavedDateToday()) {
                    clearFoods()
                    saveDate()
                }
            }
        }
    }

    // Selected food
    var selectedFood: Food = Food()

    // Mod type
    var modType = ModType.EDIT

    // Calories
    val calories = _repository.getKcal()
    val caloriesGoal = MutableLiveData(2000.0)
    val caloriesRemaining = MediatorLiveData<Double>().apply {
        value = 0.0
        val subtract: (x: Double, y: Double) -> Double = { x, y -> x - y }
        addSource(caloriesGoal) { value = subtract(it, calories.value ?: 0.0) }
        addSource(calories) { value = subtract(caloriesGoal.value ?: 0.0, it) }
    }

    // Meal
    var listType = ListType.BREAKFAST.ordinal

    // Status of the request
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    // Food lists
    private val _breakfastList =
        _repository.getFoodList(ListType.BREAKFAST.ordinal)
    private val _lunchList = _repository.getFoodList(ListType.LUNCH.ordinal)
    private val _dinnerList = _repository.getFoodList(ListType.DINNER.ordinal)
    private val _snacksList = _repository.getFoodList(ListType.SNACKS.ordinal)
    private val _historyList = _repository.getFoodList(ListType.HISTORY.ordinal)

    // Public methods
    fun getList(listType: ListType): LiveData<List<Food>> {
        return when (listType) {
            ListType.BREAKFAST -> _breakfastList
            ListType.LUNCH -> _lunchList
            ListType.DINNER -> _dinnerList
            ListType.SNACKS -> _snacksList
            ListType.HISTORY -> _historyList
        }
    }

    fun clearFoods() {
        viewModelScope.launch {
            _repository.clearFoods()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            _repository.clearHistory()
        }
    }

    fun deleteFood(food: Food) {
        viewModelScope.launch {
            _repository.deleteFood(food)
        }
    }

    fun moveFood(food: Food, listType: ListType) {
        viewModelScope.launch {
            _repository.deleteFood(food)
            _repository.addFood(food.copy(listType = listType.ordinal))
        }
    }

    suspend fun getFoods(query: String) {
        val foods = _repository.searchFood(query, listType)
        setHistory(foods)
    }

    fun getFood(food: Food, servingSize: Double) {
        val newFood = food.copy(id = Random.nextInt())
            .edit(servingSize, listType)
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _repository.addFood(newFood)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                Log.d(TAG, e.toString())
            }
        }
    }

    fun editFood(food: Food, newServingSize: Double) {
        food.edit(newServingSize, listType)
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _repository.editFood(food)
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                Log.d(TAG, e.toString())
            }
        }
    }

    fun setSearchMeal(mealType: Int) {
        this.listType = mealType
    }

    fun getDailyNutrition(): List<Double> {
        val totalNutrition = runBlocking { _repository.getAll() }.sum()
        return totalNutrition.getNutrients()
    }

    // Private methods
    private fun setHistory(foodList: List<Food>) {
        if (_historyList.value == null || foodList.isEmpty()) return
        for (food in foodList) {
            if (!_historyList.value!!.any { it.name == food.name }) {
                viewModelScope.launch {
                    try {
                        _repository.addFood(
                            food.copy(
                                id = Random.nextInt(),
                                listType = ListType.HISTORY.ordinal
                            )
                        )
                    } catch (e: Exception) {
                        Log.d(TAG, e.toString())
                    }
                }
            }
        }
    }
}
