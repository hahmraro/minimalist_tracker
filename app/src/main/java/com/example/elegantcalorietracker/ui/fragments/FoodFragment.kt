package com.example.elegantcalorietracker.ui.fragments

import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.databinding.FragmentFoodBinding
import com.example.elegantcalorietracker.ui.ModType
import com.example.elegantcalorietracker.ui.TrackerViewModel
import com.example.elegantcalorietracker.ui.widgets.FoodListView

/**
 * The screen that shows when the user selects a food item from [FoodListView]
 *
 * Displays the complete nutritional information for the food, the option to
 * change its serving size, and a button that calls [TrackerViewModel] to
 * either save or edit it
 */
class FoodFragment : BaseFragment<FragmentFoodBinding>(
    FragmentFoodBinding::inflate,
    lockDrawer = true
) {

    /**
     * The [Food] linked to the selected food item
     */
    private lateinit var selectedFood: Food

    override fun applyBinding(v: View): ApplyTo<FragmentFoodBinding> = {
        // Assigns the selected food and the default serving size editText value
        selectedFood = sharedViewModel.selectedFood
        servingEdit.setText(selectedFood.servingSize)

        // Set up the NutritionView
        val nutrients = selectedFood.getNutrients()
        foodNutritionLl.setNutrients(nutrients)

        // Set up the button text and click listener
        if (sharedViewModel.modType == ModType.ADD) {
            addButton.text = getString(R.string.add_food)
            setAddButtonClickListener(this, ModType.ADD)
        } else {
            addButton.text = getString(R.string.edit_food)
            setAddButtonClickListener(this, ModType.EDIT)
        }
    }

    // Helper Methods

    /**
     * Set the [FragmentFoodBinding] *add button* click listener
     */
    private fun setAddButtonClickListener(
        binding: FragmentFoodBinding,
        modType: ModType
    ) {
        binding.addButton.setOnClickListener {
            val newServingSize = binding.servingEdit.text.toString().toDouble()
            if (modType == ModType.ADD)
                sharedViewModel.getFood(selectedFood, newServingSize)
            else {
                sharedViewModel.editFood(selectedFood, newServingSize)
            }
            this@FoodFragment.findNavController()
                .navigate(R.id.action_foodFragment_to_trackerFragment)
        }
    }
}
