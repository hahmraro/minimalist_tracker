package com.example.elegantcalorietracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.elegantcalorietracker.R
import mehdi.sakout.aboutpage.AboutPage

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return AboutPage(requireContext())
            .isRTL(false)
            .setDescription(
                "Minimalist calorie tracker with natural language API"
            )
            .setImage(R.drawable.ic_error)
            .addGitHub("hahmraro", "GitHub Page")
            .create()
    }
}
