package com.example.elegantcalorietracker.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.elegantcalorietracker.R
import mehdi.sakout.aboutpage.AboutPage

/** GitHub username to be used in the *About Page* */
private const val GITHUB_USERNAME = "hahmraro"

/**
 * Shows an *About Page* using the [AboutPage] library
 */
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createAboutPage(requireContext())
    }

    /**
     * Creates the *About Page* with the app's description, its icon, and a
     * link to my GitHub account
     */
    private fun createAboutPage(context: Context): View? =
        AboutPage(context)
            .isRTL(false)
            .setDescription(resources.getString(R.string.about_description))
            .setImage(R.mipmap.ic_launcher)
            .addGitHub(GITHUB_USERNAME, resources.getString(R.string.github_page))
            .create()
}
