package com.example.elegantcalorietracker

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView

private const val TAG = "MainActivity"

class MainActivity :
    AppCompatActivity(R.layout.activity_main),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout

    // If true, disable drawer and enable back/up button
    private var isUpButton = false

    // Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve NavController from NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)

        drawerLayout = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.trackerFragment,
                R.id.nutrientFragment,
                R.id.settingsFragment
            ),
            drawerLayout
        )

        // Set up action bar for use with NavController
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // Disable drawer and enable the up button
    fun useUpButton() {
        supportActionBar?.setHomeAsUpIndicator(
            androidx.appcompat.R.drawable.abc_ic_ab_back_material
        )
        lockDrawerSlide(true)
        isUpButton = true
    }

    fun lockDrawerSlide(boolean: Boolean) {
        if (boolean) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    // Enable the drawer and disable up button
    fun useHamburgerButton() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        isUpButton = false
    }

    // If isUpButton is true, and the home button is clicked, navigate up and
    // enable drawer again, if false, just normal drawer behaviour
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            when {
                isUpButton -> {
                    navController.navigateUp()
                }
                drawerLayout.isDrawerOpen(GravityCompat.START) -> {
                    drawerLayout.closeDrawers()
                }
                else -> drawerLayout.openDrawer(GravityCompat.START)
            }
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // I'm unsure to whether or not to close the drawer after navigating
        // Uncomment the line below to close the drawer after navigating
        // drawerLayout.closeDrawer(GravityCompat.START, true)
        return item.onNavDestinationSelected(navController) ||
            super.onOptionsItemSelected(item)
    }

    /**
     * Handle navigation when the user chooses Up from the action bar.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
