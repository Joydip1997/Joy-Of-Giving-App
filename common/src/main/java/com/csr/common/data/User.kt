package com.csr.common.data

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "USER_TABLE")
data class User(
    @PrimaryKey(autoGenerate = false)
    val authId: String = "",
    @SerializedName("_id")
    val userId: String = "",
    val firstName: String?=null,
    val lastName: String?=null,
    val displayName: String?=null,
    val mobileNumber: String?=null,
    val email: String?=null,
    val userType: Int = 0,
)
@Keep
data class UserDTO(
    val authId: String = "",
    val firstName: String?=null,
    val lastName: String?=null,
    val displayName: String?=null,
    val mobileNumber: String?=null,
    val email: String?=null,
    val userType: Int = 0,
)

enum class UserType {
    BUYER, SELLER
}