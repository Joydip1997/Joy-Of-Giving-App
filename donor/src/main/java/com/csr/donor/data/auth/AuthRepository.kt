package com.csr.donor.data.auth



import com.csr.donor.common.Resource
import com.csr.donor.data.User
import com.csr.donor.data.UserDTO

interface AuthRepository {
    suspend fun onRegister(userDTO : UserDTO) : Resource<User?>

    suspend fun getUser(userId : String) : User?

    suspend fun onLogOut()
}