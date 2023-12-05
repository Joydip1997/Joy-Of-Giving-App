package com.csr.beneficiary.data.Order

import com.csr.beneficiary.data.PickupOrderDTO

interface PickupOrderRepository {

    suspend fun placeNewOrder(pickupOrderDTO: PickupOrderDTO): PickupOrderDTO?

    suspend fun fetchOrders(userId: String, orderStatus: Int): List<PickupOrderDTO>

    suspend fun getNearByOrders(
        latitude: Double,
        longitude: Double,
        distance: Int,
        scrapTypes : List<String>
    ): List<PickupOrderDTO>

    suspend fun getOrderById(
        orderId : String
    ): PickupOrderDTO?
}