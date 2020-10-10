package com.ludovic.vimont.nasaapod.screens.zoom

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.ActivityZoomBinding
import com.ludovic.vimont.nasaapod.helper.ViewHelper
import com.ludovic.vimont.nasaapod.helper.WindowHelper
import com.ludovic.vimont.nasaapod.screens.detail.DetailActivity
import com.ludovic.vimont.nasaapod.ui.BitmapRequestListener
import com.ludovic.vimont.nasaapod.ui.DrawableRequestListener

class ZoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityZoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowHelper.useImmersiveActivity(this)
        binding = ActivityZoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(DetailActivity.KEY_MEDIA_URL)) {
            intent.extras?.let { extras: Bundle ->
                val mediaIsVideo: Boolean = extras.getBoolean(DetailActivity.KEY_MEDIA_IS_A_VIDEO)
                val mediaURL: String = extras.getString(DetailActivity.KEY_MEDIA_URL) ?: ""
                if (mediaIsVideo) {
                    loadVideo(mediaURL)
                } else {
                    loadImage(mediaURL)
                    showProgressBar()
                }
            }
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
        Glide.with(applicationContext)
            .load(mediaURL)
            .transition(DrawableTransitionOptions.withCrossFade(ViewHelper.GLIDE_FADE_IN_DURATION))
            .listener(DrawableRequestListener {
                hideProgressBar()
            })
            .into(binding.photoViewHd)
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
}