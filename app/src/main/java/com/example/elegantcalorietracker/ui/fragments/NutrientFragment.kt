package com.example.elegantcalorietracker.ui.fragments

import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.databinding.FragmentNutrientBinding

private const val TAG = "NutrientFragment"

class NutrientFragment :
    BaseFragment<FragmentNutrientBinding>(
        R.layout.fragment_nutrient,
        isTopLevelAndNeedUpButton = true
    ) {

    override fun applyBinding(): ApplyTo<FragmentNutrientBinding> = {
        food = sharedViewModel.getDailyNutrition()
    }
}
