package com.csr.donor.data.user

import com.csr.donor.data.database.MyAppDataBase
import com.csr.donor.data.User
import com.csr.donor.data.user_address_model.UserAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val db: MyAppDataBase
) : UserRepository {
    override suspend fun addUser(user: User) {
        withContext(Dispatchers.IO){
            db.userDao().deleteUser()
            db.userDao().insertUser(user)
        }
    }

    override fun getUserAsFlow() = db.userDao().getUserAsFlow()
    override suspend fun getUser() = db.userDao().getUser()

    override suspend fun addNewAddress(userAddress: UserAddress) {
        withContext(Dispatchers.IO) {
            db.userAddressDao().insertAddress(userAddress)
        }
    }



    override suspend fun getAllAddress() = db.userAddressDao().getAllAddress()

}