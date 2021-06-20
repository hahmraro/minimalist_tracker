package com.example.elegantcalorietracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.elegantcalorietracker.MainActivity
import com.example.elegantcalorietracker.ui.TrackerViewModel

abstract class BaseFragment<BindingType : ViewDataBinding>(
    @LayoutRes private val layoutId: Int,
    private val lockDrawer: Boolean = false,
    private val hasOptionsMenu: Boolean = false,
) : Fragment() {
    // Binding
    private lateinit var binding: BindingType

    // ViewModel
    private val sharedViewModel: TrackerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        setHasOptionsMenu(hasOptionsMenu)
        (activity as MainActivity).lockDrawerSlide(lockDrawer)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBinding(binding)
    }

    private fun applyBinding(binding: ViewDataBinding) {
    }
}
