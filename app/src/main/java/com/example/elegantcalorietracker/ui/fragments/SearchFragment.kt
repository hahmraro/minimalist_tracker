package com.example.elegantcalorietracker.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.MainActivity
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.databinding.FragmentSearchBinding
import com.example.elegantcalorietracker.ui.ModType
import com.example.elegantcalorietracker.ui.adapters.FoodListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "SearchFragment"

class SearchFragment :
    BaseFragment<FragmentSearchBinding>(
        R.layout.fragment_search,
        lockDrawer = true,
        hasOptionsMenu = true
    ) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedViewModel.modType = ModType.ADD
        super.onViewCreated(view, savedInstanceState)
    }

    override fun applyBinding(v: View): ApplyTo<FragmentSearchBinding> = {
        // Specify the fragment as the lifecycle owner
        lifecycleOwner = viewLifecycleOwner
        // Assign the TrackerViewModel to the binding viewModel property
        viewModel = sharedViewModel
        //
        historyList.adapter =
            FoodListAdapter(clickListener, longClickListener)
        //
        searchField.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) v.hideKeyboard()
        }

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                searchLoading.show()
                bodySearch.alpha = 0.5f
                lifecycleScope.launch {
                    val query = searchField.text.toString()
                    searchField.text.clear()
                    searchField.isEnabled = false
                    try {
                        sharedViewModel.getFoods(query)
                        this@SearchFragment.findNavController()
                            .navigate(R.id.action_searchFragment_to_trackerFragment)
                    } catch (e: Exception) {
                        Snackbar.make(
                            v,
                            e.toString(),
                            Snackbar.LENGTH_LONG
                        ).show()
                        searchField.isEnabled = true
                        searchLoading.hide()
                        bodySearch.alpha = 1.0f
                    }
                }
                true
            } else {
                false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.bar_menu, menu)
        menu.findItem(R.id.bar_button).isVisible = false
        menu.findItem(R.id.clear_button).title = "Clear history foods"
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

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).lockDrawerSlide(false)
    }

    private fun View.hideKeyboard() {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private val clickListener: (Food) -> (Unit) = { food ->
        sharedViewModel.apply {
            selectedFood = food
        }
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
