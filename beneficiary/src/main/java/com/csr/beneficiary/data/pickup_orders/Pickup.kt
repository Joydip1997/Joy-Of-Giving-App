package com.csr.beneficiary.data.pickup_orders

import com.csr.beneficiary.data.user_address_model.UserAddress


data class Pickup(
    val id: Int,
    val date: String,
    val pickupAddress: UserAddress,
    val status: Int,
)

enum class OrderStatus {
    SCHEDULED, COMPLETE
}
