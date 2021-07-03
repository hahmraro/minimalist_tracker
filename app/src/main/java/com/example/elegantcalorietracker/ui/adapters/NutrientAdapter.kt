package com.example.elegantcalorietracker.ui.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.data.model.Nutrient
import com.example.elegantcalorietracker.databinding.ItemNutrientBinding

private const val MICRONUTRIENTS = 3

/**
 * [RecyclerView.Adapter] class that displays a specified list of nutrients of
 * a [Food] object
 *
 * Through [isListFromIndividualFood], it can choose to whether or not to show the
 * [Food.servingSize] property together with the nutrients
 *
 * Can take a [View.OnClickListener] that reacts to user clicks on each item
 */
class NutrientAdapter(
    private val nutrients: List<Nutrient>,
    private val isListFromIndividualFood: Boolean = false,
    private val clickListener: View.OnClickListener? = null
) :
    RecyclerView.Adapter<NutrientAdapter.NutrientViewHolder>() {

    fun getFirstElementValue(): Double = nutrients.first().second

    /**
     * [RecyclerView.ViewHolder] class that binds the list nutrient name and
     * serving size to [ItemNutrientBinding]
     */
    class NutrientViewHolder(
        private var binding: ItemNutrientBinding,
        private val micronutrients: List<Nutrient>
    ) : RecyclerView.ViewHolder(binding.root) {

        // Resources
        private val resources: Resources? = itemView.resources

        // Bind function
        fun bind(nutrient: Nutrient) {
            // If the nutrient is a micronutrient, use the micronutrient 
            // string, else use the normal nutrient string
            val nutrientString = if (micronutrients.contains(nutrient)) {
                R.string.food_micronutrient
            } else {
                R.string.food_nutrient
            }
            // Sets the nutrient name and its respective serving size to the 
            // binding nutritionName and nutritionSize
            binding.nutritionName.text = nutrient.first
            binding.nutritionSize.text = resources?.getString(
                nutrientString,
                nutrient.second
            )
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

        /**
         * The second parameter of the [NutrientViewHolder] constructor
         * "micronutrients" is the last three elements of the nutrients list,
         * because the micronutrients are always the last three elements, as
         * specified in the [Food] model.
         *
         * The micronutrients are: [Food.sodium], [Food.potassium] and
         * [Food.cholesterol]
         */
        return NutrientViewHolder(view, nutrients.takeLast(MICRONUTRIENTS))
    }

    override fun onBindViewHolder(holder: NutrientViewHolder, position: Int) {
        val nutrient = nutrients[position]
        // The click listener is only set on the first item, because that is the
        // position where the servingSize is if the list is from an individual food
        if (position == 0 && isListFromIndividualFood) {
            holder.itemView.setOnClickListener(clickListener)
        }
        holder.bind(nutrient)
    }
}
