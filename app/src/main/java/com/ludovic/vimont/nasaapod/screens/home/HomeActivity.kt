package com.ludovic.vimont.nasaapod.screens.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.ActivityHomeBinding
import com.ludovic.vimont.nasaapod.helper.ViewHelper
import com.ludovic.vimont.nasaapod.helper.network.ConnectionLiveData
import com.ludovic.vimont.nasaapod.helper.network.NetworkHelper
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus.LOADING
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus.SUCCESS
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus.ERROR_NO_INTERNET
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus.ERROR_NETWORK
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.screens.detail.DetailActivity
import kotlin.collections.ArrayList

class HomeActivity: AppCompatActivity() {
    companion object {
        const val KEY_PHOTO_DATE = "nasa_apod_photo_date"
    }
    private val photoAdapter = HomePhotoAdapter(ArrayList())
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.home_activity_title)

        configureRecyclerView()
        handleNetworkAvailability()
        viewModel.loadNasaPhotos()

        viewModel.photosState.observe(this, { stateData ->
            when (stateData.status) {
                LOADING -> showLoadingStatus()
                SUCCESS -> showSuccessStatus(stateData)
                ERROR_NO_INTERNET -> showErrorStatus(stateData, false)
                ERROR_NETWORK -> showErrorStatus(stateData, true)
            }
        })
    }

    private fun configureRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerViewPhotos
        recyclerView.adapter = photoAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        photoAdapter.onItemClick = { photo: Photo ->
            val intent = Intent(applicationContext, DetailActivity::class.java)
            intent.putExtra(KEY_PHOTO_DATE, photo.date)
            startActivity(intent)
        }
    }

    /**
     * Allow us to update the network availability of the HomeViewModel thanks to a live data, which
     * listen to network modification.
     */
    private fun handleNetworkAvailability() {
        connectionLiveData = ConnectionLiveData(applicationContext)
        connectionLiveData.observe(this) { hasNetworkAccess: Boolean ->
            viewModel.setNetworkAvailability(hasNetworkAccess)
        }
        viewModel.setNetworkAvailability(NetworkHelper.isOnline(applicationContext))
    }

    private fun showLoadingStatus() {
        ViewHelper.fadeOutAnimation(binding.recyclerViewPhotos, {
            binding.recyclerViewPhotos.visibility = View.GONE
        })
        ViewHelper.fadeInAnimation(binding.linearLayoutStateContainer, {
            binding.linearLayoutStateContainer.visibility = View.VISIBLE
        })
        binding.imageViewState.setImageResource(R.drawable.state_loading)
        binding.textViewStateTitle.text = getString(R.string.home_activity_loading_title)
        binding.textViewStateDescription.text = getString(R.string.home_activity_loading_description)
        binding.buttonStateAction.visibility = View.GONE
    }

    private fun showSuccessStatus(stateData: StateData<List<Photo>>) {
        ViewHelper.fadeOutAnimation(binding.linearLayoutStateContainer, {
            binding.linearLayoutStateContainer.visibility = View.GONE
        })
        ViewHelper.fadeInAnimation(binding.recyclerViewPhotos, {
            binding.recyclerViewPhotos.visibility = View.VISIBLE
        })
        stateData.data?.let { photos: List<Photo> ->
            photoAdapter.addItems(photos)
        }
    }

    private fun showErrorStatus(stateData: StateData<List<Photo>>, hasInternet: Boolean) {
        with(binding) {
            ViewHelper.fadeOutAnimation(binding.recyclerViewPhotos, {
                recyclerViewPhotos.visibility = View.GONE
            })
            ViewHelper.fadeInAnimation(binding.linearLayoutStateContainer, {
                linearLayoutStateContainer.visibility = View.VISIBLE
            })
            if (hasInternet) {
                imageViewState.setImageResource(R.drawable.state_request_error)
                textViewStateTitle.text = getString(R.string.home_activity_error_title)
                textViewStateDescription.text = stateData.errorMessage
            } else {
                imageViewState.setImageResource(R.drawable.state_error_no_internet)
                textViewStateTitle.text = getString(R.string.home_activity_no_internet_title)
                textViewStateDescription.text = getString(R.string.home_activity_no_internet_description)
            }
            buttonStateAction.apply {
                text = getString(R.string.action_retry)
                visibility = View.VISIBLE
                setOnClickListener {
                    viewModel.loadNasaPhotos()
                }
            }
        }
    }
}