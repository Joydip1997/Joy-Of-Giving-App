package com.csr.donor.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.csr.donor.base.BaseFragment
import com.csr.donor.databinding.FragmentRatesBinding
import com.csr.donor.screens.adapters.AdapterRateItem
import com.csr.donor.screens.viewModels.ViewModelRatesFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RatesFragment : BaseFragment<FragmentRatesBinding>(FragmentRatesBinding::inflate) {

    private lateinit var adapter: AdapterRateItem

    private val viewModel: ViewModelRatesFragment by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = AdapterRateItem(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvScrapItem.apply {
                adapter = this@RatesFragment.adapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            svSearchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.onUserSearch(newText.toString())
                    return true
                }

            })
        }

        viewModel.apply {
            scrapItemList.observe(viewLifecycleOwner) {
                adapter.setScrapeItemList(it)
            }
        }


    }

}