package com.example.elegantcalorietracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.MainActivity
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.databinding.FragmentFoodBinding
import com.example.elegantcalorietracker.ui.ModType
import com.example.elegantcalorietracker.ui.TrackerViewModel

private const val TAG = "FoodFragment"

class FoodFragment : Fragment() {

    private lateinit var binding: FragmentFoodBinding

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: TrackerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFoodBinding.inflate(inflater, container, false)
        (activity as MainActivity).lockDrawerSlide(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentFood = sharedViewModel.selectedFood
        if (fragmentFood == null) {
            this@FoodFragment.findNavController()
                .navigate(R.id.action_foodFragment_to_trackerFragment)
        }

        binding.apply {
            // Specify the fragment as the lifecycle owner
            lifecycleOwner = viewLifecycleOwner
            //
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

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).lockDrawerSlide(false)
    }
}
