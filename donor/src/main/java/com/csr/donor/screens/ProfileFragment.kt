package com.csr.donor.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.play.core.review.ReviewException
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.csr.donor.BuildConfig
import com.csr.donor.activity.AuthActivity
import com.csr.donor.activity.WebViewActivity
import com.csr.donor.base.BaseFragment
import com.csr.donor.databinding.FragmentProfileBinding
import com.csr.donor.screens.viewModels.ViewModelProfileFragment
import com.csr.donor.utils.collectIn
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
                requireActivity().startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finish()
            }
        }
        binding.apply {
            llProfileAppInfo.apply {
                llReviewApp.setOnClickListener { startReviewFlow() }
                llPrivacyPolicy.setOnClickListener { WebViewActivity.goToWebViewActivity(requireContext(),"${BuildConfig.WEBPAGE_URL}privacy-policy") }
                llTermsAndConditions.setOnClickListener { WebViewActivity.goToWebViewActivity(requireContext(),"${BuildConfig.WEBPAGE_URL}terms-and-conditions")  }
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