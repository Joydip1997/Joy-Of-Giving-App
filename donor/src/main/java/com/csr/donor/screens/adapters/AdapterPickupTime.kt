package com.csr.donor.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.csr.donor.R
import com.csr.donor.data.scrapModel.PickupTime
import com.csr.donor.databinding.LayoutScrapWeightItemBinding

class AdapterPickupTime(private val context: Context) :
    RecyclerView.Adapter<AdapterPickupTime.AdapterRateItemViewHolder>() {

    private var _itemPickupTimeList: List<PickupTime> = emptyList()

    fun setPickupTimeList(itemPickupTimeList: List<PickupTime>) {
        _itemPickupTimeList = itemPickupTimeList
        notifyDataSetChanged()
    }


    private var onPickupTimeSelected: ((id: String) -> Any?)? = null

    fun setOnPickupTimeSelected(onPickupTimeSelected: ((id: String) -> Any?)) {
        this.onPickupTimeSelected = onPickupTimeSelected
    }

    inner class AdapterRateItemViewHolder(private val binding: LayoutScrapWeightItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pickupTime : PickupTime) {
            binding.apply {
                val tintColor = if (pickupTime.isSelected) {
                    ContextCompat.getColor(context, R.color.color_secondary)
                } else {
                    ContextCompat.getColor(context, R.color.white)
                }
                val textColor = if (pickupTime.isSelected) {
                    ContextCompat.getColor(context, R.color.white)
                } else {
                    ContextCompat.getColor(context, R.color.color_text_primary)
                }
                parentLayout.setBackgroundColor(tintColor)
                tvWeightName.setTextColor(textColor)
                tvWeightName.text = pickupTime.name
                parentLayout.setOnClickListener {
                    onPickupTimeSelected?.invoke(pickupTime.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AdapterRateItemViewHolder(
        LayoutScrapWeightItemBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun getItemCount() = _itemPickupTimeList.size

    override fun onBindViewHolder(holder: AdapterRateItemViewHolder, position: Int) {
        val item = _itemPickupTimeList[position]
        holder.bind(item)
    }
}