package com.ludovic.vimont.nasaapod.screens.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.databinding.ActivityHomeBinding
import com.ludovic.vimont.nasaapod.ext.clearDecorations
import com.ludovic.vimont.nasaapod.helper.ViewHelper
import com.ludovic.vimont.nasaapod.helper.network.ConnectionLiveData
import com.ludovic.vimont.nasaapod.helper.network.NetworkHelper
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.preferences.UserPreferences
import com.ludovic.vimont.nasaapod.screens.detail.DetailActivity
import com.ludovic.vimont.nasaapod.screens.settings.SettingsActivity
import com.ludovic.vimont.nasaapod.ui.GridItemOffsetDecoration
import com.ludovic.vimont.nasaapod.ui.dialog.NumberPickerDialog
import kotlin.collections.ArrayList
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity: AppCompatActivity() {
    companion object {
        const val KEY_PHOTO_DATE = "nasa_apod_photo_date"
    }
    private val photoAdapter = HomePhotoAdapter(ArrayList())
    private val viewModel: HomeViewModel by viewModel()
    private var numberOfDaysToFetch: Int = NasaAPI.NUMBER_OF_DAY_TO_FETCH
    private lateinit var binding: ActivityHomeBinding
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = getString(R.string.home_activity_title, NasaAPI.NUMBER_OF_DAY_TO_FETCH)

        handleNetworkAvailability()

        viewModel.numberOfDaysToFetch.observe(this) { lastDesiredRange: Int ->
            numberOfDaysToFetch = lastDesiredRange
            title = getString(R.string.home_activity_title, numberOfDaysToFetch)
        }

        viewModel.layout.observe(this, { newLayout: String ->
            if (photoAdapter.layout != newLayout) {
                photoAdapter.layout = newLayout
                configureRecyclerView()
            }
        })

        viewModel.photosState.observe(this) { stateData: StateData<List<Photo>> ->
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
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadLayout()
        viewModel.loadNasaPhotos()
        viewModel.loadNumberOfDaysToFetchPreference()
    }

    /**
     * Configure the recyclerView based on the layout chosen by the user. By default, it is a list.
     * @see: UserPreferences
     */
    private fun configureRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerViewPhotos
        recyclerView.adapter = photoAdapter
        recyclerView.clearDecorations()
        if (photoAdapter.layout == UserPreferences.LAYOUT_LIST) {
            recyclerView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        } else {
            recyclerView.layoutManager = StaggeredGridLayoutManager(UserPreferences.GRID_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
            val gridSpaceDimension: Int = resources.getDimension(R.dimen.item_photo_padding_size).toInt()
            recyclerView.addItemDecoration(GridItemOffsetDecoration(UserPreferences.GRID_SPAN_COUNT, gridSpaceDimension))
        }
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
                photoAdapter.setItems(photos)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_refresh -> {
                viewModel.loadNasaPhotos(true)
            }
            R.id.menu_item_number_picker -> {
                val numberPickerDialog = NumberPickerDialog(this, numberOfDaysToFetch)
                numberPickerDialog.show()
                numberPickerDialog.onValidateClick = { rangeOfDays: Int ->
                    viewModel.saveNumberOfDaysToFetchPreference(rangeOfDays)
                    viewModel.loadNasaPhotos(true)
                }
            }
            R.id.menu_item_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}