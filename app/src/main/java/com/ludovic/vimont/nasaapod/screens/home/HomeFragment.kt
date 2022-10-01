package com.ludovic.vimont.nasaapod.screens.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.api.NasaAPI
import com.ludovic.vimont.nasaapod.databinding.FragmentHomeBinding
import com.ludovic.vimont.nasaapod.ext.clearDecorations
import com.ludovic.vimont.nasaapod.helper.ViewHelper
import com.ludovic.vimont.nasaapod.helper.network.ConnectionLiveData
import com.ludovic.vimont.nasaapod.helper.network.NetworkHelper
import com.ludovic.vimont.nasaapod.helper.viewmodel.DataStatus
import com.ludovic.vimont.nasaapod.helper.viewmodel.StateData
import com.ludovic.vimont.nasaapod.model.Photo
import com.ludovic.vimont.nasaapod.preferences.UserPreferences
import com.ludovic.vimont.nasaapod.ui.dialog.NumberPickerDialog
import com.ludovic.vimont.nasaapod.ui.gridlayout.GridItemOffsetDecoration
import com.ludovic.vimont.nasaapod.ui.dialog.NumberPickerDialog.OnNumberPickerClickListener
import com.ludovic.vimont.nasaapod.ui.gridlayout.GridSpanSizeLookup
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment: Fragment() {
    private val photoAdapter = HomePhotoAdapter(ArrayList())
    private val viewModel: HomeViewModel by viewModel()
    private var numberOfDaysToFetch: Int = NasaAPI.NUMBER_OF_DAY_TO_FETCH

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        requireActivity().title = getString(R.string.home_activity_title, NasaAPI.NUMBER_OF_DAY_TO_FETCH)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleNetworkAvailability()
        configureRecyclerView()
        configurePullToRefresh()
        setNumberOfDaysToFetchObserver()
        setLayoutObserver()
        setPhotosObserver()
    }

    /**
     * Allow us to update the network availability of the HomeViewModel thanks to a live data, which
     * listen to network modification.
     */
    private fun handleNetworkAvailability() {
        activity?.let {
            connectionLiveData = ConnectionLiveData(it)
            connectionLiveData.observe(viewLifecycleOwner) { hasNetworkAccess: Boolean ->
                viewModel.setNetworkAvailability(hasNetworkAccess)
            }
            viewModel.setNetworkAvailability(NetworkHelper.isOnline(it))
        }
    }

    /**
     * Configure the recyclerView based on the layout chosen by the user. By default, it is a list.
     * @see: UserPreferences
     */
    private fun configureRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerViewPhotos
        val concatAdapter = ConcatAdapter(photoAdapter, HomePhotoFooterAdapter())
        recyclerView.adapter = concatAdapter
        recyclerView.clearDecorations()
        if (photoAdapter.layout == UserPreferences.LAYOUT_LIST) {
            recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        } else {
            val gridLayoutManager = GridLayoutManager(context, UserPreferences.GRID_SPAN_COUNT)
            gridLayoutManager.spanSizeLookup = GridSpanSizeLookup(photoAdapter, UserPreferences.GRID_SPAN_COUNT)
            recyclerView.layoutManager = gridLayoutManager
            val gridSpaceDimension: Int = resources.getDimension(R.dimen.item_photo_padding_size).toInt()
            recyclerView.addItemDecoration(
                GridItemOffsetDecoration(
                    UserPreferences.GRID_SPAN_COUNT,
                    gridSpaceDimension
                )
            )
        }
        postponeEnterTransition()
        recyclerView.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }
        photoAdapter.onItemClick = { imageView: ImageView, photo: Photo ->
            val extras: FragmentNavigator.Extras = FragmentNavigatorExtras(
                imageView to photo.url
            )
            val action: NavDirections = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                photo.date
            )
            findNavController().navigate(action, extras)
        }
    }

    private fun configurePullToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadNasaPhotos(true)
        }
    }

    private fun setNumberOfDaysToFetchObserver() {
        viewModel.numberOfDaysToFetch.observe(viewLifecycleOwner) { lastDesiredRange: Int ->
            numberOfDaysToFetch = lastDesiredRange
            requireActivity().title = getString(R.string.home_activity_title, numberOfDaysToFetch)
        }
    }

    private fun setLayoutObserver() {
        viewModel.layout.observe(viewLifecycleOwner) { newLayout: String ->
            if (photoAdapter.layout != newLayout) {
                photoAdapter.layout = newLayout
                configureRecyclerView()
            }
        }
    }

    private fun setPhotosObserver() {
        viewModel.photosState.observe(viewLifecycleOwner) { stateData: StateData<List<Photo>> ->
            when (stateData.status) {
                DataStatus.LOADING -> showLoadingStatus()
                DataStatus.SUCCESS -> showSuccessStatus(stateData)
                DataStatus.ERROR_NO_INTERNET -> showErrorStatus(stateData, false)
                DataStatus.ERROR_NETWORK -> showErrorStatus(stateData, true)
            }
            with(binding) {
                if (swipeRefreshLayout.isRefreshing) {
                    swipeRefreshLayout.isRefreshing = false
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

    private fun showLoadingStatus() {
        with(binding) {
            ViewHelper.fadeOutAnimation(recyclerViewPhotos, { it.visibility = View.GONE })
            ViewHelper.fadeInAnimation(linearLayoutStateContainer, { it.visibility = View.VISIBLE })
            imageViewState.setImageResource(R.drawable.state_loading)
            textViewStateTitle.text = getString(R.string.home_activity_loading_title)
            textViewStateDescription.text = getString(R.string.home_activity_loading_description)
            buttonStateAction.visibility = View.GONE
        }
    }

    private fun showSuccessStatus(stateData: StateData<List<Photo>>) {
        with(binding) {
            ViewHelper.fadeOutAnimation(linearLayoutStateContainer, { it.visibility = View.GONE })
            ViewHelper.fadeInAnimation(recyclerViewPhotos, { it.visibility = View.VISIBLE })
            stateData.data?.let { photos: List<Photo> ->
                photoAdapter.setItems(photos)
            }
        }
    }

    private fun showErrorStatus(stateData: StateData<List<Photo>>, hasInternet: Boolean) {
        with(binding) {
            ViewHelper.fadeOutAnimation(recyclerViewPhotos, { it.visibility = View.GONE })
            ViewHelper.fadeInAnimation(linearLayoutStateContainer, { it.visibility = View.VISIBLE })
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_refresh -> viewModel.loadNasaPhotos(true)
            R.id.menu_item_number_picker -> setUpNumberPicker()
            R.id.menu_item_settings -> goToSettingsFragment()
        }
        return true
    }

    private fun setUpNumberPicker() {
        val numberPickerDialog = NumberPickerDialog(requireActivity(), numberOfDaysToFetch)
        numberPickerDialog.show()
        numberPickerDialog.clickListener = OnNumberPickerClickListener { rangeOfDays ->
            viewModel.saveNumberOfDaysToFetchPreference(rangeOfDays)
            viewModel.loadNasaPhotos(true)
        }
    }

    private fun goToSettingsFragment() {
        val action: NavDirections = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerViewPhotos.adapter = null
        _binding = null
    }
}