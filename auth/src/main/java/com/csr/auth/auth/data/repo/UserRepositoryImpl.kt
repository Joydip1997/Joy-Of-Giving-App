package com.csr.auth.auth.data.repo

import com.csr.auth.auth.data.database.MyAppDataBase
import com.csr.common.data.User
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



}