package com.csr.beneficiary.data.location_repository

import com.csr.beneficiary.data.models.geoCoding.FullPlaceData

interface LocationRepository {

    suspend fun fetchAddressFromLatLng(
        latitude: Double,
        longitude: Double,
    ): FullPlaceData?
}