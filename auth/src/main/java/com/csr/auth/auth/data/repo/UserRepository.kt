package com.csr.auth.auth.data.repo

import com.csr.common.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun addUser(user: User)

    fun getUserAsFlow() : Flow<List<User>?>
    suspend fun getUser() : List<User>?
}