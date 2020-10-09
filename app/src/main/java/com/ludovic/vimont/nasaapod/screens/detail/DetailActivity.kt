package com.ludovic.vimont.nasaapod.screens.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.ludovic.vimont.nasaapod.databinding.ActivityDetailBinding
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.home.HomeActivity

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
                    println(photo)
                })
            }
        }
    }
}