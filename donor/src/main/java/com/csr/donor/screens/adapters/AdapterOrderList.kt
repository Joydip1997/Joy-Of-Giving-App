package com.csr.donor.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csr.donor.data.PickupOrderDTO
import com.csr.donor.data.getOrderStatus
import com.csr.donor.databinding.LayoutPickupOrderItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterOrderList(private val context: Context) :
    RecyclerView.Adapter<AdapterOrderList.AdapterOrderListViewHolder>() {

    private var _pickupOrderList: List<PickupOrderDTO> = emptyList()

    fun setPickupOrderList(pickupOrderList: List<PickupOrderDTO>) {
        _pickupOrderList = pickupOrderList
        notifyDataSetChanged()
    }

    inner class AdapterOrderListViewHolder(private val binding: LayoutPickupOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pickupOrderDTO: PickupOrderDTO) {
            binding.apply {
                tvItemPickPlaceType.text = "HOME"
                tvOrderQuantity.text = "${pickupOrderDTO.orderEstimatedWeight} ${pickupOrderDTO.orderEstimatedWeightUnit ?: "KG"}"
                tvOrderStatus.text = getOrderStatus(pickupOrderDTO.orderStatus)
                val monthDatePair = parseDate(pickupOrderDTO.orderPickDate.toString())
                tvMonth.text = monthDatePair?.first
                tvDate.text = monthDatePair?.second.toString()
            }
        }
    }

    fun parseDate(dateString: String): Pair<String, Int>? {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date: Date?

        try {
            date = inputFormat.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
        val dayFormat = SimpleDateFormat("dd", Locale.getDefault())

        val month = monthFormat.format(date)
        val day = dayFormat.format(date).toInt()

        return Pair(month, day)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AdapterOrderListViewHolder(
        LayoutPickupOrderItemBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun getItemCount() = _pickupOrderList.size

    override fun onBindViewHolder(holder: AdapterOrderListViewHolder, position: Int) {
        val item = _pickupOrderList[position]

        holder.bind(item)
    }
}