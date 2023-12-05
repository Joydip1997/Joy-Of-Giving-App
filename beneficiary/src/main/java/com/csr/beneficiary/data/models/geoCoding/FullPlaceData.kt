package com.csr.beneficiary.data.models.geoCoding

import androidx.annotation.Keep

@Keep
data class FullPlaceData(
    val address: String?,
    val cityOrVillage: String?,
    val state: String?,
    val country: String?,
    val latitude: Double?,
    val longitude: Double?
)
@Keep
fun ReverseGeoCodingDTO.toUiModel() = FullPlaceData(
    address = display_name,
    latitude = lat,
    longitude = lon,
    cityOrVillage = address.city ?: address.village,
    country = address.country,
    state = address.state
)