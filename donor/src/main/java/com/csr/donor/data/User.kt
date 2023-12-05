package com.csr.donor.data

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "USER_TABLE")
data class User(
    @PrimaryKey(autoGenerate = false)
    var authId: String = "",
    @SerializedName("_id")
    var userId: String = "",
    var firstName: String?=null,
    var lastName: String?=null,
    var displayName: String?=null,
    var mobileNumber: String?=null,
    var email: String?=null,
    var userType: Int = 0,
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