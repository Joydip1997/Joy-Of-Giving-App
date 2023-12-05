package com.csr.donor.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csr.donor.data.scrapModel.ScrapItemCost
import com.csr.donor.databinding.LayoutScrapItemBinding

class AdapterRateItem(private val context: Context) :
    RecyclerView.Adapter<AdapterRateItem.AdapterRateItemViewHolder>() {

    private var _scrapeItemList: List<ScrapItemCost> = emptyList()

    fun setScrapeItemList(scrapeItemList: List<ScrapItemCost>) {
        _scrapeItemList = scrapeItemList
        notifyDataSetChanged()
    }

    inner class AdapterRateItemViewHolder(private val binding: LayoutScrapItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(scrapItemCost: ScrapItemCost) {
            binding.apply {
                Glide.with(context).load(scrapItemCost.icon).into(imageView4)
                tvItemName.text = scrapItemCost.name
                tvItemProposedPrice.text =
                    "RS ${scrapItemCost.price}/${scrapItemCost.itemQuantityType.name}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AdapterRateItemViewHolder(
        LayoutScrapItemBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun getItemCount() = _scrapeItemList.size

    override fun onBindViewHolder(holder: AdapterRateItemViewHolder, position: Int) {
        val item = _scrapeItemList[position]

        holder.bind(item)
    }
}