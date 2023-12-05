package com.csr.donor.data.geoCoding

import androidx.annotation.Keep
import com.csr.donor.data.geoCoding.Address

@Keep
data class ReverseGeoCodingDTO(
    val address: Address,
    val boundingbox: List<String>,
    val display_name: String,
    val lat: Double,
    val licence: String,
    val lon: Double,
    val osm_id: Int,
    val osm_type: String,
    val place_id: Int,
    val powered_by: String
)