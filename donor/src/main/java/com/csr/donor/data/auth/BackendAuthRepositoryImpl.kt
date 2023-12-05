package com.csr.donor.data.auth



import com.csr.donor.common.Resource
import com.csr.donor.data.api.ApiInterface
import com.csr.common.data.AppPrefs
import com.csr.donor.data.database.MyAppDataBase
import com.csr.donor.data.User
import com.csr.donor.data.UserDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackendAuthRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface,
    private val db: MyAppDataBase,
    private val appPrefs: AppPrefs
) : AuthRepository {
    override suspend fun onRegister(userDTO : UserDTO): Resource<User?> {
       return try {
            val response = apiInterface.addUser(userDTO)
            if(response.isSuccessful && response.body() != null){
                Resource.Success(response.body()!!)
            }else{
                Resource.Error(Exception(response.message()))
            }
        }catch (e : Exception){
           Resource.Error(e)
        }

    }

    override suspend fun getUser(userId: String): User? {
        val response = apiInterface.getUserById(userId)
        return response.body()
    }


    override suspend fun onLogOut() {
        withContext(Dispatchers.IO) {
            db.clearAllTables()
            appPrefs.authId = null
            appPrefs.userId = null
        }
    }


}