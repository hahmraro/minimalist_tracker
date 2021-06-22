package com.example.elegantcalorietracker.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.elegantcalorietracker.databinding.ClCounterBinding

class Counter(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private val binding: ClCounterBinding = ClCounterBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun setCalories(calories: Double) {
        binding.caloriesValue.text = formatDouble(calories)
    }

    fun setCaloriesGoal(calories: Double) {
        binding.caloriesGoal.text = formatDouble(calories)
    }

    fun setCaloriesRemaining(calories: Double) {
        binding.caloriesRemaining.text = formatDouble(calories) + " kcal"
    }

    private fun formatDouble(double: Double): String {
        return "%.0f".format(double)
    }
}
