package com.example.elegantcalorietracker.ui.widgets

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.databinding.ClCounterBinding
import com.example.elegantcalorietracker.utils.DialogWithTextFieldClickListener
import com.example.elegantcalorietracker.utils.showDialogWithTextField

class Counter(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private val binding: ClCounterBinding = ClCounterBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    // Sets the "triple dot" (More) button click listener. Opens up a dialog 
    // that modifies the saved calories goal
    fun setMoreClickListener(
        hint: String,
        positiveListener: DialogWithTextFieldClickListener? = null
    ) {
        binding.moreIcon.setOnClickListener {
            showDialogWithTextField(
                context,
                title = context.getString(R.string.set_calories_goal),
                hint = hint,
                inputType = InputType.TYPE_CLASS_NUMBER,
                positiveText = context.getString(R.string.save),
                positiveListener = positiveListener
            )
        }
    }

    fun setCalories(calories: Double) {
        binding.caloriesValue.text = formatDouble(calories)
    }

    fun setCaloriesGoal(calories: Int) {
        binding.caloriesGoal.text = calories.toString()
    }

    fun setCaloriesRemaining(calories: Double) {
        binding.caloriesRemaining.text = context.getString(R.string.remaining_value, calories)
        // If the calorie goal is not surpassed, make the remaining calories 
        // Green 
        if (calories >= 0) {
            binding.caloriesRemaining.setTextColor(
                ContextCompat.getColor(context, R.color.safe_color)
            )
            // Else, make it red
        } else {
            binding.caloriesRemaining.setTextColor(
                ContextCompat.getColor(context, R.color.danger_color)
            )
        }
    }

    private fun formatDouble(double: Double) = "%.0f".format(double)
}
