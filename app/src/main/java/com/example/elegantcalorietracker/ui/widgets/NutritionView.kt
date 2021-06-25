package com.example.elegantcalorietracker.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.elegantcalorietracker.databinding.CardViewNutritionBinding
import com.example.elegantcalorietracker.ui.adapters.NutrientAdapter

class NutritionView(context: Context, attributeSet: AttributeSet?) :
    LinearLayoutCompat(context, attributeSet) {

    private val binding = CardViewNutritionBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val defaultNames = listOf(
        "Sugar",
        "Fiber",
        "Total Carbs",
        "Saturated Fat",
        "Total Fat",
        "Protein",
        "Sodium",
        "Potassium",
        "Cholesterol"
    )

    fun makeAdapter(
        nutrients: List<Double>,
        requireCalories: Boolean = false,
        clickListener: OnClickListener? = null,
        swap: Boolean = false,
    ) {
        val nutrientsNames = mutableListOf<String>()
        if (nutrients.size > 9) nutrientsNames.add("Serving Size")
        nutrientsNames.addAll(defaultNames)
        val nutrientInformation = nutrientsNames.zip(nutrients)
        val adapter =
            NutrientAdapter(nutrientInformation, requireCalories, clickListener)
        if (swap) {
            binding.nutrientList.swapAdapter(adapter, false)
        } else {
            binding.nutrientList.adapter = adapter
        }
    }

    fun getServingSize(): Double {
        val adapter = binding.nutrientList.adapter as NutrientAdapter
        return adapter.getCalories()
    }
}
