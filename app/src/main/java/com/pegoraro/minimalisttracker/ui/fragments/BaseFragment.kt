package com.pegoraro.minimalisttracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.pegoraro.minimalisttracker.MainActivity
import com.pegoraro.minimalisttracker.ui.TrackerViewModel

/**
 * Alias for the lambda passed to T.apply()
 */
typealias ApplyTo<T> = T.() -> Unit

/**
 * Alias for the inflater lambda
 */
typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

/**
 * Generic [Fragment] to be implemented by the app's various fragments
 *
 * @param BindingType The ViewBinding of the [Fragment]
 * @param bindingInflater Inflater of [BindingType]. Needed because [BaseFragment]
 * doesn't have access to the ViewBinding inflate method
 * @param lockDrawer a [Boolean] that determines if the drawer should be disabled
 * for the [Fragment]
 * @param hasOptionsMenu a [Boolean] that determines if the [Fragment] will
 * like to participate in populating the options menu
 * @param topLevelAndCanHaveUpButton a [Boolean] that communicates to whether or
 * not the [Fragment] is a top level destination of the navigation but would
 * still like to show an *Up* button when the means to navigating to it don't
 * involve the navigation drawer
 */
abstract class BaseFragment<BindingType : ViewBinding>(
    private val bindingInflater: Inflate<BindingType>,
    private val lockDrawer: Boolean = false,
    private val hasOptionsMenu: Boolean = false,
    private val topLevelAndCanHaveUpButton: Boolean = false
) : Fragment() {
    // Binding and container
    protected lateinit var binding: BindingType
    private var container: ViewGroup? = null

    // ViewModel
    protected val sharedViewModel: TrackerViewModel by activityViewModels()

    /**
     * Variable that determines if the [Fragment] needs to forcefully show the
     * *Up* button or not
     */
    private var upButtonNeeded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Modifies upButtonNeeded depending on whether or not the fragment 
        // needs to forcefully use an *Up* button
        if (topLevelAndCanHaveUpButton) {
            upButtonNeeded = arguments?.getBoolean("upButtonNeeded") ?: false
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
        // Revert changes made to the *Up* button, if needed
        if (upButtonNeeded) {
            (activity as MainActivity).useHamburgerButton()
        }
        // Revert changes made to the navigation drawer, if needed
        if (lockDrawer) {
            (activity as MainActivity).lockDrawerSlide(false)
        }
        super.onDestroy()
    }

    /**
     * Sets the lambda that will be passed to the [binding].apply method inside
     * [onViewCreated]
     *
     * @param v The [View] from the [onViewCreated] method
     * @return an [ApplyTo] lambda of [BindingType]
     */
    protected open fun applyBinding(v: View): ApplyTo<BindingType> = {}
}
