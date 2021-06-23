package com.example.elegantcalorietracker.ui.fragments

import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.databinding.FragmentFoodBinding
import com.example.elegantcalorietracker.ui.ModType

private const val TAG = "FoodFragment"

class FoodFragment : BaseFragment<FragmentFoodBinding>(
    FragmentFoodBinding::inflate,
    lockDrawer = true
) {

    override fun applyBinding(v: View): ApplyTo<FragmentFoodBinding> = {
        //
        val fragmentFood = sharedViewModel.selectedFood
        if (fragmentFood == null) {
            this@FoodFragment.findNavController()
                .navigate(R.id.action_foodFragment_to_trackerFragment)
            onDestroy()
        } else {
            servingEdit.setText(fragmentFood.servingSize)
            foodNutritionLl.apply {
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
            //
            if (sharedViewModel.modType == ModType.ADD) {
                addButton.text = "Add Food"
                addButton.setOnClickListener {
                    val newServingSize = servingEdit.text.toString().toDouble()
                    sharedViewModel.getFood(
                        fragmentFood,
                        newServingSize
                    )
                    this@FoodFragment.findNavController()
                        .navigate(R.id.action_foodFragment_to_trackerFragment)
                }
            } else {
                addButton.text = "Edit Food"
                addButton.setOnClickListener {
                    val newServingSize = servingEdit.text.toString().toDouble()
                    sharedViewModel.editFood(fragmentFood, newServingSize)
                    this@FoodFragment.findNavController()
                        .navigate(R.id.action_foodFragment_to_trackerFragment)
                }
            }
        }
    }
}
