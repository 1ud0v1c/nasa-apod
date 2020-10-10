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
import com.ludovic.vimont.nasaapod.screens.detail.DetailActivity
import com.ludovic.vimont.nasaapod.ui.BitmapRequestListener
import com.ludovic.vimont.nasaapod.ui.DrawableRequestListener

class ZoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityZoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useImmersiveActivity()
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

    /**
     * Use to enable immersive mode: https://developer.android.com/training/system-ui/immersive.
     */
    private fun useImmersiveActivity() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadVideo(mediaURL: String) {
        binding.photoViewHd.visibility = View.GONE
        val videoWebView: WebView = binding.webViewVideo
        videoWebView.visibility = View.VISIBLE
        videoWebView.settings.javaScriptEnabled = true
        videoWebView.loadUrl(mediaURL)
        videoWebView.webChromeClient = WebChromeClient()
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