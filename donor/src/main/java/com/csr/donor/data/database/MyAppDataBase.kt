package com.csr.donor.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.csr.donor.data.database.dao.UserAddressDao
import com.csr.donor.data.database.dao.UserDao
import com.csr.donor.data.User
import com.csr.donor.data.user_address_model.UserAddress

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