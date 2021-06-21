package com.example.elegantcalorietracker.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.elegantcalorietracker.databinding.LlNutritionBinding

class NutritionView(context: Context, attributeSet: AttributeSet?) :
    LinearLayoutCompat(context, attributeSet) {

    private val binding = LlNutritionBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setCalories(string: String) {
        binding.foodCalories.text = "Calories: $string kcal"
    }

    fun setSugar(string: String) {
        binding.foodSugar.text = "Sugar: $string g"
    }

    fun setFiber(string: String) {
        binding.foodFiber.text = "Fiber: $string g"
    }

    fun setCarbs(string: String) {
        binding.foodCarbs.text = "Total Carbs: $string g"
    }

    fun setSaturatedFat(string: String) {
        binding.foodSaturatedFat.text = "Saturated Fat: $string g"
    }

    fun setFat(string: String) {
        binding.foodFat.text = "Total Fat: $string g"
    }

    fun setProtein(string: String) {
        binding.foodProtein.text = "Protein: $string g"
    }

    fun setSodium(string: String) {
        binding.foodSodium.text = "Sodium: $string mg"
    }

    fun setPotassium(string: String) {
        binding.foodPotassium.text = "Potassium: $string mg"
    }

    fun setCholesterol(string: String) {
        binding.foodCholesterol.text = "Cholesterol: $string mg"
    }
}
