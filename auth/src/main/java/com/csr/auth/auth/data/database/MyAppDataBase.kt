package com.csr.auth.auth.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.csr.auth.auth.data.database.dao.UserDao
import com.csr.common.data.User

@Database(
    entities = [
        User::class
    ],
    version = 2,
    exportSchema = false
)

abstract class MyAppDataBase : RoomDatabase() {
    abstract fun userDao() : UserDao

}