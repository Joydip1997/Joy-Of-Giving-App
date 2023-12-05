package com.csr.beneficiary.data.Order

import com.csr.beneficiary.data.api.ApiInterface
import com.csr.beneficiary.data.PickupOrderDTO
import javax.inject.Inject

class BackendPickupOrderRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface
) : PickupOrderRepository {
    override suspend fun placeNewOrder(pickupOrderDTO: PickupOrderDTO): PickupOrderDTO? {
        return apiInterface.addPickupOrder(pickupOrderDTO).body()
    }

    override suspend fun fetchOrders(userId: String, orderStatus: Int): List<PickupOrderDTO> {
        return apiInterface.getOrdersByUserId(userId).body() ?: emptyList()
    }

    override suspend fun getNearByOrders(
        latitude: Double,
        longitude: Double,
        distance: Int,
        scrapTypes : List<String>
    ): List<PickupOrderDTO> {
        return apiInterface.getPickupOrdersNearBy(latitude,longitude,distance,scrapTypes).body() ?: emptyList()
    }

    override suspend fun getOrderById(orderId: String): PickupOrderDTO? {
        return apiInterface.getOrderById(orderId).body()
    }

}