package com.ludovic.vimont.nasaapod.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.ActivityMainBinding
import com.ludovic.vimont.nasaapod.screens.home.HomeFragmentDirections

class MainActivity: AppCompatActivity() {
    companion object {
        const val KEY_PHOTO_DATE = "nasa_apod_photo_date"
        const val KEY_OPEN_DETAIL_FRAGMENT = "nasa_apod_detail_fragment"
    }
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.activityMainProductsListHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        intent.extras?.let { bundle: Bundle ->
            goToDetailFragmentFromNotification(bundle)
        }
    }

    private fun goToDetailFragmentFromNotification(bundle: Bundle) {
        if (bundle.containsKey(KEY_OPEN_DETAIL_FRAGMENT)) {
            bundle.getString(KEY_PHOTO_DATE)?.let { photoDate: String ->
                val action: NavDirections = HomeFragmentDirections.actionHomeFragmentToDetailFragment(photoDate)
                val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.activityMainProductsListHostFragment) as NavHostFragment
                val navController: NavController = navHostFragment.navController
                navController.navigate(action)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}