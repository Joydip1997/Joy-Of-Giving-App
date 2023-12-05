package com.csr.beneficiary.data.location_repository

import com.csr.beneficiary.data.api.ApiInterface
import com.csr.beneficiary.data.models.geoCoding.FullPlaceData
import com.csr.beneficiary.data.models.geoCoding.toUiModel
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