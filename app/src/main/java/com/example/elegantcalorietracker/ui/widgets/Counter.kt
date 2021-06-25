package com.example.elegantcalorietracker.ui.widgets

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.elegantcalorietracker.databinding.ClCounterBinding
import com.example.elegantcalorietracker.utils.DialogWithTextFieldClickListener
import com.example.elegantcalorietracker.utils.showDialogWithTextField

class Counter(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private val binding: ClCounterBinding = ClCounterBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun setMoreClickListener(
        hint: String,
        positiveListener: DialogWithTextFieldClickListener? = null
    ) {
        binding.moreIcon.setOnClickListener {
            showDialogWithTextField(
                context,
                title = "Set calories goal",
                hint = hint,
                inputType = InputType.TYPE_CLASS_NUMBER,
                positiveText = "Save",
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
        binding.caloriesRemaining.text = formatDouble(calories) + " kcal"
        if (calories >= 0) {
            binding.caloriesRemaining.setTextColor(Color.parseColor("#99cc01"))
        } else {
            binding.caloriesRemaining.setTextColor(Color.parseColor("#fe4445"))
        }
    }

    private fun formatDouble(double: Double): String {
        return "%.0f".format(double)
    }
}
