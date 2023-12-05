package com.csr.beneficiary.partner_screens

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.csr.beneficiary.R
import com.csr.common.base.BaseFragment
import com.csr.beneficiary.databinding.FragmentHomeBinding
import com.csr.beneficiary.partner_screens.adapters.AdapterPartnerPickupOrderList
import com.csr.beneficiary.partner_screens.viewModels.ViewModelHomeFragment
import com.csr.beneficiary.placeFilter.FilterActivity
import com.csr.beneficiary.placeFilter.adapters.PickupOrderFilterHorizontalAdapter
import com.csr.beneficiary.placeFilter.model.PickupOrderFilterType
import com.recyclerdada.partner.utils.callToNumber
import com.recyclerdada.partner.utils.openAppSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    OnMapReadyCallback {

    private lateinit var adapterPartnerPickupOrderList: AdapterPartnerPickupOrderList
    private val viewModel: ViewModelHomeFragment by viewModels()
    private val zoom = 13f
    private var googleMap: GoogleMap? = null
    private lateinit var pickupOrderFilterHorizontalAdapter: PickupOrderFilterHorizontalAdapter


    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted =
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted && coarseLocationGranted) {
                // Both permissions are granted
                // Get the current location
                viewModel.fetchNearByPickupOrders()
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

    private fun checkLocationPermission(shouldAnimate: Boolean = false) {
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
            viewModel.fetchNearByPickupOrders()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterPartnerPickupOrderList = AdapterPartnerPickupOrderList(requireContext())
        pickupOrderFilterHorizontalAdapter = PickupOrderFilterHorizontalAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLocationPermission(shouldAnimate = true)
        binding.apply {
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this@HomeFragment)
            rvFilters.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = pickupOrderFilterHorizontalAdapter
            }
            rvNearbyPickupOrders.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = adapterPartnerPickupOrderList
            }
        }
        viewModel.apply {
            selectedFilters.observe(viewLifecycleOwner) {
                pickupOrderFilterHorizontalAdapter.setFilters(it)
            }
            pickupOrdersList.observe(viewLifecycleOwner) {
                binding.apply {
                    animationView.isVisible = it.isEmpty()
                    titleEmpty.isVisible = it.isEmpty()
                }
                googleMap?.clear()
                it.map { orderDto ->
                    LatLng(
                        orderDto.location?.coordinates?.firstOrNull() ?: 0.0,
                        orderDto.location?.coordinates?.getOrNull(1) ?: 0.0
                    )
                }.forEach { latLangs ->
                    setLocationMarkers(latLangs)
                }
                adapterPartnerPickupOrderList.setPickupOrderList(it)


            }
            myLatLong.observe(viewLifecycleOwner) {
                googleMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it,
                        zoom
                    )
                )
            }
        }
//        MyApplication.onFilterChanged.collectIn(viewLifecycleOwner){
//            viewModel.fetchNearByPickupOrders()
//        }
        adapterPartnerPickupOrderList.setOnCallNowClicked {
            val bundle = Bundle().apply {
                putString("orderId", it)
            }
            findNavController().navigate(R.id.pickupOrderDetailsFragment, bundle)
        }
        pickupOrderFilterHorizontalAdapter.setOnFilterItemClicked { placeFilter ->
            if (placeFilter.filterType == PickupOrderFilterType.FULL_FILTER) {
                startActivity(Intent(requireContext(), FilterActivity::class.java))
            }
        }
    }


    private val requestCallPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission is granted, you can make a phone call here
                callToNumber(
                    requireContext(),
                    viewModel.numberToCall
                )
            } else {
                // Permission is denied, handle it as needed (e.g., show a message)
                // You may want to inform the user that the permission is required to make calls.
                requireContext().openAppSettings()
            }
        }


    private fun requestCallPermissionAndMakeCall() {
        val permission = Manifest.permission.CALL_PHONE
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, make the call
            callToNumber(
                requireContext(),
                viewModel.numberToCall
            )
        } else {
            // Permission is not granted, request it
            requestCallPermissionLauncher.launch(permission)
        }
    }

    private fun setLocationMarkers(latLng: LatLng) {
        val markerOptions = MarkerOptions()
            .position(latLng)
        val marker = googleMap?.addMarker(markerOptions)
    }

    private fun showAlert() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        // Set the title for the alert dialog
        alertDialogBuilder.setTitle("Want to Call This Seller?")

        // Set the message for the alert dialog
        alertDialogBuilder.setMessage("Please mention that you are calling from Recycler Dada.")

        // Set a positive button and its click listener
        alertDialogBuilder.setPositiveButton("Call") { dialog, which ->
            requestCallPermissionAndMakeCall()
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        // Create and show the alert dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
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


    }

}