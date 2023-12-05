package com.csr.donor.data.location_repository

import com.csr.donor.data.geoCoding.FullPlaceData

interface LocationRepository {

    suspend fun fetchAddressFromLatLng(
        latitude: Double,
        longitude: Double,
    ): FullPlaceData?
}