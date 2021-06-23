package com.example.elegantcalorietracker.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.data.model.ListType
import com.example.elegantcalorietracker.databinding.FragmentTrackerBinding
import com.example.elegantcalorietracker.ui.ModType
import com.example.elegantcalorietracker.ui.adapters.FoodListAdapter
import com.example.elegantcalorietracker.ui.widgets.FoodListView
import java.util.*

private const val TAG = "TrackerFragment"

class TrackerFragment : BaseFragment<FragmentTrackerBinding>(
    FragmentTrackerBinding::inflate,
    hasOptionsMenu = true
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedViewModel.modType = ModType.EDIT
        super.onViewCreated(view, savedInstanceState)
    }

    override fun applyBinding(v: View): ApplyTo<FragmentTrackerBinding> = {
        // Assign the TrackerViewModel to the binding viewModel property
        counter.apply {
            setOnClickListener { navigateToNutrients() }
            val caloriesGoal = sharedViewModel.caloriesGoal
            setCaloriesGoal(caloriesGoal)
            sharedViewModel.calories.observe(viewLifecycleOwner) { calories ->
                setCalories(calories)
                setCaloriesRemaining(caloriesGoal - calories)
            }
        }
        //
        applyFoodListView(breakfast, ListType.BREAKFAST)
        applyFoodListView(lunch, ListType.LUNCH)
        applyFoodListView(dinner, ListType.DINNER)
        applyFoodListView(snacks, ListType.SNACKS)
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

    private fun clickListener(listType: ListType): (Food) -> (Unit) = { food ->
        sharedViewModel.apply {
            selectedFood = food
            this.listType = listType.ordinal
        }
        val argument = bundleOf(
            "foodName" to food.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        )
        this@TrackerFragment.findNavController()
            .navigate(R.id.action_trackerFragment_to_foodFragment, argument)
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

    private fun applyFoodListView(
        foodListView: FoodListView,
        listType: ListType
    ) {
        foodListView.apply {
            setButtonClickListener {
                navigateToSearch(listType)
            }
            val listTitle = listType.toString().lowercase().replaceFirstChar {
                it.titlecase()
            }
            setListText(listTitle)
            sharedViewModel.getList(listType)
                .observe(viewLifecycleOwner) { list ->
                    setListData(list)
                }
            setAdapter(
                FoodListAdapter(
                    clickListener(listType),
                    longClickListener
                )
            )
        }
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
