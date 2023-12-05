package com.csr.beneficiary.partner_screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.csr.beneficiary.BuildConfig
import com.csr.common.base.BaseFragment
import com.csr.beneficiary.databinding.FragmentProfileBinding
import com.csr.beneficiary.partner_screens.viewModels.ViewModelProfileFragment
import com.csr.beneficiary.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: ViewModelProfileFragment by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            userDetails().collectIn(viewLifecycleOwner) {
                val userDetails = it?.firstOrNull() ?: return@collectIn
                binding.apply {
                    tvUserName.text = userDetails.firstName + " " + userDetails.lastName
                    tvUserPhone.text = userDetails.mobileNumber
                }

            }
            onLogout.collectIn(viewLifecycleOwner) {

            }
        }
        binding.apply {
            llProfileAppInfo.apply {
                llReviewApp.setOnClickListener { startReviewFlow() }
                llPrivacyPolicy.setOnClickListener {  }
                llTermsAndConditions.setOnClickListener { }
               // llHelpAndSupport.setOnClickListener { (requireActivity() as MainActivity).fetchBillingForHelp()  }
            }
            btnLogOut.setOnClickListener {
                viewModel.logOut()
            }
        }
    }

    private fun startReviewFlow(){
        val manager = ReviewManagerFactory.create(requireContext())
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(requireActivity(), reviewInfo)
                flow.addOnCompleteListener { _ ->

                }
            } else {
                // There was some problem, log or handle the error code.
                @ReviewErrorCode val reviewErrorCode = (task.exception as ReviewException).errorCode
                Toast.makeText(requireContext(),"You already reviewed us :) Thank you",Toast.LENGTH_SHORT).show()
            }
        }
    }

}