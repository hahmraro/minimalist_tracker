package com.example.elegantcalorietracker.ui.fragments

import android.view.View
import com.example.elegantcalorietracker.databinding.FragmentNutrientBinding

private const val TAG = "NutrientFragment"

class NutrientFragment :
    BaseFragment<FragmentNutrientBinding>(
        FragmentNutrientBinding::inflate,
        topLevelAndCanHaveUpButton = true
    ) {

    override fun applyBinding(v: View): ApplyTo<FragmentNutrientBinding> = {
        val food = sharedViewModel.getDailyNutrition()
        nutrientNutritionLl.apply {
            setCalories(food.calories)
            setFiber(food.fiber)
            setSugar(food.sugar)
            setCarbs(food.totalCarbs)
            setSaturatedFat(food.saturatedFat)
            setFat(food.totalFat)
            setProtein(food.protein)
            setSodium(food.sodium)
            setPotassium(food.potassium)
            setCholesterol(food.cholesterol)
        }
    }
}
