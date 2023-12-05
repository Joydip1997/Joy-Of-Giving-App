package com.csr.auth.auth

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.csr.auth.R
import com.csr.auth.databinding.FragmentOnboardingBinding
import com.csr.common.base.BaseFragment
import com.csr.common.data.AppPrefs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment :
    BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {

    @Inject
    lateinit var appPrefs: AppPrefs
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            btnLetsStart.setOnClickListener {
                appPrefs.isOnboardingAlreadyDonne = true
                findNavController().navigate(R.id.loginFragment)
            }
        }

    }

}