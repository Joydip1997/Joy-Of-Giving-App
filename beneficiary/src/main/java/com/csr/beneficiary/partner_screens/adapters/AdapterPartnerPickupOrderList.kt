package com.csr.beneficiary.partner_screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csr.beneficiary.data.PickupOrderDTO
import com.csr.beneficiary.databinding.LayoutPickupNearbyOrderItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterPartnerPickupOrderList(private val context: Context) :
    RecyclerView.Adapter<AdapterPartnerPickupOrderList.AdapterPartnerPickupOrderListViewHolder>() {

    private var _pickupOrderList: List<PickupOrderDTO> = emptyList()


    private var _onCallNowClicked: ((orderId: String) -> Any?)? = null

    fun setOnCallNowClicked(_onCallNowClicked: ((number: String) -> Any?)) {
        this._onCallNowClicked = _onCallNowClicked
    }


    fun setPickupOrderList(pickupOrderList: List<PickupOrderDTO>) {
        _pickupOrderList = pickupOrderList
        notifyDataSetChanged()
    }

    inner class AdapterPartnerPickupOrderListViewHolder(private val binding: LayoutPickupNearbyOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pickupOrderDTO: PickupOrderDTO) {
            binding.apply {
                tvOrderQuantity.text = "${pickupOrderDTO.orderEstimatedWeight} ${pickupOrderDTO.orderEstimatedWeightUnit}"
                tvItemPickPlaceType.text = "HOME"
                val monthDatePair = parseDate(pickupOrderDTO.orderPickDate.toString())
                tvMonth.text = monthDatePair?.first
                tvDate.text = monthDatePair?.second.toString()
                tvCallNow.setOnClickListener {
                    _onCallNowClicked?.invoke(pickupOrderDTO.orderId ?: "")
                }
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AdapterPartnerPickupOrderListViewHolder(
            LayoutPickupNearbyOrderItemBinding.inflate(LayoutInflater.from(context), parent, false)
        )

    override fun getItemCount() = _pickupOrderList.size

    override fun onBindViewHolder(holder: AdapterPartnerPickupOrderListViewHolder, position: Int) {
        val item = _pickupOrderList[position]

        holder.bind(item)
    }
}