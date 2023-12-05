package com.csr.donor.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csr.donor.databinding.LayoutBannerItemBinding

class AdapterHomeBanner(private val context: Context) :
    RecyclerView.Adapter<AdapterHomeBanner.AdapterHomeBannerItemViewHolder>() {

    private var _itemBannerList: List<Int> = emptyList()

    fun setBannerImages(itemBannerList: List<Int>) {
        _itemBannerList = itemBannerList
        notifyDataSetChanged()
    }


    private var onPickupTimeSelected: ((id: String) -> Any?)? = null

    fun setOnPickupTimeSelected(onPickupTimeSelected: ((id: String) -> Any?)) {
        this.onPickupTimeSelected = onPickupTimeSelected
    }

    inner class AdapterHomeBannerItemViewHolder(private val binding: LayoutBannerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bannerSrc: Int) {
            binding.apply {
                Glide.with(context).load(bannerSrc).centerCrop().into(ivBanner)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AdapterHomeBannerItemViewHolder(
            LayoutBannerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        )

    override fun getItemCount() = _itemBannerList.size

    override fun onBindViewHolder(holder: AdapterHomeBannerItemViewHolder, position: Int) {
        val item = _itemBannerList[position]
        holder.bind(item)
    }
}