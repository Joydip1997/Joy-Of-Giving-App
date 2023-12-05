package com.csr.auth.auth.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csr.common.data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User?)

    @Query("SELECT * FROM USER_TABLE")
    fun getUserAsFlow() : Flow<List<User>>

    @Query("SELECT * FROM USER_TABLE")
    suspend fun getUser() : List<User>




    @Query("DELETE FROM USER_TABLE")
    fun deleteUser()



}