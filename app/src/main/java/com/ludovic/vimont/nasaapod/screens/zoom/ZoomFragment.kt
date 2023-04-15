package com.ludovic.vimont.nasaapod.screens.zoom

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ludovic.vimont.nasaapod.databinding.FragmentZoomBinding
import com.ludovic.vimont.nasaapod.ext.resetActivityUIVisibility
import com.ludovic.vimont.nasaapod.ext.useImmersiveActivity
import com.ludovic.vimont.nasaapod.helper.ViewHelper
import com.ludovic.vimont.nasaapod.ui.DrawableRequestListener

class ZoomFragment: Fragment() {
    private val zoomFragmentArgs: ZoomFragmentArgs by navArgs()

    private var _binding: FragmentZoomBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.useImmersiveActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentZoomBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mediaURL: String = zoomFragmentArgs.mediaUrl
        if (zoomFragmentArgs.isMediaVideo) {
            loadVideo(mediaURL)
        } else {
            loadImage(mediaURL)
            showProgressBar()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadVideo(mediaURL: String) = with(binding) {
        photoViewHd.visibility = View.GONE
        webViewVideo.visibility = View.VISIBLE
        webViewVideo.settings.javaScriptEnabled = true
        webViewVideo.loadUrl(mediaURL)
        webViewVideo.webChromeClient = WebChromeClient()
    }

    private fun loadImage(mediaURL: String) {
        Glide.with(requireContext())
            .load(mediaURL)
            .transition(DrawableTransitionOptions.withCrossFade(ViewHelper.GLIDE_FADE_IN_DURATION))
            .listener(DrawableRequestListener { hideProgressBar() })
            .into(binding.photoViewHd)
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        val progressBar: ProgressBar = binding.progressBar
        if (progressBar.isVisible) {
            ViewHelper.fadeOutAnimation(progressBar, { it.visibility = View.GONE })
        }
    }

    override fun onPause() {
        super.onPause()
        Glide.with(requireContext()).clear(binding.photoViewHd)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onDestroyView() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requireActivity().window.resetActivityUIVisibility()
        super.onDestroyView()
        _binding = null
    }
}