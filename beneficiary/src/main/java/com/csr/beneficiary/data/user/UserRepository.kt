package com.csr.beneficiary.data.user

import com.csr.common.data.User
import com.csr.beneficiary.data.user_address_model.UserAddress
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun addUser(user: User)

    fun getUserAsFlow() : Flow<List<User>?>
    suspend fun getUser() : List<User>?
    suspend fun addNewAddress(userAddress: UserAddress)

    suspend fun getAllAddress() : List<UserAddress>
}