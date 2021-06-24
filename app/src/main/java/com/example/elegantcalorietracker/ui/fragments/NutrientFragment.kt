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
        val nutrients = sharedViewModel.getDailyNutrition()
        nutrientNutritionLl.setNutrients(nutrients)
    }
}
