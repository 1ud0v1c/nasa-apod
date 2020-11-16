package com.ludovic.vimont.nasaapod.screens.settings

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.preferences.UserPreferences
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
        setHasOptionsMenu(true)
        activity?.let {
            it.title = getString(R.string.settings_activity_title)
            it.actionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        handleThemeModification()
        handleQuota()
        handleCache()
    }

    private fun handleThemeModification() {
        val themePreference: ListPreference? = findPreference("current_theme")
        themePreference?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference, newValue: Any ->
            when (newValue) {
                UserPreferences.THEME_LIGHT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                UserPreferences.THEME_DARK -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                UserPreferences.THEME_BATTERY -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
                UserPreferences.THEME_DEFAULT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
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
}