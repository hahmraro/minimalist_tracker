package com.example.elegantcalorietracker.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.data.model.ListType
import com.example.elegantcalorietracker.databinding.FragmentSearchBinding
import com.example.elegantcalorietracker.ui.ModType
import com.example.elegantcalorietracker.ui.adapters.FoodListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*

class SearchFragment :
    BaseFragment<FragmentSearchBinding>(
        FragmentSearchBinding::inflate,
        lockDrawer = true,
        hasOptionsMenu = true
    ) {

    private var searchQuery = MutableLiveData<String?>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedViewModel.modType = ModType.ADD
        super.onViewCreated(view, savedInstanceState)
    }

    override fun applyBinding(v: View): ApplyTo<FragmentSearchBinding> = {
        historyList.apply {
            sharedViewModel.getList(ListType.HISTORY)
                .observe(viewLifecycleOwner) { list ->
                    val adapter = this.adapter as FoodListAdapter
                    adapter.submitList(list)
                }
            adapter = FoodListAdapter(clickListener, longClickListener)
        }
        searchQuery.observe(viewLifecycleOwner) { query ->
            if (query != null) {
                // Set loading state
                searchLoading.show()
                historyList.alpha = 0.5f
                lifecycleScope.launch {
                    try {
                        // If food is found, navigate back
                        sharedViewModel.searchFoodsWithQuery(query)
                        this@SearchFragment.findNavController()
                            .navigate(R.id.action_searchFragment_to_trackerFragment)
                    } catch (e: Exception) {
                        // Else reset loading state and have snackbar show error
                        Snackbar.make(
                            requireView(),
                            e.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                        searchLoading.hide()
                        historyList.alpha = 1.0f
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_bar_menu, menu)
        val searchIcon = menu.findItem(R.id.search_icon)
        val searchView = searchIcon.actionView as SearchView
        searchView.apply {
            queryHint = "Ex: 100g of Cheddar"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchQuery.value = query
                    searchIcon.collapseActionView()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_button -> {
                sharedViewModel.clearHistory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Listener of clicks made to the food items of history list
    private val clickListener: (Food) -> (Unit) = { food ->
        sharedViewModel.apply {
            selectedFood = food
        }
        // Set the label of FoodFragment to the selected foods name
        val argument = bundleOf(
            "foodName" to food.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        )
        this@SearchFragment.findNavController()
            .navigate(R.id.action_searchFragment_to_foodFragment, argument)
    }

    // Listener of long clicks made to the food items of history list
    private val longClickListener: (PopupMenu, Food, View) -> (Boolean) =
        { menu, food, _ ->
            menu.inflate(R.menu.search_options_menu)
            menu.setOnMenuItemClickListener {
                sharedViewModel.deleteFood(food)
                true
            }
            true
        }
}
