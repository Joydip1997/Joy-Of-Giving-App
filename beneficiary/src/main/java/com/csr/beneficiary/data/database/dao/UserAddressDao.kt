package com.csr.beneficiary.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csr.beneficiary.data.user_address_model.UserAddress

@Dao
interface UserAddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(userAddress: UserAddress?)



    @Query("SELECT * FROM USER_ADDRESS_TABLE")
    suspend fun getAllAddress() : List<UserAddress>




}