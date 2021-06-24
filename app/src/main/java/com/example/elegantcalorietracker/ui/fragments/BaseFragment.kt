package com.example.elegantcalorietracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.example.elegantcalorietracker.MainActivity
import com.example.elegantcalorietracker.ui.TrackerViewModel

typealias ApplyTo<T> = T.() -> Unit
typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<BindingType : ViewBinding>(
    private val bindingInflater: Inflate<BindingType>,
    private val lockDrawer: Boolean = false,
    private val hasOptionsMenu: Boolean = false,
    private val topLevelAndCanHaveUpButton: Boolean = false
) : Fragment() {
    // Binding
    protected lateinit var binding: BindingType
    protected var container: ViewGroup? = null

    // ViewModel
    protected val sharedViewModel: TrackerViewModel by activityViewModels()

    // 
    private var upButtonNeeded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (topLevelAndCanHaveUpButton) {
            upButtonNeeded =
                arguments?.getBoolean("upButtonNeeded") ?: false
            if (upButtonNeeded) {
                (activity as MainActivity).useUpButton()
            }
        }
        this.container = container
        binding = bindingInflater.invoke(inflater, container, false)
        setHasOptionsMenu(hasOptionsMenu)
        (activity as MainActivity).lockDrawerSlide(lockDrawer)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply(applyBinding(view))
    }

    override fun onDestroy() {
        if (upButtonNeeded) {
            (activity as MainActivity).useHamburgerButton()
        }
        if (lockDrawer) {
            (activity as MainActivity).lockDrawerSlide(false)
        }
        super.onDestroy()
    }

    protected open fun applyBinding(v: View): ApplyTo<BindingType> = {}
}
