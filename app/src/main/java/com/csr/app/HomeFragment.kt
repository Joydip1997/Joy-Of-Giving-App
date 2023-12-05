package com.csr.app

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.csr.app.databinding.FragmentHomeBinding
import com.csr.common.base.BaseFragment


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().navigate(R.id.action_homeFragment_to_main_navigation_graph)
    }
}