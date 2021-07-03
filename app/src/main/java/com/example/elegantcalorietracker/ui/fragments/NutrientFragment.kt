package com.example.elegantcalorietracker.ui.fragments

import android.view.View
import com.example.elegantcalorietracker.databinding.FragmentNutrientBinding

class NutrientFragment :
    BaseFragment<FragmentNutrientBinding>(
        FragmentNutrientBinding::inflate,
        topLevelAndCanHaveUpButton = true
    ) {

    override fun applyBinding(v: View): ApplyTo<FragmentNutrientBinding> = {
        val nutrients = sharedViewModel.getNutrientSumOfSavedFoods()
        nutrientNutritionLl.makeAdapter(nutrients)
    }
}
