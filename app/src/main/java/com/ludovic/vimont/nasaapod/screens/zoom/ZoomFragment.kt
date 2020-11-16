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
import com.ludovic.vimont.nasaapod.helper.ViewHelper
import com.ludovic.vimont.nasaapod.helper.WindowHelper
import com.ludovic.vimont.nasaapod.ui.DrawableRequestListener


class ZoomFragment: Fragment() {
    private val zoomFragmentArgs: ZoomFragmentArgs by navArgs()
    private lateinit var binding: FragmentZoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            WindowHelper.useImmersiveActivity(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentZoomBinding.inflate(inflater, container, false)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
         val mediaURL: String = zoomFragmentArgs.mediaUrl
         if (zoomFragmentArgs.isMediaVideo) {
             loadVideo(mediaURL)
         } else {
             loadImage(mediaURL)
             showProgressBar()
         }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadVideo(mediaURL: String) {
        with(binding) {
            photoViewHd.visibility = View.GONE
            webViewVideo.visibility = View.VISIBLE
            webViewVideo.settings.javaScriptEnabled = true
            webViewVideo.loadUrl(mediaURL)
            webViewVideo.webChromeClient = WebChromeClient()
        }
    }

    private fun loadImage(mediaURL: String) {
        activity?.let {
            Glide.with(it)
                .load(mediaURL)
                .transition(DrawableTransitionOptions.withCrossFade(ViewHelper.GLIDE_FADE_IN_DURATION))
                .listener(DrawableRequestListener {
                    hideProgressBar()
                })
                .into(binding.photoViewHd)
        }
    }

    private fun showProgressBar() {
        val progressBar: ProgressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        val progressBar: ProgressBar = binding.progressBar
        if (progressBar.isVisible) {
            ViewHelper.fadeOutAnimation(progressBar, {
                progressBar.visibility = View.GONE
            })
        }
    }

    override fun onDestroyView() {
        activity?.let {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            WindowHelper.resetActivityUIVisibility(it)
        }
        super.onDestroyView()
    }
}