package com.ludovic.vimont.nasaapod.screens.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.ActivityDetailBinding
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.home.HomeActivity
import com.ludovic.vimont.nasaapod.screens.home.HomePhotoAdapter

class DetailActivity : AppCompatActivity() {
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(HomeActivity.KEY_PHOTO_ID)) {
            intent.extras?.getInt(HomeActivity.KEY_PHOTO_ID)?.let { photoId: Int ->
                viewModel.loadPhoto(photoId)
                viewModel.photo.observe(this, { photo: Photo ->
                    title = photo.title
                    updateUI(photo)
                })
            }
        }
    }

    private fun updateUI(photo: Photo) {
        val cornersRadiusSize: Int = applicationContext.resources.getDimension(R.dimen.item_photo_corners_radius).toInt()
        val factory: DrawableCrossFadeFactory = DrawableCrossFadeFactory.Builder(HomePhotoAdapter.FADE_IN_DURATION)
            .setCrossFadeEnabled(true)
            .build()

        Glide.with(applicationContext)
            .load(photo.getImageURL())
            .placeholder(R.drawable.photo_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .transform(CenterCrop(), RoundedCorners(cornersRadiusSize))
            .into(binding.imageViewPhoto)

        binding.textViewPhotoDate.text = getString(R.string.detail_activity_photo_date, photo.getFormattedDate())
        binding.textViewPhotoExplanation.text = photo.explanation
        photo.copyright?.let { copyright ->
            binding.textViewCopyright.text = getString(R.string.detail_activity_copyright, copyright)
        }
    }
}