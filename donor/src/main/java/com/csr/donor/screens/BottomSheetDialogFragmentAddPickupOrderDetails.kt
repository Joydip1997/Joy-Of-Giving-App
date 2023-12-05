package com.csr.donor.screens


import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.csr.app.MyApplication
import com.csr.donor.data.scrapModel.ScrapItemCost
import com.csr.donor.databinding.BottomsheetPickupOrderMoreDetailsBinding
import com.csr.donor.screens.adapters.AdapterScrapImages
import com.csr.donor.screens.adapters.HorizontalItemDecoration
import com.csr.donor.screens.adapters.ScrapTypeSpinnerAdapter
import com.csr.donor.screens.viewModels.ViewModelAddPickupOrderDetailsFragment
import com.csr.donor.utils.collectIn
import com.csr.donor.utils.openAppSettings
import com.csr.donor.utils.safeInt
import com.csr.donor.utils.showToast
import com.csr.donor.worker.AddPickupOrderWorker
import com.csr.donor.worker.UploadWorker
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BottomSheetDialogFragmentAddPickupOrderDetails : BottomSheetDialogFragment() {

    private var _binding: BottomsheetPickupOrderMoreDetailsBinding? = null
    val binding: BottomsheetPickupOrderMoreDetailsBinding get() = _binding!!
    private lateinit var workManager: WorkManager
    private val viewModel: ViewModelAddPickupOrderDetailsFragment by viewModels()

    private lateinit var adapterScrapImage: AdapterScrapImages
    private lateinit var scrapTypeSpinnerAdapter: ScrapTypeSpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterScrapImage = AdapterScrapImages(requireContext())
        scrapTypeSpinnerAdapter = ScrapTypeSpinnerAdapter(requireContext())
        workManager = WorkManager.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            ?.let { bottomSheet ->
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        _binding = BottomsheetPickupOrderMoreDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvScrapImages.apply {
                addItemDecoration(HorizontalItemDecoration(requireContext()))
                adapter = adapterScrapImage
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
            spinnerScrapTypes.adapter = scrapTypeSpinnerAdapter
            ivPickImageFromGalary.setOnClickListener {
                requestGalleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            btnSchedulePickup.setOnClickListener {
                val scrapWeight = binding.etScrapWegiht.text.toString()
                binding.etScrapWegiht.error = null
                if (scrapWeight.isEmpty()) {
                    binding.etScrapWegiht.error = "Please enter your approx scrap weight"
                    binding.etScrapWegiht.requestFocus()
                    return@setOnClickListener
                }
                com.csr.app.MyApplication.pickupOrderDTO?.orderEstimatedWeight = binding.etScrapWegiht.text.safeInt()
                viewModel.schedulePickup()
            }
        }

        viewModel.apply {
            scrapImages.observe(viewLifecycleOwner) {
                adapterScrapImage.setScrapeImageList(it)
            }
            selectedScrapTypes.observe(viewLifecycleOwner) {
                setScrapType(it)
            }
            onOrderPlaced.collectIn(viewLifecycleOwner) {
                startUploadingPlaceData(it)
                requireActivity().showToast("Your Order Is Scheduling")
                findNavController().popBackStack()
            }
            scrapItemList.observe(viewLifecycleOwner) {
                scrapTypeSpinnerAdapter.setPickupAddressList(it)
            }
        }
        adapterScrapImage.setOnRemovePhotoClicked {
            viewModel.removeScrapImage(it)
        }
        scrapTypeSpinnerAdapter.setOnScrapTypeSelected {
            viewModel.onScrapSelected(it)
        }

    }

    private fun setScrapType(chipData: List<ScrapItemCost>) {
        binding.scrapTypes.removeAllViews()
        for (scrapType in chipData) {
            val chip = Chip(requireContext())
            chip.text = scrapType.name
            chip.tag = scrapType.id
            chip.isCheckable = true
            chip.isChecked = true
            binding.scrapTypes.addView(chip)
            chip.id = View.generateViewId()
            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                // Handle chip selection change here
                viewModel.onScrapUnSelected(scrapType.id)
            }
        }

        // Set a listener for chip selection change

    }


    private val requestGalleryPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                multiplePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            } else {
                requireContext().openAppSettings()
            }
        }

    private val multiplePhotoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
            if (it != null) {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                it.forEach { uri ->
                    context?.contentResolver?.takePersistableUriPermission(uri, flag)
                }
                viewModel.addImageFilesToList(it)
            }
        }

    private fun startUploadingPlaceData(imageUri: Array<String>) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Specify the required network type
            .build()

        val imageFileFolder = Data.Builder()
            .putString(UploadWorker.OUTPUT_NAME, "IMAGE_FILES")
            .putStringArray(UploadWorker.FILE_URI, imageUri)
            .putString(UploadWorker.FOLDER_NAME, "scrap_images")
            .build()




        val uploadImageFileWorkRequest =
            OneTimeWorkRequestBuilder<UploadWorker>().addTag(UploadWorker.TAG)
                .setInputData(imageFileFolder)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(constraints)
                .build()

        val addPickupOrderRequest =
            OneTimeWorkRequestBuilder<AddPickupOrderWorker>().setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(constraints)
                .build()
        workManager
            .beginWith(uploadImageFileWorkRequest)
            .then(addPickupOrderRequest)
            .enqueue()


    }



}