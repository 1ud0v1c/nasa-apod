package com.ludovic.vimont.nasaapod.screens.settings

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.ludovic.vimont.nasaapod.R

class SettingsFragment: PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleCache()
        handleQuota()
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
                val quotaInformation: String = getString(R.string.home_activity_display_quota, remainingQuota)
                val snackBar: Snackbar = Snackbar.make(rootView, quotaInformation, Snackbar.LENGTH_INDEFINITE)
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
}