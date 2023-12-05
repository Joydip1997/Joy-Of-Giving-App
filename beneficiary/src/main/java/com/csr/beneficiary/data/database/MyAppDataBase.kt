package com.csr.beneficiary.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.csr.beneficiary.data.database.dao.UserAddressDao
import com.csr.beneficiary.data.database.dao.UserDao
import com.csr.common.data.User
import com.csr.beneficiary.data.user_address_model.UserAddress

@Database(
    entities = [
        User::class,
        UserAddress::class,
    ],
    version = 2,
    exportSchema = false
)

abstract class MyAppDataBase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun userAddressDao() : UserAddressDao

}