package com.ludovic.vimont.nasaapod.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.ActivityMainBinding
import com.ludovic.vimont.nasaapod.ext.hasPostNotificationPermission
import com.ludovic.vimont.nasaapod.screens.permissions.NotificationPermissionDialogFragment
import com.ludovic.vimont.nasaapod.screens.MainViewModel.NavigationEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.activityMainProductsListHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        registerObservers()

        if (!hasPostNotificationPermission()) {
            viewModel.hasNotificationPermissionBeingAsked()
        }
    }

    private fun registerObservers() {
        viewModel.navigationEvent.observe(this, ::handleNavigationEvent)
    }

    private fun handleNavigationEvent(navigationEvent: NavigationEvent) = when (navigationEvent) {
        NavigationEvent.AskForNotificationPermission -> NotificationPermissionDialogFragment.display(
            supportFragmentManager
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}