package com.csr.auth.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.csr.auth.databinding.FragmentCreateProfileBinding
import com.csr.auth.auth.viewModels.ViewModelAuth
import com.csr.common.base.BaseFragment
import com.csr.common.collectIn
import com.csr.common.showToast

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class CreateProfileFragment :
    BaseFragment<FragmentCreateProfileBinding>(FragmentCreateProfileBinding::inflate) {

    private val viewModel: ViewModelAuth by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            etUserMobile.setText(viewModel.phoneNumber)
            btnCreateAccount.setOnClickListener {
                val userFirstName = etUserFirstName.text.toString()
                val userLastName = etUserLastName.text.toString()
                val userEmail = etUserEmail.text.toString()
                if (userFirstName.isNotBlank() && userLastName.isNotBlank() && userEmail.isNotBlank()) {
                    viewModel.registerUser(userFirstName, userLastName, userEmail)
                    toggleLoadingUi(true)
                }else{
                    requireActivity().showToast("Please enter all the details")
                }
            }
        }

        viewModel.apply {
            isUserRegistered.collectIn(viewLifecycleOwner) {
               if(it){
                   lifecycleScope.launch {
                       withContext(Dispatchers.Main) {
                           binding.apply {
                               mainLayout.isVisible = false
                               llAccountCreatedAnimation.root.isVisible = true
                           }
                       }
                       delay(2.seconds)
                       withContext(Dispatchers.Main) {
                           toggleLoadingUi(false)
//                           requireActivity().finish()
//                           requireActivity().startActivity(
//                               Intent(
//                                   requireContext(),
//                                   MainActivity::class.java
//                               )
//                           )
                       }
                   }
               }else{
                   requireActivity().showToast("Please try again!")
               }
            }
        }


    }

    private fun toggleLoadingUi(isLoading : Boolean){
        binding.apply {
            btnCreateAccount.isVisible = !isLoading
            cpProgress.isVisible = isLoading
        }
    }



}