package com.csr.auth.auth.data.api

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






}