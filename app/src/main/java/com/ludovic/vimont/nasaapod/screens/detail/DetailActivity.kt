package com.ludovic.vimont.nasaapod.screens.detail

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.ActivityDetailBinding
import com.ludovic.vimont.nasaapod.helper.IntentHelper
import com.ludovic.vimont.nasaapod.helper.ViewHelper
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.home.HomeActivity
import com.ludovic.vimont.nasaapod.screens.zoom.ZoomActivity
import com.ludovic.vimont.nasaapod.ui.dialog.ProgressBarDialog

class DetailActivity : AppCompatActivity() {
    companion object {
        const val KEY_MEDIA_URL = "nasa_apod_photo_media_url"
        const val KEY_MEDIA_IS_A_VIDEO = "nasa_apod_photo_media_is_video"
    }
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var progressBarDialog: ProgressBarDialog
    private lateinit var snackBar: Snackbar
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        snackBar = Snackbar.make(binding.root, "Strange, strange, strange", Snackbar.LENGTH_INDEFINITE)
        progressBarDialog = ProgressBarDialog(this)

        if (intent.hasExtra(HomeActivity.KEY_PHOTO_DATE)) {
            intent.extras?.getString(HomeActivity.KEY_PHOTO_DATE)?.let { photoDate: String ->
                viewModel.loadPhoto(photoDate)

                viewModel.photo.observe(this) { photo: Photo ->
                    title = photo.title
                    updateUI(photo)
                }

                viewModel.bitmap.observe(this) { stateData: StateData<Bitmap> ->
                    if (stateData.status == DataStatus.SUCCESS) {
                        progressBarDialog.dismiss()
                    } else if (stateData.status == DataStatus.ERROR_NETWORK) {
                        snackBar.setText(stateData.errorMessage)
                            .setAction(getString(R.string.action_ok)) {
                                snackBar.dismiss()
                            }
                    }
                }

                viewModel.bitmapDownloadProgression.observe(this) { progression: Int ->
                    progressBarDialog.update(progression)
                }
            }
        }
    }

    private fun updateUI(photo: Photo) {
        with(binding) {
            Glide.with(applicationContext)
                .load(photo.getImageURL())
                .placeholder(R.drawable.photo_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade(ViewHelper.GLIDE_FADE_IN_DURATION))
                .into(imageViewPhoto)

            if (photo.isMediaVideo()) {
                imageViewMediaType.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_video))
            }

            imageViewPhoto.setOnClickListener {
                val mediaURL: String? = if (photo.isMediaVideo()) photo.url else photo.hdurl ?: ""
                val intent = Intent(applicationContext, ZoomActivity::class.java)
                intent.putExtra(KEY_MEDIA_URL, mediaURL)
                intent.putExtra(KEY_MEDIA_IS_A_VIDEO, photo.isMediaVideo())
                startActivity(intent)
            }

            val formattedDate: String = photo.getFormattedDate()
            textViewPhotoDate.text = getString(R.string.detail_activity_photo_date, formattedDate)
            textViewPhotoExplanation.text = getString(R.string.detail_activity_explanation, photo.explanation)
            photo.copyright?.let { copyright: String ->
                textViewCopyright.text = getString(
                    R.string.detail_activity_copyright,
                    copyright
                )
            }

            imageViewWallpaper.setOnClickListener {
                setAsWallpaper(photo)
            }

            imageViewShare.setOnClickListener {
                val subject: String = getString(R.string.detail_activity_share_subject, formattedDate)
                IntentHelper.shareLink(applicationContext, photo.getApodLink(), subject, photo.title)
            }
        }
    }

    /**
     * We only set Wallpaper with HD images. So we cannot do it for thumbnail of videos
     */
    private fun setAsWallpaper(photo: Photo) {
        if (photo.isMediaImage() && photo.hdurl != null) {
            progressBarDialog.show()
            progressBarDialog.onCancelClick = {
                viewModel.cancelImageDownload()
                progressBarDialog.update(0)
                progressBarDialog.dismiss()
            }
            viewModel.downloadImageHD(photo.hdurl)
        } else {
            val errorMessage: String = getString(R.string.detail_activity_cannot_set_wallpaper_for_video)
            updateSnackBar(errorMessage)
            snackBar.setAction(getString(R.string.action_ok)) {
                snackBar.dismiss()
            }
            snackBar.show()
        }
    }

    private fun updateSnackBar(text: String, length: Int = Snackbar.LENGTH_INDEFINITE) {
        snackBar = Snackbar.make(binding.root, text, length)
        val snackBarView: View = snackBar.view
        val snackBarTextView: TextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text)
        snackBarTextView.maxLines = 3
    }
}