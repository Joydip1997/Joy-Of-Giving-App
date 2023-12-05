package com.csr.donor.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.csr.donor.base.BaseFragment
import com.csr.donor.databinding.FragmentOrdersBinding
import com.csr.donor.screens.adapters.AdapterOrderList
import com.csr.donor.screens.viewModels.ViewModelMyPickupFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderFragment : BaseFragment<FragmentOrdersBinding>(FragmentOrdersBinding::inflate) {

    private val viewModel: ViewModelMyPickupFragment by viewModels()
    private lateinit var adapter: AdapterOrderList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = AdapterOrderList(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvOrders.apply {
                adapter = this@OrderFragment.adapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    // Handle tab selection
                    tab?.let {
                        val position = it.position
                        // Your code based on the selected tab position
                        viewModel.apply {
                            orderStatus = position
                            fetchPickupOrders()
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // Handle tab unselection
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // Handle tab reselection
                }
            })
        }
        viewModel.apply {
            myPickupOrders.observe(viewLifecycleOwner) {
                adapter.setPickupOrderList(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPickupOrders()
    }
}