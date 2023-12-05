package com.csr.donor.data.Order

import com.csr.donor.data.PickupOrderDTO

interface PickupOrderRepository {

    suspend fun placeNewOrder(pickupOrderDTO: PickupOrderDTO): PickupOrderDTO?

    suspend fun fetchOrders(userId: String, orderStatus: Int): List<PickupOrderDTO>

    suspend fun getNearByOrders(
        latitude: Double,
        longitude: Double,
        distance: Int
    ): List<PickupOrderDTO>
}