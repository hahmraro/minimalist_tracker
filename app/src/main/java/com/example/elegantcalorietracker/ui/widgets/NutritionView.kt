package com.example.elegantcalorietracker.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.elegantcalorietracker.databinding.LlNutritionBinding
import com.example.elegantcalorietracker.ui.adapters.NutrientAdapter

class NutritionView(context: Context, attributeSet: AttributeSet?) :
    LinearLayoutCompat(context, attributeSet) {

    private val binding = LlNutritionBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setNutrients(nutrients: List<Double>) {
        val nutrientsNames = mutableListOf<String>()
        if (nutrients.size > 9) nutrientsNames.add("Serving Size")
        nutrientsNames.addAll(
            listOf(
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
        )
        val nutrientInformation = nutrientsNames.zip(nutrients)
        val adapter = NutrientAdapter(nutrientInformation)
        binding.nutrientList.adapter = adapter
    }

    fun getServingSize(): Double {
        val adapter = binding.nutrientList.adapter as NutrientAdapter
        return adapter.getCalories()
    }
}
