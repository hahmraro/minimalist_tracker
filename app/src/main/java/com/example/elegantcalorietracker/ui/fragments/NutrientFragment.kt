package com.example.elegantcalorietracker.ui.fragments

import android.view.View
import com.example.elegantcalorietracker.databinding.FoodNutrientViewBinding

class NutrientFragment :
    BaseFragment<FoodNutrientViewBinding>(
        FoodNutrientViewBinding::inflate,
        topLevelAndCanHaveUpButton = true
    ) {

    override fun applyBinding(v: View): ApplyTo<FoodNutrientViewBinding> = {
        val nutrients = sharedViewModel.getNutrientSumOfSavedFoods()
        nutrition.makeAdapter(nutrients)
    }
}
