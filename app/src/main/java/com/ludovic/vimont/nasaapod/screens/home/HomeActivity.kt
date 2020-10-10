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

        viewModel.photosState.observe(this, { stateData: StateData<List<Photo>> ->
            when (stateData.status) {
                DataStatus.LOADING -> {
                    showLoadingStatus()
                }
                DataStatus.SUCCESS -> {
                    showSuccessStatus(stateData)
                }
                DataStatus.ERROR_NO_INTERNET -> {
                    showErrorStatus(stateData, false)
                }
                DataStatus.ERROR_NETWORK -> {
                    showErrorStatus(stateData, true)
                }
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
        with(binding) {
            ViewHelper.fadeOutAnimation(recyclerViewPhotos, {
                recyclerViewPhotos.visibility = View.GONE
            })
            ViewHelper.fadeInAnimation(linearLayoutStateContainer, {
                linearLayoutStateContainer.visibility = View.VISIBLE
            })
            imageViewState.setImageResource(R.drawable.state_loading)
            textViewStateTitle.text = getString(R.string.home_activity_loading_title)
            textViewStateDescription.text = getString(R.string.home_activity_loading_description)
            buttonStateAction.visibility = View.GONE
        }
    }

    private fun showSuccessStatus(stateData: StateData<List<Photo>>) {
        with(binding) {
            ViewHelper.fadeOutAnimation(linearLayoutStateContainer, {
                linearLayoutStateContainer.visibility = View.GONE
            })
            ViewHelper.fadeInAnimation(recyclerViewPhotos, {
                recyclerViewPhotos.visibility = View.VISIBLE
            })
            stateData.data?.let { photos: List<Photo> ->
                photoAdapter.addItems(photos)
            }
        }
    }

    private fun showErrorStatus(stateData: StateData<List<Photo>>, hasInternet: Boolean) {
        with(binding) {
            ViewHelper.fadeOutAnimation(recyclerViewPhotos, {
                recyclerViewPhotos.visibility = View.GONE
            })
            ViewHelper.fadeInAnimation(linearLayoutStateContainer, {
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
            val buttonState: Button = buttonStateAction
            buttonState.text = getString(R.string.action_retry)
            buttonState.visibility = View.VISIBLE
            buttonState.setOnClickListener {
                viewModel.loadNasaPhotos()
            }
        }
    }
}