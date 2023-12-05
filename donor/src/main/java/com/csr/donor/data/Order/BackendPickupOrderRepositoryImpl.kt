package com.csr.donor.data.Order

import com.csr.donor.data.PickupOrderDTO
import com.csr.donor.data.api.ApiInterface
import javax.inject.Inject

class BackendPickupOrderRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface
) : PickupOrderRepository {
    override suspend fun placeNewOrder(pickupOrderDTO: PickupOrderDTO): PickupOrderDTO? {
        return apiInterface.addPickupOrder(pickupOrderDTO).body()
    }

    override suspend fun fetchOrders(userId: String, orderStatus: Int): List<PickupOrderDTO> {
        return apiInterface.getOrdersByUserId(userId,orderStatus).body() ?: emptyList()
    }

    override suspend fun getNearByOrders(
        latitude: Double,
        longitude: Double,
        distance: Int
    ): List<PickupOrderDTO> {
        return apiInterface.getPickupOrdersNearBy(latitude,longitude,distance).body() ?: emptyList()
    }

}