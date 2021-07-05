package com.pegoraro.minimalisttracker.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.pegoraro.minimalisttracker.R
import com.pegoraro.minimalisttracker.databinding.CardViewNutritionBinding
import com.pegoraro.minimalisttracker.ui.adapters.NutrientAdapter

class NutritionView(context: Context, attributeSet: AttributeSet?) :
    LinearLayoutCompat(context, attributeSet) {

    private val binding = CardViewNutritionBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val defaultNutrientNames =
        resources.getStringArray(R.array.default_nutrients)

    fun makeAdapter(
        nutrients: List<Double>,
        isListFromIndividualFood: Boolean = false,
        clickListener: OnClickListener? = null,
        swap: Boolean = false,
    ) {
        val nutrientsNames = mutableListOf<String>()
        val servingSizeText = resources.getString(R.string.serving_size)
        if (isListFromIndividualFood) nutrientsNames.add(servingSizeText)
        nutrientsNames.addAll(defaultNutrientNames)
        val nutrientInformation = nutrientsNames.zip(nutrients)
        val adapter =
            NutrientAdapter(
                nutrientInformation,
                isListFromIndividualFood,
                clickListener
            )
        if (swap) {
            binding.nutrientList.swapAdapter(adapter, false)
        } else {
            binding.nutrientList.adapter = adapter
        }
    }

    fun getFirstNutrientValue(): Double {
        val adapter = binding.nutrientList.adapter as NutrientAdapter
        return adapter.getFirstElementValue()
    }
}
