package com.csr.donor.screens

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.csr.donor.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.csr.donor.base.BaseFragment
import com.csr.donor.databinding.FragmentAddNewAddressBinding
import com.csr.donor.screens.viewModels.ViewModelAddAddressFragment
import com.csr.donor.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddAddressFragment :
    BaseFragment<FragmentAddNewAddressBinding>(FragmentAddNewAddressBinding::inflate),
    OnMapReadyCallback, GoogleMap.OnCameraMoveListener {

    private val zoom = 13f
    private var googleMap: GoogleMap? = null

    private val viewModel: ViewModelAddAddressFragment by activityViewModels()

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted && coarseLocationGranted) {
                // Both permissions are granted
                // Get the current location
                viewModel.fetchCurrentLocation()
            } else {
                // Handle the denied or permanently denied cases for either permission
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                ) {
                    // Permission is permanently denied (user selected "Never ask again")
                    // You may inform the user or redirect them to app settings
                    Bundle().apply {
                        putBoolean("SHOULD_GO_TO_SETTINGS", true)
                        // findNavController().navigate(R.id.bottomSheetAskLocationPermission, this)
                    }
                } else {
                    // Permission is denied (user selected "Deny" but not "Never ask again")
                    // You can handle this case by showing a rationale
                    Bundle().apply {
                        putBoolean("SHOULD_GO_TO_SETTINGS", false)
                        // findNavController().navigate(R.id.bottomSheetAskLocationPermission, this)
                    }
                }
            }
        }

    private fun checkLocationPermission() {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted && coarseLocationGranted) {
            // Both permissions are already granted
            // Get the current location
            viewModel.fetchCurrentLocation()
            googleMap?.isMyLocationEnabled = true
        } else {
            // Permission(s) is/are not yet granted, request them
            val permissionsToRequest = mutableListOf<String>()
            if (!fineLocationGranted) {
                permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (!coarseLocationGranted) {
                permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            locationPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            mapview.onCreate(savedInstanceState)
            mapview.getMapAsync(this@AddAddressFragment)
            tvYourLocation.isSelected = true
            ivLocateMe.setOnClickListener {
                viewModel.fetchCurrentLocation()
            }
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSelectedAddress.setOnClickListener {
                findNavController().navigate(R.id.bottomSheetDialogFragmentAddPickupAddressDetails)
            }
        }

        checkLocationPermission()

        viewModel.apply {
            moveToLocation.observe(viewLifecycleOwner) {
                val southwestBound = LatLng(22.45, 88.25)
                val northeastBound = LatLng(22.75, 88.5)
                val bounds = LatLngBounds(southwestBound, northeastBound)
                val isPointWithinBounds = bounds.contains(it.first)
                binding.btnSelectedAddress.apply {
                    isEnabled = isPointWithinBounds
                    text = if(!isPointWithinBounds)"No Service Available" else "Pick The Location"
                    alpha = if (isPointWithinBounds) 1f else 0.3f
                }
                googleMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it.first,
                        zoom
                    )
                )
            }
            isLoading.collectIn(viewLifecycleOwner) {
                binding.shimmerBtnAddLayout.isVisible = it
                binding.btnSelectedAddress.visibility = if(it) View.INVISIBLE else View.VISIBLE
            }
            pickupAddress.observe(viewLifecycleOwner) {
                binding.tvYourLocation.text = it
            }

        }
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.clear()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap?.isMyLocationEnabled = true
        }

        googleMap?.uiSettings?.setAllGesturesEnabled(true)
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
        try {
            googleMap?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.style_json
                )
            )

        } catch (e: Resources.NotFoundException) {
            Log.e("JAPAN", "Can't find style. Error: ", e)
        }



        googleMap?.setOnMapClickListener { latLng ->
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng,
                    zoom
                )
            )

        }

        googleMap?.setOnCameraMoveListener {
            viewModel.onGoogleMapMoving()
        }

        googleMap?.setOnCameraIdleListener {
            val cameraPosition = googleMap?.cameraPosition?.target ?: return@setOnCameraIdleListener
            val southwestBound = LatLng(22.45, 88.25)
            val northeastBound = LatLng(22.75, 88.5)
            val bounds = LatLngBounds(southwestBound, northeastBound)
            val isPointWithinBounds = bounds.contains(cameraPosition)
            binding.btnSelectedAddress.apply {
                isEnabled = isPointWithinBounds
                text = if(!isPointWithinBounds)"No Service Available" else "Pick The Location"
                alpha = if (!isPointWithinBounds) 0.3f else 1f
            }
            viewModel.fetchCurrentAddressFromGeoCoding(
                cameraPosition.latitude,
                cameraPosition.longitude
            )
        }
    }

    override fun onCameraMove() {

    }

}