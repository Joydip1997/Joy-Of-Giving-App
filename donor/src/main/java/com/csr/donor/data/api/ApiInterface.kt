package com.csr.donor.data.api

import com.csr.donor.data.PickupOrderDTO
import com.csr.donor.data.geoCoding.ReverseGeoCodingDTO
import com.csr.donor.data.User
import com.csr.donor.data.UserDTO
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

    @GET("/pickup-orders-nearby")
    suspend fun getPickupOrdersNearBy(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude : Double,
        @Query("distance") distance : Int,
    ) : Response<List<PickupOrderDTO>>

    @GET("/pickup-orders-by-user-id")
    suspend fun getOrdersByUserId(@Query("userId") userId : String,@Query("orderStatus") orderStatus : Int = 0) : Response<List<PickupOrderDTO>>
}