package com.ludovic.vimont.nasaapod.screens.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.settings_activity_title)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.constraint_layout_container, SettingsFragment())
            .commit()
    }
}