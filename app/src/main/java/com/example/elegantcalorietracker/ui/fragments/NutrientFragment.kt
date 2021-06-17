package com.example.elegantcalorietracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.elegantcalorietracker.databinding.FragmentNutrientBinding
import com.example.elegantcalorietracker.ui.TrackerViewModel

private const val TAG = "NutrientFragment"

class NutrientFragment : Fragment() {

    private lateinit var binding: FragmentNutrientBinding

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: TrackerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNutrientBinding.inflate(inflater, container, false)
        activity?.actionBar?.setHomeButtonEnabled(true)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.food = sharedViewModel.getDailyNutrition()
    }
}
