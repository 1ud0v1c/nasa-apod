package com.ludovic.vimont.nasaapod.screens.about

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ludovic.vimont.nasaapod.BuildConfig
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.FragmentAboutBinding
import com.ludovic.vimont.nasaapod.helper.IntentHelper

class AboutFragment: Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        activity?.let {
            it.title = getString(R.string.about_activity_title)
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        textViewAppVersion.text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

        linearLayoutTwitter.setOnClickListener {
            context?.let {
                IntentHelper.openLink(it, it.getString(R.string.twitter_url))
            }
        }

        linearLayoutGithub.setOnClickListener {
            context?.let {
                IntentHelper.openLink(it, it.getString(R.string.github_url))
            }
        }

        linearLayoutRating.setOnClickListener {
            context?.let {
                IntentHelper.openLink(it, it.getString(R.string.play_store_url))
            }
        }

        textViewLicenses.setOnClickListener {
            context?.let {
                IntentHelper.openLicenses(it, getString(R.string.about_activity_licenses_title))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
