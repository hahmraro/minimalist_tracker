package com.example.elegantcalorietracker.ui.fragments

import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.databinding.FragmentFoodBinding
import com.example.elegantcalorietracker.ui.ModType
import com.example.elegantcalorietracker.ui.widgets.NutritionView

private const val TAG = "FoodFragment"

class FoodFragment : BaseFragment<FragmentFoodBinding>(
    FragmentFoodBinding::inflate,
    lockDrawer = true
) {

    private lateinit var selectedFood: Food

    override fun applyBinding(v: View): ApplyTo<FragmentFoodBinding> = {
        //
        selectedFood = sharedViewModel.selectedFood
        servingEdit.setText(selectedFood.servingSize)
        foodNutritionLl.apply(applyFoodNutrition(selectedFood))
        //
        if (sharedViewModel.modType == ModType.ADD) {
            addAction(this)
        } else {
            editAction(this)
        }
    }

    private fun addAction(binding: FragmentFoodBinding) {
        binding.addButton.text = "Add Food"
        binding.addButton.setOnClickListener {
            val newServingSize = binding.servingEdit.text.toString().toDouble()
            sharedViewModel.getFood(
                selectedFood,
                newServingSize
            )
            this@FoodFragment.findNavController()
                .navigate(R.id.action_foodFragment_to_trackerFragment)
        }
    }

    private fun editAction(binding: FragmentFoodBinding) {
        binding.addButton.text = "Edit Food"
        binding.addButton.setOnClickListener {
            val newServingSize = binding.servingEdit.text.toString().toDouble()
            sharedViewModel.editFood(selectedFood, newServingSize)
            this@FoodFragment.findNavController()
                .navigate(R.id.action_foodFragment_to_trackerFragment)
        }
    }

    private fun applyFoodNutrition(fragmentFood: Food): ApplyTo<NutritionView> =
        {
            setCalories(fragmentFood.calories)
            setFiber(fragmentFood.fiber)
            setSugar(fragmentFood.sugar)
            setCarbs(fragmentFood.totalCarbs)
            setSaturatedFat(fragmentFood.saturatedFat)
            setFat(fragmentFood.totalFat)
            setProtein(fragmentFood.protein)
            setSodium(fragmentFood.sodium)
            setPotassium(fragmentFood.potassium)
            setCholesterol(fragmentFood.cholesterol)
        }
}
