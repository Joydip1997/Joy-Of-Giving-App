package com.csr.beneficiary.placeFilter

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.csr.beneficiary.data.scrapModel.ScrapItemCost
import com.csr.beneficiary.databinding.ActivityFilterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterActivity : AppCompatActivity() {

    private val viewModel: FilterActivityVideModel by viewModels()
    private var _binding: ActivityFilterBinding? = null
    private val binding: ActivityFilterBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.fetchScrapTypes()
        binding.apply {
            swipeRefreshLayout.isEnabled = false
            rvScrapeTypes.apply {

            }
            seekbarDistance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    binding.tvDistance.text = "$p1 Km"
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    viewModel.distance = seekbarDistance.progress
                }

            })
            btnSaveChanges.setOnClickListener {
                viewModel.applyFilters()
                lifecycleScope.launch {
                    MyApplication.onFilterChanged.emit(Unit)
                }
                finish()
            }
            tvReset.setOnClickListener {
                viewModel.resetFilters()
            }
            ivArrowBack.setOnClickListener { finish() }
        }

        viewModel.apply {
            scrapItemList.observe(this@FilterActivity){
                setScrapType(it)
            }
        }

    }

    private fun setScrapType(chipData: List<ScrapItemCost>) {
        binding.rvScrapeTypes.removeAllViews()
        for (scrapType in chipData) {
            val chip = Chip(this)
            chip.text = scrapType.name
            chip.chipIcon = ContextCompat.getDrawable(this,scrapType.icon)
            chip.tag = scrapType.id
            chip.isCheckable = true
            chip.isChecked = scrapType.isSelected
            binding.rvScrapeTypes.addView(chip)
            chip.id = View.generateViewId()
            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                // Handle chip selection change here
                viewModel.onScrapSelected(chip.tag as String)
            }
        }

        // Set a listener for chip selection change

    }

    override fun onResume() {
        super.onResume()
        val currentDistanceInPref = viewModel.appPrefs.distance
        binding.seekbarDistance.progress = currentDistanceInPref / 1000
        binding.tvDistance.text = "${currentDistanceInPref / 1000} Km"
    }

}