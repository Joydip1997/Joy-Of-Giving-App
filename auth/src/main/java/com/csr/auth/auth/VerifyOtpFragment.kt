package com.csr.auth.auth

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.csr.auth.R
import com.csr.common.data.AppPrefs
import com.csr.auth.databinding.FragmentVerifyOtpBinding
import com.csr.auth.auth.viewModels.ViewModelAuth
import com.csr.common.base.BaseFragment
import com.csr.common.collectIn
import com.csr.common.showToast
import com.csr.otpview.OTPListener

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VerifyOtpFragment :
    BaseFragment<FragmentVerifyOtpBinding>(FragmentVerifyOtpBinding::inflate) {

    private lateinit var auth: FirebaseAuth
    private val viewModel: ViewModelAuth by activityViewModels()
    @Inject
    lateinit var appPrefs: AppPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            titleEnterOtp.text = "Enter Verification Code we sent to\n+91-${viewModel.phoneNumber}"
            otpView.otpListener = object : OTPListener {
                override fun onInteractionListener() {
                    val otpSize = otpView.otp?.length ?: 0

                    binding.btnVerifyOtp.apply {
                        isEnabled = otpSize == 6
                        alpha = if (otpSize == 6) 1f else 0.3f
                    }
                }

                override fun onOTPComplete(otp: String) {

                }

            }
            btnVerifyOtp.setOnClickListener {
                val credential = PhoneAuthProvider.getCredential(
                    viewModel.storedVerificationId,
                    otpView.otp ?: ""
                )
                signInWithPhoneAuthCredential(credential)
            }
        }
        viewModel.apply {
            isUserLoggedIn.collectIn(viewLifecycleOwner) {
                when(it){
                    ViewModelAuth.UserLoginType.NO_USER_FOUND -> {
                        findNavController().navigate(R.id.createProfileFragment)
                    }
                    ViewModelAuth.UserLoginType.BUYER -> {
                        toggleLoadingUi(false)
                        requireActivity().showToast("This mobile is registered as Seller! Please use new number")
                    }
                    ViewModelAuth.UserLoginType.SELLER -> {
//                        requireActivity().startActivity(
//                            Intent(
//                                requireContext(),
//                                MainActivity::class.java
//                            )
//                        )
//                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        toggleLoadingUi(true)
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information


                val user = task.result?.user
                viewModel.authId = user?.uid ?: ""
                appPrefs.authId = user?.uid
                viewModel.onLogin()
            } else {
                toggleLoadingUi(false)
                requireActivity().showToast("Wrong OTP! Please try again")
                // Sign in failed, display a message and update the UI
                // Update UI
            }
        }.addOnFailureListener {
            requireActivity().showToast("Wrong OTP! Please try again")
            toggleLoadingUi(false)
        }
    }

    private fun toggleLoadingUi(isLoading : Boolean){
        binding.apply {
            btnVerifyOtp.isVisible = !isLoading
            cpProgress.isVisible = isLoading
        }
    }
}