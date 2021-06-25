package com.example.elegantcalorietracker.ui.fragments

import android.app.AlertDialog
import android.util.Log
import android.view.*
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.databinding.FoodDialogBinding
import com.example.elegantcalorietracker.databinding.FragmentFoodBinding
import com.example.elegantcalorietracker.ui.ModType
import com.example.elegantcalorietracker.ui.TrackerViewModel
import com.example.elegantcalorietracker.ui.widgets.FoodListView
import com.example.elegantcalorietracker.utils.DialogClickListener

private const val TAG = "FoodFragment"

/**
 * The screen that shows when the user selects a food item from [FoodListView]
 *
 * Displays the complete nutritional information for the food, the option to
 * change its serving size, and a button that calls [TrackerViewModel] to
 * either save or edit it
 */
class FoodFragment : BaseFragment<FragmentFoodBinding>(
    FragmentFoodBinding::inflate,
    lockDrawer = true,
    hasOptionsMenu = true
) {

    /**
     * The [Food] linked to the selected food item
     */
    private lateinit var selectedFood: Food

    override fun applyBinding(v: View): ApplyTo<FragmentFoodBinding> = {
        // Assigns the selected food and the default serving size editText value
        selectedFood = sharedViewModel.selectedFood

        // Set up the NutritionView
        val nutrients = makeNutrients(selectedFood)
        foodNutritionLl.makeAdapter(nutrients, true, showDialog())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.food_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done_button -> {
                setAddButtonClickListener(binding, sharedViewModel.modType)
                true
            }
            else -> super.onOptionsItemSelected(item)
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
        try {
            val newServingSize = binding.foodNutritionLl.getServingSize()
            if (modType == ModType.ADD)
                sharedViewModel.getFood(selectedFood, newServingSize)
            else {
                sharedViewModel.editFood(selectedFood, newServingSize)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        } finally {
            this@FoodFragment.findNavController()
                .navigate(R.id.action_foodFragment_to_trackerFragment)
        }
    }

    private fun makeNutrients(food: Food): MutableList<Double> {
        val nutrients = mutableListOf<Double>()
        nutrients.add(food.servingSize.toDouble())
        nutrients.addAll(food.getNutrients())
        return nutrients
    }

    private fun showDialog(): DialogClickListener = {
        val servingEditText = FoodDialogBinding
            .inflate(LayoutInflater.from(requireContext())).textField
        servingEditText.apply {
            hint = "%.1f".format(selectedFood.servingSize.toDouble())
            requestFocus()
        }
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Serving Size")
            .setView(servingEditText.rootView)
            .setPositiveButton("Save") { _, _ ->
                val newServingSize = servingEditText.text.toString().toDouble()
                val newFood = selectedFood.copy().edit(newServingSize)
                val nutrients = makeNutrients(newFood)
                binding.foodNutritionLl.makeAdapter(
                    nutrients, true,
                    showDialog(), true
                )
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.window?.clearFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
        )
        dialog.window?.setSoftInputMode(
            WindowManager.LayoutParams
                .SOFT_INPUT_STATE_VISIBLE
        )
        dialog.show()
    }
}
