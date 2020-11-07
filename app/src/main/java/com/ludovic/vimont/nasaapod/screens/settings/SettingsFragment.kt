package com.ludovic.vimont.nasaapod.screens.settings

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.ludovic.vimont.nasaapod.R

class SettingsFragment: PreferenceFragmentCompat() {
    companion object {
        private const val LIGHT = "light"
        private const val DARK = "dark"
        private const val BATTERY = "battery"
        private const val DEFAULT = "default"
    }
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleCache()
        handleQuota()
        handleThemeModification()
    }

    private fun handleCache() {
        val clearCachePreference: Preference? = findPreference("clear_cache")
        viewModel.getGlideCacheSize()
        viewModel.glideCacheSize.observe(viewLifecycleOwner) { cacheSize: Long ->
            clearCachePreference?.summary = getString(R.string.settings_cache_size, cacheSize)
        }
        clearCachePreference?.setOnPreferenceClickListener {
            viewModel.launchClearCache()
            true
        }
    }

    private fun handleQuota() {
        val viewQuotaPreference: Preference? = findPreference("view_quota")
        viewModel.quota.observe(viewLifecycleOwner) { remainingQuota: String ->
            view?.rootView?.let { rootView: View ->
                val quotaInformation: String = getString(
                    R.string.home_activity_display_quota,
                    remainingQuota
                )
                val snackBar: Snackbar = Snackbar.make(
                    rootView,
                    quotaInformation,
                    Snackbar.LENGTH_INDEFINITE
                )
                snackBar.setAction(getString(R.string.action_ok)) {
                    snackBar.dismiss()
                }
                val snackBarView: View = snackBar.view
                val snackBarTextView: TextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text)
                snackBarTextView.maxLines = 3
                snackBar.show()
            }
        }
        viewQuotaPreference?.setOnPreferenceClickListener {
            viewModel.loadQuota()
            true
        }
    }

    private fun handleThemeModification() {
        val themePreference: ListPreference? = findPreference("change_theme")
        themePreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference, newValue: Any ->
            when (newValue) {
                LIGHT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                DARK -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                BATTERY -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
                DEFAULT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
            true
        }
    }
}