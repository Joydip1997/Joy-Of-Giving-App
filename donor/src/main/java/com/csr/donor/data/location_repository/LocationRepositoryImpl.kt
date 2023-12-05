package com.csr.donor.data.location_repository

import com.csr.donor.data.api.ApiInterface
import com.csr.donor.data.geoCoding.FullPlaceData
import com.csr.donor.data.geoCoding.toUiModel
import com.csr.donor.data.location_repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface
) : LocationRepository {
    override suspend fun fetchAddressFromLatLng(
        latitude: Double,
        longitude: Double
    ): FullPlaceData? {
        return try {
            val response = apiInterface.fetchAddressFromLatLng(latitude, longitude)
            response.body()?.toUiModel()
        } catch (e: Exception) {
            null
        }
    }

}