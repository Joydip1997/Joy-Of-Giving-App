package com.csr.donor.data

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.csr.donor.data.user_address_model.UserAddressDTO


@Keep
data class PickupOrderDTO(
    val userId: String? = null,
    var orderEstimatedWeight: Int? = null,
    val orderEstimatedWeightUnit: String? = "KG",
    val location: PickupLocation? = null,
    val orderPickDate: String? = null,
    val orderPickTime: String? = null,
    @SerializedName("buyerDetails")
    val buyer : User?= null,
    val pickupLocation: UserAddressDTO? = null,
    var scrapTypes : List<String> ?= null,
    var scrapImages : List<String> ?= null,
    val orderStatus: Int = 0
)
@Keep
data class PickupLocation(
    val type: String = "Point",
    val coordinates: List<Double> = emptyList()
)
@Keep
enum class PickupOrderStatus {
    SCHEDULED, COMPLETE
}
@Keep
fun getOrderStatus(statusCode: Int): String {
    return when (statusCode) {
        PickupOrderStatus.SCHEDULED.ordinal -> PickupOrderStatus.SCHEDULED.name
        PickupOrderStatus.COMPLETE.ordinal -> PickupOrderStatus.COMPLETE.name
        else -> "ERROR"
    }
}



