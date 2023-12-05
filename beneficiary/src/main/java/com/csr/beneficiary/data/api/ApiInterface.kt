package com.csr.beneficiary.data.api

import com.csr.beneficiary.data.PickupOrderDTO
import com.csr.beneficiary.data.models.geoCoding.ReverseGeoCodingDTO
import com.csr.common.data.User
import com.csr.common.data.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST("/add-user")
    suspend fun addUser(@Body userDTO: UserDTO) : Response<User>

    @GET("/login-user-by-id")
    suspend fun getUserById(@Query("authId") userId : String) : Response<User>


    @GET("reverse-geo-coding")
    suspend fun fetchAddressFromLatLng(
        @Query("latitude", encoded = true) latitude : Double,
        @Query("longitude", encoded = true) longitude : Double,
    ) : Response<ReverseGeoCodingDTO?>


    @POST("/add-pickup-order")
    suspend fun addPickupOrder(@Body pickupOrderDTO: PickupOrderDTO) : Response<PickupOrderDTO>

    @POST("/pickup-orders-nearby")
    suspend fun getPickupOrdersNearBy(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude : Double,
        @Query("distance") distance : Int,
        @Body scrapTypes : List<String>
    ) : Response<List<PickupOrderDTO>>


    @GET("/order-by-id")
    suspend fun getOrderById(
        @Query("orderId") orderId : String
    ) : Response<PickupOrderDTO>



    @GET("/pickup-orders-by-user-id")
    suspend fun getOrdersByUserId(@Query("userId") userId : String) : Response<List<PickupOrderDTO>>
}