package com.ludovic.vimont.nasaapod.screens.zoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.ActivityZoomBinding
import com.ludovic.vimont.nasaapod.helper.ViewHelper
import com.ludovic.vimont.nasaapod.screens.detail.DetailActivity

class ZoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityZoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useImmersiveActivity()
        binding = ActivityZoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(DetailActivity.KEY_MEDIA_URL)) {
            intent.extras?.getString(DetailActivity.KEY_MEDIA_URL)?.let { mediaURL: String ->
                Glide.with(applicationContext)
                    .load(mediaURL)
                    .placeholder(R.drawable.photo_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade(ViewHelper.GLIDE_FADE_IN_DURATION))
                    .into(binding.photoViewHd)
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
}