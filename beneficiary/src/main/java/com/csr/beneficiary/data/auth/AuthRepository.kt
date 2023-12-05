package com.csr.beneficiary.data.auth



import com.csr.common.data.User
import com.csr.common.data.UserDTO

interface AuthRepository {
    suspend fun onRegister(userDTO: UserDTO) : User?

    suspend fun getUser(userId : String) : User?

    suspend fun onLogOut()
}