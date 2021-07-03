package com.example.elegantcalorietracker.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.example.elegantcalorietracker.R
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

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

    // Had to create this class and function to be able to use the AboutPage "addGitHub"
    // function while being able to change the tint color of the icon. The alternative would be to
    // include a GitHub icon drawable in the project. The less resources the better.
    class CustomAboutPage(context: Context) : AboutPage(context) {
        fun addGitHub(id: String?, title: String?, @ColorRes tintColor: Int? = null): AboutPage {
            val gitHubElement = Element()
            gitHubElement.title = title
            gitHubElement.iconDrawable = R.drawable.about_icon_github
            gitHubElement.iconTint = tintColor ?: R.color.about_github_color
            gitHubElement.value = id

            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse(String.format("https://github.com/%s", id))

            gitHubElement.intent = intent
            addItem(gitHubElement)

            return this
        }
    }

    /**
     * Creates the *About Page* with the app's description, its icon, and a
     * link to my GitHub account. The github icon color is different depending on whether or not
     * dark mode is on
     */
    private fun createAboutPage(context: Context): View? {
        val darkThemeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isDarkTheme = darkThemeFlags == Configuration.UI_MODE_NIGHT_YES
        val tintColor = if (isDarkTheme) R.color.secondaryTextColor else R.color.primaryColor
        return CustomAboutPage(context)
            .addGitHub(GITHUB_USERNAME, resources.getString(R.string.github_page), tintColor)
            .isRTL(false)
            .setDescription(resources.getString(R.string.about_description))
            .setImage(R.mipmap.ic_launcher)
            .create()
    }
}
