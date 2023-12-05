package com.csr.donor.screens

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.csr.donor.R
import com.csr.donor.base.BaseFragment
import com.csr.donor.databinding.FragmentPickupsBinding
import com.csr.donor.screens.adapters.AdapterPickupTime
import com.csr.donor.screens.adapters.AdapterScrapItemWeight
import com.csr.donor.screens.adapters.AdapterUserAddress
import com.csr.donor.screens.viewModels.ViewModelSchedulePickupFragment
import com.csr.donor.utils.collectIn
import com.csr.donor.utils.getCollapseAnimation
import com.csr.donor.utils.getExpandAnimation
import com.csr.donor.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@AndroidEntryPoint
class PickupFragment : BaseFragment<FragmentPickupsBinding>(FragmentPickupsBinding::inflate) {

    private lateinit var adapterScrapItemWeight: AdapterScrapItemWeight
    private lateinit var adapterPickupTime: AdapterPickupTime
    private lateinit var adapterUserAddress: AdapterUserAddress

    private val viewModel: ViewModelSchedulePickupFragment by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterScrapItemWeight = AdapterScrapItemWeight(requireContext())
        adapterPickupTime = AdapterPickupTime(requireContext())
        adapterUserAddress = AdapterUserAddress(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnSchedulePickup.setOnClickListener {
                viewModel.schedulePickup()
            }
            rvWeightOptions.apply {
                adapter = adapterScrapItemWeight
                layoutManager =
                    GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            }
            rvScrapPickUptime.apply {
                adapter = adapterPickupTime
                layoutManager =
                    GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            }
            ivOpenWeightMenu.setOnClickListener {
                if (!binding.rvWeightOptions.isVisible) {
                    viewModel.openWeightChart()
                }
            }
            ivOpenDateMenu.setOnClickListener {
                if (!binding.calendarView.isVisible) {
                    viewModel.openDatePicker()
                }

            }
            ivOpenTimeMenu.setOnClickListener {
                if (!binding.rvScrapPickUptime.isVisible) {
                    viewModel.openTimePicker()
                }
            }
            ivOpenLocationMenu.setOnClickListener {
                if (!binding.clLocationHolder.root.isVisible) {
                    viewModel.openAddressPicker()
                }
            }
            clLocationHolder.btnAddAddress.setOnClickListener {
                findNavController().navigate(R.id.addAddressFragment)
            }
            clLocationHolder.rvPickupAddress.apply {
                adapter = adapterUserAddress
                layoutManager = LinearLayoutManager(requireContext(), GridLayoutManager.VERTICAL, false)
            }
            val currentDate = Date(System.currentTimeMillis())
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate = sdf.format(currentDate)
            binding.tvDate.text = formattedDate
            viewModel.pickupDate = formattedDate
            val maxDate = Calendar.getInstance()
            maxDate.add(Calendar.MONTH, 3) // Add 1 year to the current date

            // Set the minimum date to the current date
            binding.calendarView.minDate = currentDate.time
            // Set the maximum date to 1 year in the future
            binding.calendarView.maxDate = maxDate.timeInMillis
            // Optionally, you can set an event listener to handle date selection
            binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val selectedDate = Date(year - 1900, month, dayOfMonth)
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                val formattedDate = sdf.format(selectedDate)
                binding.tvDate.text = formattedDate
                viewModel.pickupDate = formattedDate
            }
        }

        viewModel.apply {
            getUserAddress()
            scrapItemWeightList.observe(viewLifecycleOwner) {
                it.find { it.isSelected }?.name?.let { selectedScrapWeight ->
                    binding.tvWeight.text = selectedScrapWeight
                }

                adapterScrapItemWeight.setScrapeItemList(it)
            }
            pickupTimeList.observe(viewLifecycleOwner) {
                it.find { it.isSelected }?.name?.let { selectedScrapWeight ->
                    binding.tvTime.text = selectedScrapWeight
                }

                adapterPickupTime.setPickupTimeList(it)
            }
            errorMessage.collectIn(viewLifecycleOwner) {
                requireActivity().showToast(it)
            }
            onOrderPlaced.collectIn(viewLifecycleOwner) {
                findNavController().navigate(R.id.bottomSheetDialogFragmentAddPickupOrderDetails)
            }
            expandedUiState.collectIn(viewLifecycleOwner) {
                when (it) {
                    ViewModelSchedulePickupFragment.UiToExpand.WEIGHT_CHART -> {
                        binding.apply {
                            rvWeightOptions.startAnimation(rvWeightOptions.getExpandAnimation())
                            calendarView.startAnimation(calendarView.getCollapseAnimation())
                            rvScrapPickUptime.startAnimation(rvScrapPickUptime.getCollapseAnimation())
                            clLocationHolder.root.startAnimation(clLocationHolder.root.getCollapseAnimation())
                        }
                    }

                    ViewModelSchedulePickupFragment.UiToExpand.DATE_PICKER -> {
                        binding.apply {
                            rvWeightOptions.startAnimation(rvWeightOptions.getCollapseAnimation())
                            calendarView.startAnimation(calendarView.getExpandAnimation())
                            rvScrapPickUptime.startAnimation(rvScrapPickUptime.getCollapseAnimation())
                            clLocationHolder.root.startAnimation(clLocationHolder.root.getCollapseAnimation())
                        }
                    }

                    ViewModelSchedulePickupFragment.UiToExpand.TIME_PICKER -> {
                        binding.apply {
                            rvWeightOptions.startAnimation(rvWeightOptions.getCollapseAnimation())
                            calendarView.startAnimation(calendarView.getCollapseAnimation())
                            rvScrapPickUptime.startAnimation(rvScrapPickUptime.getExpandAnimation())
                            clLocationHolder.root.startAnimation(clLocationHolder.root.getCollapseAnimation())
                        }
                    }

                    ViewModelSchedulePickupFragment.UiToExpand.ADDRESS_PICKER -> {
                        binding.apply {
                            rvWeightOptions.startAnimation(rvWeightOptions.getCollapseAnimation())
                            calendarView.startAnimation(calendarView.getCollapseAnimation())
                            rvScrapPickUptime.startAnimation(rvScrapPickUptime.getCollapseAnimation())
                            clLocationHolder.root.startAnimation(clLocationHolder.root.getExpandAnimation())
                        }
                    }

                    ViewModelSchedulePickupFragment.UiToExpand.NONE -> {
                        binding.apply {
                            rvWeightOptions.startAnimation(rvWeightOptions.getCollapseAnimation())
                            calendarView.startAnimation(calendarView.getCollapseAnimation())
                            rvScrapPickUptime.startAnimation(rvScrapPickUptime.getCollapseAnimation())
                            clLocationHolder.root.startAnimation(clLocationHolder.root.getCollapseAnimation())
                        }
                    }
                }
            }
            userPickupAddressList.observe(viewLifecycleOwner) {
                binding.clLocationHolder.apply {
                    rvPickupAddress.isVisible = it.isNotEmpty()
                    tvNoAddressFound.isVisible = it.isEmpty()
                }
                adapterUserAddress.setPickupAddressList(it)
            }
            selectedPickupAddress.observe(viewLifecycleOwner) {
                binding.tvLocation.text = it.address
            }
        }

        adapterScrapItemWeight.setOnWeightSelected {
            viewModel.onWeightSelected(it)
        }
        adapterPickupTime.setOnPickupTimeSelected {
            viewModel.onPickupTimeSelected(it)
        }
        adapterUserAddress.setOnPickupAddressSelected {
            viewModel.onUserAddressIsSelected(it)
        }

    }

    private fun sendPushNotificationWhenPickupIsScheduled(date: String, time: String) {
        // Code to execute when the alarm triggers
        // Show a notification here
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "PICKUP_ORDER_CHANNEL"
            val channelName = "Pickup Order Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notificationBuilder =
            NotificationCompat.Builder(requireContext(), "PICKUP_ORDER_CHANNEL")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Your Pickup order is scheduled")
                .setContentText("You next Pickup order is Scheduled on ${date} on ${time}.Our Sales person will contact you")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)

        // Show the notification
        notificationManager.notify(0, notificationBuilder.build())
    }

}