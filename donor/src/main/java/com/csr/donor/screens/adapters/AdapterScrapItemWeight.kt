package com.csr.donor.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.csr.donor.R
import com.csr.donor.data.scrapModel.ScrapItemWeight
import com.csr.donor.databinding.LayoutScrapWeightItemBinding

class AdapterScrapItemWeight(private val context: Context) :
    RecyclerView.Adapter<AdapterScrapItemWeight.AdapterRateItemViewHolder>() {

    private var _itemWeightList: List<ScrapItemWeight> = emptyList()

    fun setScrapeItemList(itemWeightList: List<ScrapItemWeight>) {
        _itemWeightList = itemWeightList
        notifyDataSetChanged()
    }


    private var onWeightSelected: ((id: String) -> Any?)? = null

    fun setOnWeightSelected(onWeightSelected: ((id: String) -> Any?)) {
        this.onWeightSelected = onWeightSelected
    }

    inner class AdapterRateItemViewHolder(private val binding: LayoutScrapWeightItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(scrapItemWeight: ScrapItemWeight) {
            binding.apply {
                val tintColor = if (scrapItemWeight.isSelected) {
                    ContextCompat.getColor(context, R.color.color_secondary)
                } else {
                    ContextCompat.getColor(context, R.color.white)
                }
                val textColor = if (scrapItemWeight.isSelected) {
                    ContextCompat.getColor(context, R.color.white)
                } else {
                    ContextCompat.getColor(context, R.color.color_text_primary)
                }
                parentLayout.setBackgroundColor(tintColor)
                tvWeightName.setTextColor(textColor)
                tvWeightName.text = scrapItemWeight.name
                parentLayout.setOnClickListener {
                    onWeightSelected?.invoke(scrapItemWeight.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AdapterRateItemViewHolder(
        LayoutScrapWeightItemBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun getItemCount() = _itemWeightList.size

    override fun onBindViewHolder(holder: AdapterRateItemViewHolder, position: Int) {
        val item = _itemWeightList[position]
        holder.bind(item)
    }
}