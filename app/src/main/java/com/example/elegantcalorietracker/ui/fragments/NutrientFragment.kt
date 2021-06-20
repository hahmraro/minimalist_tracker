package com.example.elegantcalorietracker.ui.fragments

import android.view.View
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.databinding.FragmentNutrientBinding

private const val TAG = "NutrientFragment"

class NutrientFragment :
    BaseFragment<FragmentNutrientBinding>(
        R.layout.fragment_nutrient,
        topLevelAndCanHaveUpButton = true
    ) {

    override fun applyBinding(v: View): ApplyTo<FragmentNutrientBinding> = {
        food = sharedViewModel.getDailyNutrition()
    }
}
