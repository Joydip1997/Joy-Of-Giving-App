package com.csr.donor.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.csr.donor.R
import com.csr.donor.databinding.BottomsheetPickupAddressMoreDetailsBinding
import com.csr.donor.screens.viewModels.ViewModelAddAddressFragment
import com.csr.donor.utils.collectIn
import com.csr.donor.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BottomSheetDialogFragmentAddPickupAddressDetails : BottomSheetDialogFragment() {

    private var _binding: BottomsheetPickupAddressMoreDetailsBinding? = null
    val binding: BottomsheetPickupAddressMoreDetailsBinding get() = _binding!!

    private val viewModel: ViewModelAddAddressFragment by activityViewModels()

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
        _binding = BottomsheetPickupAddressMoreDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {
            fullAddress.observe(viewLifecycleOwner) {
                binding.apply {
                    etPickupFullAddress.setText(it?.address ?: "")
                    etPickupAddressCity.setText(it?.cityOrVillage ?: "")


                    btnSaveAddress.setOnClickListener {
                        if(binding.etPickupFullAddress.text.isEmpty()){
                            binding.etPickupFullAddress.requestFocus()
                            requireActivity().showToast("Please enter your full address")
                            return@setOnClickListener
                        }
                        if(binding.etPickupAddressCity.text.isEmpty()){
                            binding.etPickupAddressCity.requestFocus()
                            requireActivity().showToast("Please enter your city")
                            return@setOnClickListener
                        }
                        if(binding.etPickupAddressPincode.text.isEmpty()){
                            binding.etPickupAddressPincode.requestFocus()
                            requireActivity().showToast("Please enter your pincode")
                            return@setOnClickListener
                        }
                        if(binding.etPickupAddressLandmark.text.isEmpty()){
                            binding.etPickupAddressLandmark.requestFocus()
                            requireActivity().showToast("Please enter your nearest landmark")
                            return@setOnClickListener
                        }
                        viewModel.apply {
                            setNewUserAddress(binding.etPickupFullAddress.text.toString())
                            setCityName(binding.etPickupAddressCity.text.toString())
                            setLandMarkName(binding.etPickupAddressLandmark.text.toString())
                            setPinCode(binding.etPickupAddressPincode.text.toString())
                        }
                        viewModel.addUserAddress()

                    }
                }
            }
            userAddressAdded.collectIn(viewLifecycleOwner) {
                findNavController().popBackStack(R.id.addAddressFragment, true)
            }
        }

    }

}