package com.ludovic.vimont.nasaapod.screens.detail

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.databinding.FragmentDetailBinding
import com.ludovic.vimont.nasaapod.ext.hasWritePermission
import com.ludovic.vimont.nasaapod.ext.loadImage
import com.ludovic.vimont.nasaapod.ext.requestPermission
import com.ludovic.vimont.nasaapod.helper.IntentHelper
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.ui.dialog.ProgressBarDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment: Fragment() {
    companion object {
        private const val SNACK_BAR_MAX_LINES: Int = 3
    }
    private val detailFragmentArgs: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModel()

    private lateinit var snackBar: Snackbar
    private lateinit var progressBarDialog: ProgressBarDialog

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transitionResource: Int = android.R.transition.move
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(transitionResource)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        activity?.let {
            it.title = getString(R.string.home_activity_title, NasaAPI.NUMBER_OF_DAY_TO_FETCH)
        }
        postponeEnterTransition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snackBar = Snackbar.make(binding.root, "", Snackbar.LENGTH_INDEFINITE)
        activity?.let {
            progressBarDialog = ProgressBarDialog(it)
        }

        val photoDate: String = detailFragmentArgs.photoDate
        viewModel.loadPhoto(photoDate)

        viewModel.photo.observe(viewLifecycleOwner) { photo: Photo ->
            activity?.let {
                it.title = photo.title
            }
            updateUI(photo)
        }

        viewModel.bitmap.observe(viewLifecycleOwner) { stateData: StateData<Bitmap> ->
            if (stateData.status == DataStatus.SUCCESS) {
                progressBarDialog.dismiss()
            } else if (stateData.status == DataStatus.ERROR_NETWORK) {
                snackBar.setText(stateData.errorMessage)
                    .setAction(getString(R.string.action_ok)) {
                        snackBar.dismiss()
                    }
            }
        }

        viewModel.bitmapDownloadProgression.observe(viewLifecycleOwner) { progression: Int ->
            progressBarDialog.update(progression)
        }
    }

    private fun updateUI(photo: Photo) {
        activity?.let { activity: FragmentActivity ->
            with(binding) {
                imageViewPhoto.loadImage(photo.getImageURL())

                if (photo.isMediaVideo()) {
                    val videoDrawable = ContextCompat.getDrawable(activity, R.drawable.ic_video)
                    imageViewMediaType.setImageDrawable(videoDrawable)
                }

                imageViewPhoto.setOnClickListener {
                    goToZoomFragment(photo)
                }
                imageViewPhoto.transitionName = photo.url
                startPostponedEnterTransition()

                val formattedDate: String = photo.getFormattedDate()
                textViewPhotoDate.text = getString(
                    R.string.detail_activity_photo_date,
                    formattedDate
                )
                textViewPhotoExplanation.text = getString(
                    R.string.detail_activity_explanation,
                    photo.explanation
                )
                photo.copyright?.let { copyright: String ->
                    textViewCopyright.text = getString(
                        R.string.detail_activity_copyright,
                        copyright
                    )
                }

                imageViewWallpaper.setOnClickListener {
                    setAsWallpaper(photo)
                }

                if (photo.isImageHDValid()) {
                    imageViewDownload.setOnClickListener {
                        launchDownloadImage(photo)
                    }
                } else {
                    imageViewDownload.visibility = View.GONE
                }

                imageViewShare.setOnClickListener {
                    val subject: String = getString(
                        R.string.detail_activity_share_subject,
                        formattedDate
                    )
                    IntentHelper.shareLink(activity, photo.getApodLink(), subject, photo.title)
                }
            }
        }
    }

    private fun goToZoomFragment(photo: Photo) {
        val mediaURL: String = if (photo.isMediaVideo()) photo.url else photo.hdurl ?: ""
        val action: NavDirections = DetailFragmentDirections.actionDetailFragmentToZoomFragment(
            photo.isMediaVideo(), mediaURL
        )
        findNavController().navigate(action)
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
            viewModel.setImageAsWallpaper(photo.hdurl)
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
        snackBarTextView.maxLines = SNACK_BAR_MAX_LINES
    }

    private fun launchDownloadImage(photo: Photo) {
        if (requireContext().hasWritePermission()) {
            downloadImage(photo)
        } else {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, requestCode = 0)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            val writePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            val isPermissionGranted = PackageManager.PERMISSION_GRANTED
            if (permissions[0] == writePermission && grantResults[0] == isPermissionGranted) {
                viewModel.photo.value?.let {
                    downloadImage(it)
                }
            }
        }
    }

    private fun downloadImage(photo: Photo) {
        if (photo.isImageHDValid()) {
            photo.hdurl?.let {
                viewModel.saveImage(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
