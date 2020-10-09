package com.ludovic.vimont.nasaapod.screens.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.ActivityDetailBinding
import com.ludovic.vimont.nasaapod.helper.IntentHelper
import com.ludovic.vimont.nasaapod.helper.ViewHelper
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.home.HomeActivity

class DetailActivity : AppCompatActivity() {
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(HomeActivity.KEY_PHOTO_DATE)) {
            intent.extras?.getString(HomeActivity.KEY_PHOTO_DATE)?.let { photoDate: String ->
                viewModel.loadPhoto(photoDate)
                viewModel.photo.observe(this, { photo: Photo ->
                    title = photo.title
                    updateUI(photo)
                })
            }
        }
    }

    private fun updateUI(photo: Photo) {
        Glide.with(applicationContext)
            .load(photo.getImageURL())
            .placeholder(R.drawable.photo_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade(ViewHelper.GLIDE_FADE_IN_DURATION))
            .into(binding.imageViewPhoto)

        val formattedDate: String = photo.getFormattedDate()
        binding.textViewPhotoDate.text = getString(R.string.detail_activity_photo_date, formattedDate)
        binding.textViewPhotoExplanation.text = getString(R.string.detail_activity_explanation, photo.explanation)
        photo.copyright?.let { copyright: String ->
            binding.textViewCopyright.text = getString(
                R.string.detail_activity_copyright,
                copyright
            )
        }

        binding.imageViewShare.setOnClickListener {
            val subject: String = getString(R.string.detail_activity_share_subject, formattedDate)
            IntentHelper.shareLink(applicationContext, photo.getApodLink(), subject, photo.title)
        }
    }
}