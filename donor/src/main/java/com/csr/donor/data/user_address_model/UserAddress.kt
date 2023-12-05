package com.csr.donor.data.user_address_model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USER_ADDRESS_TABLE")
@Keep
data class UserAddress(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val landmarkName: String? = null,
    val pincode: String? = null,
    val country: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    var isSelected: Boolean = false
)
@Keep
data class UserAddressDTO(
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val landmarkName: String? = null,
    val pincode: String? = null,
    val country: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
@Keep
fun UserAddress.toDTO() = UserAddressDTO(
    address, city, state, landmarkName, pincode, country, latitude, longitude
)
