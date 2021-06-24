package com.example.elegantcalorietracker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elegantcalorietracker.data.model.Nutrient
import com.example.elegantcalorietracker.databinding.ItemNutrientBinding

class NutrientAdapter(private val nutrients: List<Nutrient>) :
    RecyclerView.Adapter<NutrientAdapter.NutrientViewHolder>() {

    fun getCalories(): Double = if (nutrients.size > 9) {
        nutrients.first().second
    } else {
        throw Exception()
    }

    class NutrientViewHolder(
        private var binding: ItemNutrientBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nutrient: Pair<String, Double>) {
            binding.nutritionName.text = nutrient.first
            binding.nutritionSize.text = "${nutrient.second}g"
        }
    }

    override fun getItemCount(): Int {
        return nutrients.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NutrientViewHolder {
        val view = ItemNutrientBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NutrientViewHolder(view)
    }

    override fun onBindViewHolder(holder: NutrientViewHolder, position: Int) {
        val nutrient = nutrients[position]
        holder.bind(nutrient)
    }
}
