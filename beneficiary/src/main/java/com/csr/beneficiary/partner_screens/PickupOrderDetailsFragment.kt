package com.csr.beneficiary.partner_screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.csr.beneficiary.R
import com.csr.common.base.BaseFragment
import com.csr.beneficiary.databinding.FragmentPickupOrderDetailsBinding
import com.csr.beneficiary.partner_screens.adapters.ImageSliderAdapter
import com.csr.beneficiary.partner_screens.viewModels.ViewModelPickupOrderDetailsFragment
import com.recyclerdada.partner.utils.callToNumber
import com.csr.beneficiary.utils.collectIn
import com.recyclerdada.partner.utils.openAppSettings
import com.recyclerdada.partner.utils.openDirection
import com.csr.beneficiary.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PickupOrderDetailsFragment :
    BaseFragment<FragmentPickupOrderDetailsBinding>(FragmentPickupOrderDetailsBinding::inflate) {


    private lateinit var adapterImageSlider: ImageSliderAdapter
    private val viewModel: ViewModelPickupOrderDetailsFragment by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterImageSlider = ImageSliderAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orderId = arguments?.getString("orderId") ?: ""
        viewModel.fetchOrderDetailsById(orderId)
        binding.ivArrowBack.setOnClickListener { findNavController().popBackStack() }
        binding.vpScrapImages.adapter = adapterImageSlider
        binding.vpScrapImages.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val currentPageIndex = 1
        binding.vpScrapImages.currentItem = currentPageIndex
        TabLayoutMediator(binding.vpImageIndicator, binding.vpScrapImages) { tab, position ->

        }.attach()
        observeData()
    }

    private fun setScrapType(chipData: List<String>) {
        binding.cgScrapTypes.removeAllViews()
        for (scrapType in chipData) {
            val chip = Chip(requireContext())
            chip.text = scrapType
            chip.chipIcon = ContextCompat.getDrawable(
                requireContext(),
                viewModel.scrapeItemList.find { it.name.equals(scrapType, true) }?.icon
                    ?: R.drawable.ic_newpaper
            )
            chip.isCheckable = true
            chip.isChecked = true
            chip.isEnabled = false
            binding.cgScrapTypes.addView(chip)
            chip.id = View.generateViewId()
//            chip.setOnCheckedChangeListener { buttonView, isChecked ->
//                // Handle chip selection change here
//                viewModel.onScrapUnSelected(scrapType.id)
//            }
        }

        // Set a listener for chip selection change

    }

    private fun observeData() {
        viewModel.apply {
            orderDetails.observe(viewLifecycleOwner) { orderDto ->
                setScrapType(orderDto.scrapTypes ?: emptyList())
                binding.apply {
                    tvScrapQuantity.text =
                        "${orderDto.orderEstimatedWeight} ${orderDto.orderEstimatedWeightUnit}"
                    tvPickupAddress.text = orderDto.pickupLocation?.address
                    tvPickupAddress.isSelected = true
                    tvCity.text = orderDto.pickupLocation?.city
                    tvLandmark.text = orderDto.pickupLocation?.landmarkName
                    tvPincode.text = orderDto.pickupLocation?.pincode
                    btnCallNow.setOnClickListener { requestCallPermissionAndMakeCall() }
                    btnDirection.setOnClickListener {
                        openDirection(
                            requireContext(),
                            placeName = "${orderDto.orderEstimatedWeight} ${orderDto.orderEstimatedWeightUnit}",
                            latLng = LatLng(
                                orderDto.location?.coordinates?.firstOrNull() ?: 0.0,
                                orderDto.location?.coordinates?.getOrNull(1) ?: 0.0
                            )
                        )
                    }
                    orderDto.scrapImages?.let { adapterImageSlider.setScrapImages(it) }
                }
            }
            error.collectIn(viewLifecycleOwner) {
                requireActivity().showToast(it)
            }
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


}