package com.csr.auth.auth

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.csr.common.R
import com.csr.auth.databinding.FragmentLoginBinding
import com.csr.auth.auth.viewModels.ViewModelAuth
import com.csr.common.base.BaseFragment
import com.csr.common.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private lateinit var auth: FirebaseAuth
    private val viewModel: ViewModelAuth by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etUserMobileNumber.addTextChangedListener {
                val phoneNumberLength = it?.length ?: 0
                btnGetOtp.apply {
                    isEnabled = phoneNumberLength == 10
                    alpha = if (phoneNumberLength == 10) 1f else 0.3f
                }
            }
            btnGetOtp.setOnClickListener {
                val phoneNumber = etUserMobileNumber.text.toString()
                sendVerificationCode(phoneNumber)
            }
        }

    }

    private fun sendVerificationCode(phoneNumber: String) {
        viewModel.phoneNumber = phoneNumber
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91${phoneNumber}") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        toggleLoadingUi(true)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            findNavController().navigate(R.id.verifyOtpFragment)
            toggleLoadingUi(false)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            requireActivity().showToast("Please try again!")
            toggleLoadingUi(false)
        }
        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            // Save verification ID and resending token so we can use them later
            viewModel.storedVerificationId = verificationId
           // viewModel.resendToken = token
            findNavController().navigate(R.id.verifyOtpFragment)
            toggleLoadingUi(false)
        }


    }

    private fun toggleLoadingUi(isLoading : Boolean){
        binding.apply {
            btnGetOtp.isVisible = !isLoading
            cpProgress.isVisible = isLoading
        }
    }


}