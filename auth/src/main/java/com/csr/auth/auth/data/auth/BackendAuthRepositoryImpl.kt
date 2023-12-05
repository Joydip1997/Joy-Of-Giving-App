package com.csr.auth.auth.data.auth


import com.csr.auth.auth.data.api.ApiInterface
import com.csr.auth.auth.data.database.MyAppDataBase
import com.csr.common.data.AppPrefs
import com.csr.common.data.User
import com.csr.common.data.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackendAuthRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val db: MyAppDataBase,
    private val appPrefs: AppPrefs
) : AuthRepository {
    override suspend fun onRegister(userDTO: UserDTO): User? {
        val response = apiInterface.addUser(userDTO)
        return response.body()
    }

    override suspend fun getUser(userId: String): User? {
        val response = apiInterface.getUserById(userId)
        return response.body()
    }


    override suspend fun onLogOut() {
        withContext(Dispatchers.IO) {
            db.clearAllTables()
            appPrefs.userId = null
            appPrefs.authId = null
        }
    }


}