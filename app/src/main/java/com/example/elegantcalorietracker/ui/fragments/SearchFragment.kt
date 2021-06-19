package com.example.elegantcalorietracker.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.elegantcalorietracker.MainActivity
import com.example.elegantcalorietracker.R
import com.example.elegantcalorietracker.data.model.Food
import com.example.elegantcalorietracker.databinding.FragmentSearchBinding
import com.example.elegantcalorietracker.ui.ModType
import com.example.elegantcalorietracker.ui.TrackerViewModel
import com.example.elegantcalorietracker.ui.adapters.FoodListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

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
        (activity as MainActivity).lockDrawerSlide(true)
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
                                view,
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
