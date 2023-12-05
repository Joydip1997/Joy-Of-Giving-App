package com.csr.donor.screens.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.csr.donor.data.user_address_model.UserAddress
import com.csr.donor.databinding.LayoutUserAddressItemBinding

class AdapterUserAddress(private val context: Context) :
    RecyclerView.Adapter<AdapterUserAddress.AdapterUserAddressItemViewHolder>() {

    private var _userAddress: List<UserAddress> = emptyList()

    fun setPickupAddressList(userAddress: List<UserAddress>) {
        _userAddress = userAddress
        notifyDataSetChanged()
    }


    private var onPickupAddressSelected: ((id: Int) -> Any?)? = null

    fun setOnPickupAddressSelected(onPickupAddressSelected: ((id: Int) -> Any?)) {
        this.onPickupAddressSelected = onPickupAddressSelected
    }

    inner class AdapterUserAddressItemViewHolder(private val binding: LayoutUserAddressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userAddress: UserAddress) {
            binding.apply {
                tvUserAddress.text = userAddress.address
                radioButton.isChecked = userAddress.isSelected
                radioButton.setOnClickListener {
                    onPickupAddressSelected?.invoke(userAddress.id)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AdapterUserAddressItemViewHolder(
            LayoutUserAddressItemBinding.inflate(LayoutInflater.from(context), parent, false)
        )

    override fun getItemCount() = _userAddress.size

    override fun onBindViewHolder(holder: AdapterUserAddressItemViewHolder, position: Int) {
        val item = _userAddress[position]
        holder.bind(item)
    }
}