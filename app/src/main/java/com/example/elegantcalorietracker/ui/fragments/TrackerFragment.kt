package com.example.elegantcalorietracker.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.data.model.ListType
import com.example.elegantcalorietracker.databinding.FragmentTrackerBinding
import com.example.elegantcalorietracker.ui.ModType
import com.example.elegantcalorietracker.ui.TrackerViewModel
import com.example.elegantcalorietracker.ui.adapters.FoodListAdapter

private const val TAG = "TrackerFragment"

class TrackerFragment : Fragment() {

    private lateinit var binding: FragmentTrackerBinding

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: TrackerViewModel by activityViewModels()
    //

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackerBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.modType = ModType.EDIT

        binding.apply {
            // Specify the fragment as the lifecycle owner
            lifecycleOwner = viewLifecycleOwner
            // Assign the TrackerViewModel to the binding viewModel property
            viewModel = sharedViewModel
            //
            breakfastList.adapter =
                FoodListAdapter(
                    clickListener(ListType.BREAKFAST),
                    longClickListener
                )
            lunchList.adapter =
                FoodListAdapter(
                    clickListener(ListType.LUNCH),
                    longClickListener
                )
            dinnerList.adapter =
                FoodListAdapter(
                    clickListener(ListType.DINNER),
                    longClickListener
                )
            snacksList.adapter =
                FoodListAdapter(
                    clickListener(ListType.SNACKS),
                    longClickListener
                )
            //
            caloriesText.setOnClickListener {
                navigateToNutrients()
            }
            caloriesValue.setOnClickListener {
                navigateToNutrients()
            }
            //
            breakfastButton.setOnClickListener {
                navigateToSearch(ListType.BREAKFAST)
            }
            lunchButton.setOnClickListener {
                navigateToSearch(ListType.LUNCH)
            }
            dinnerButton.setOnClickListener {
                navigateToSearch(ListType.DINNER)
            }
            snacksButton.setOnClickListener {
                navigateToSearch(ListType.SNACKS)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bar_button -> {
                navigateToNutrients()
                true
            }
            R.id.clear_button -> {
                sharedViewModel.clearFoods()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clickListener(listType: ListType): (Food) -> (Unit) = {
        sharedViewModel.apply {
            selectedFood = it
            this.listType = listType.ordinal
        }
        this@TrackerFragment.findNavController()
            .navigate(R.id.action_trackerFragment_to_foodFragment)
    }

    private val longClickListener: (PopupMenu, Food, View) -> (Boolean) =
        { menu, food, view ->
            menu.inflate(R.menu.tracker_options_menu)
            menu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.remove_from_list -> sharedViewModel.deleteFood(food)
                    R.id.move_to -> {
                        val moveMenu = PopupMenu(view.context, view)
                        moveMenu.inflate(R.menu.move_to_menu)
                        moveMenu.setOnMenuItemClickListener { moveItem ->
                            when (moveItem.itemId) {
                                R.id.move_to_breakfast ->
                                    sharedViewModel.moveFood(
                                        food,
                                        ListType.BREAKFAST
                                    )
                                R.id.move_to_lunch ->
                                    sharedViewModel.moveFood(
                                        food,
                                        ListType.LUNCH
                                    )
                                R.id.move_to_dinner ->
                                    sharedViewModel.moveFood(
                                        food,
                                        ListType.DINNER
                                    )
                                R.id.move_to_snacks ->
                                    sharedViewModel.moveFood(
                                        food,
                                        ListType.SNACKS
                                    )
                            }
                            true
                        }
                        moveMenu.show()
                    }
                }
                true
            }
            true
        }

    private fun navigateToNutrients() {
        val argument = bundleOf("upButtonNeeded" to true)
        this@TrackerFragment
            .findNavController()
            .navigate(
                R.id.action_trackerFragment_to_nutrientFragment,
                argument
            )
    }

    private fun navigateToSearch(listType: ListType) {
        sharedViewModel.setSearchMeal(listType.ordinal)
        findNavController()
            .navigate(R.id.action_trackerFragment_to_searchFragment)
    }
}
