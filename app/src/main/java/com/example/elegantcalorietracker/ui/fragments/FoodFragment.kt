package com.example.elegantcalorietracker.ui.fragments

import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.databinding.FragmentFoodBinding
import com.example.elegantcalorietracker.ui.ModType

private const val TAG = "FoodFragment"

class FoodFragment : BaseFragment<FragmentFoodBinding>(
    R.layout.fragment_food,
    lockDrawer = true
) {

    override fun applyBinding(v: View): ApplyTo<FragmentFoodBinding> = {
        // Specify the fragment as the lifecycle owner
        lifecycleOwner = viewLifecycleOwner
        //
        val fragmentFood = sharedViewModel.selectedFood
        if (fragmentFood == null) {
            this@FoodFragment.findNavController()
                .navigate(R.id.action_foodFragment_to_trackerFragment)
            onDestroy()
        }
        food = fragmentFood
        //
        if (sharedViewModel.modType == ModType.ADD) {
            addButton.text = "Add Food"
            addButton.setOnClickListener {
                val newServingSize = servingEdit.text.toString().toDouble()
                sharedViewModel.getFood(
                    fragmentFood!!,
                    newServingSize
                )
                this@FoodFragment.findNavController()
                    .navigate(R.id.action_foodFragment_to_trackerFragment)
            }
        } else {
            addButton.text = "Edit Food"
            addButton.setOnClickListener {
                val newServingSize = servingEdit.text.toString().toDouble()
                sharedViewModel.editFood(fragmentFood!!, newServingSize)
                this@FoodFragment.findNavController()
                    .navigate(R.id.action_foodFragment_to_trackerFragment)
            }
        }
    }
}
