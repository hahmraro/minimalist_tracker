package com.example.elegantcalorietracker.ui.fragments

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.databinding.FragmentSearchBinding
import com.example.elegantcalorietracker.ui.ModType
import com.example.elegantcalorietracker.ui.TrackerViewModel
import com.example.elegantcalorietracker.ui.adapters.FoodListAdapter
import com.google.android.material.snackbar.Snackbar

private const val TAG = "SearchFragment"

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: TrackerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.modType = ModType.ADD

        binding.apply {
            // Specify the fragment as the lifecycle owner
            lifecycleOwner = viewLifecycleOwner
            // Assign the TrackerViewModel to the binding viewModel property
            viewModel = sharedViewModel
            //
            historyList.adapter =
                FoodListAdapter(clickListener, longClickListener)
            //
            searchField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    val query = searchField.text.toString()
                    searchField.text.clear()
                    searchField.isEnabled = false
                    val errorMessage = sharedViewModel.getFoods(query)
                    if (errorMessage == null) {
                        this@SearchFragment.findNavController()
                            .navigate(R.id.action_searchFragment_to_trackerFragment)
                    } else {
                        Snackbar.make(
                            view, errorMessage,
                            Snackbar.LENGTH_LONG
                        ).show()
                        searchField.isEnabled = true
                    }
                    true
                } else {
                    false
                }
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

    private val clickListener: (Food) -> (Unit) = {
        sharedViewModel.apply {
            selectedFood = it
        }
        this@SearchFragment.findNavController()
            .navigate(R.id.action_searchFragment_to_foodFragment)
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
