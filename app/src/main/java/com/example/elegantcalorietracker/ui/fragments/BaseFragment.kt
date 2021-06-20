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

typealias ApplyTo<T> = T.() -> Unit

abstract class BaseFragment<BindingType : ViewDataBinding>(
    @LayoutRes private val layoutId: Int,
    private val lockDrawer: Boolean = false,
    private val hasOptionsMenu: Boolean = false,
    private val isTopLevelAndNeedUpButton: Boolean = false
) : Fragment() {
    // Binding
    protected lateinit var binding: BindingType

    // ViewModel
    protected val sharedViewModel: TrackerViewModel by activityViewModels()

    // 
    private var upButtonNeeded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (isTopLevelAndNeedUpButton) {
            upButtonNeeded =
                arguments?.getBoolean("upButtonNeeded") ?: false
            if (upButtonNeeded) {
                (activity as MainActivity).useUpButton()
            }
        }
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        setHasOptionsMenu(hasOptionsMenu)
        (activity as MainActivity).lockDrawerSlide(lockDrawer)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply(applyBinding())
    }

    override fun onDestroy() {
        if (upButtonNeeded) {
            (activity as MainActivity).useHamburgerButton()
        }
        super.onDestroy()
    }

    protected open fun applyBinding(): ApplyTo<BindingType> = {}
}
