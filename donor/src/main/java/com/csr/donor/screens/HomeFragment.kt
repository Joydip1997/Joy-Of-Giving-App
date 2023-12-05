package com.csr.donor.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.csr.donor.base.BaseFragment
import com.csr.donor.databinding.FragmentHomeBinding
import com.csr.donor.screens.adapters.AdapterHomeBanner
import com.csr.donor.screens.viewModels.ViewModelHomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private lateinit var adapterHomeBanner: AdapterHomeBanner
    private val viewModel: ViewModelHomeFragment by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapterHomeBanner = AdapterHomeBanner(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val snapHelper = PagerSnapHelper()
        binding.apply {
            tvMessage.isSelected = true
            vpAboutUs.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = adapterHomeBanner
                snapHelper.attachToRecyclerView(this)
            }
        }

        viewModel.apply {
            bannerImageList.observe(viewLifecycleOwner) {
                adapterHomeBanner.setBannerImages(it)
            }
        }

    }

}