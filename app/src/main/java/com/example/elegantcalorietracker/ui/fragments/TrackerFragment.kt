package com.example.elegantcalorietracker.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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

class TrackerFragment : BaseFragment<FragmentTrackerBinding>(
    FragmentTrackerBinding::inflate,
    hasOptionsMenu = true
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedViewModel.modType = ModType.EDIT
        // Refresh the saved goal because it may have been changed through 
        // SettingsFragment
        sharedViewModel.refreshCalorieGoal()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun applyBinding(v: View): ApplyTo<FragmentTrackerBinding> = {
        counter.apply {
            setOnClickListener { navigateToNutrients() }
            sharedViewModel.caloriesGoal.observe(viewLifecycleOwner) { goal ->
                setCaloriesGoal(goal)
                setMoreClickListener(goal.toString()) { _, _, editText ->
                    val newGoal = editText.text.toString().toInt()
                    sharedViewModel.setNewCalorieGoal(newGoal)
                }
            }
            sharedViewModel.calories.observe(viewLifecycleOwner) { calories ->
                setCalories(calories)
            }
            sharedViewModel.caloriesRemaining.observe(viewLifecycleOwner) { remaining ->
                setCaloriesRemaining(remaining)
            }
        }
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
                sharedViewModel.clearNonHistoryFoods()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Listener to clicks on food items of the food lists
    private fun clickListener(listType: ListType): (Food) -> (Unit) = { food ->
        sharedViewModel.apply {
            selectedFood = food
            setSearchListType(listType)
        }
        // Set the label of FoodFragment to the selected foods name
        val argument = bundleOf(
            "foodName" to food.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
        )
        this@TrackerFragment.findNavController()
            .navigate(R.id.action_trackerFragment_to_foodFragment, argument)
    }

    // Listener to long clicks on food items of the food lists
    private val longClickListener: (PopupMenu, Food, View) -> (Boolean) =
        { menu, food, view ->
            menu.inflate(R.menu.tracker_options_menu)
            menu.setOnMenuItemClickListener { item ->
                // First menu that opens when long click
                when (item.itemId) {
                    R.id.remove_from_list -> sharedViewModel.deleteFood(food)
                    R.id.move_to -> {
                        val moveMenu = PopupMenu(view.context, view)
                        moveMenu.inflate(R.menu.move_to_menu)
                        moveMenu.setOnMenuItemClickListener { moveItem ->
                            // Second menu that opens if user selects the 
                            // "Move to" option
                            when (moveItem.itemId) {
                                R.id.move_to_breakfast ->
                                    sharedViewModel.moveFoodToAnotherList(
                                        food,
                                        ListType.BREAKFAST
                                    )
                                R.id.move_to_lunch ->
                                    sharedViewModel.moveFoodToAnotherList(
                                        food,
                                        ListType.LUNCH
                                    )
                                R.id.move_to_dinner ->
                                    sharedViewModel.moveFoodToAnotherList(
                                        food,
                                        ListType.DINNER
                                    )
                                R.id.move_to_snacks ->
                                    sharedViewModel.moveFoodToAnotherList(
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
            val listTitle = listType.toString().lowercase().replaceFirstChar { it.titlecase() }
            setListTitle(listTitle)
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
        // Since NutrientFragment has the constructor parameter 
        // "topLevelAndCanHaveUpButton" set to true, passing the argument 
        // below to it will force it to use the "Up" button instead of the 
        // navigation drawer. This is desirable because the navigation drawer is
        // only to be shown if the Fragment is navigated to through it, which
        // is not the case here, because this function is called through a 
        // normal button, and not the navigation drawer.
        val argument = bundleOf("upButtonNeeded" to true)
        this@TrackerFragment
            .findNavController()
            .navigate(
                R.id.action_trackerFragment_to_nutrientFragment,
                argument
            )
    }

    private fun navigateToSearch(listType: ListType) {
        sharedViewModel.setSearchListType(listType)
        findNavController()
            .navigate(R.id.action_trackerFragment_to_searchFragment)
    }
}
