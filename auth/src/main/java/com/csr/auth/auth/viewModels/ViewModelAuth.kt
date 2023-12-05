package com.csr.auth.auth.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csr.auth.auth.data.auth.AuthRepository
import com.csr.auth.auth.data.repo.UserRepository
import com.csr.common.data.AppPrefs
import com.csr.common.data.UserDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelAuth @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val appPrefs: AppPrefs
) : ViewModel() {

    var phoneNumber: String = ""
    var storedVerificationId: String = ""
    var authId: String = ""

    private val _isUserLoggedIn: MutableSharedFlow<UserLoginType> = MutableSharedFlow()
    val isUserLoggedIn: SharedFlow<UserLoginType> = _isUserLoggedIn

    private val _isUserRegistered: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isUserRegistered: SharedFlow<Boolean> = _isUserRegistered

    enum class UserLoginType{
        NO_USER_FOUND,BUYER,SELLER
    }

    fun onLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = authRepository.getUser(authId)
            val userLoginType = if (user == null) {
                UserLoginType.NO_USER_FOUND
            } else if (user.userType == 0) {
                UserLoginType.BUYER
            } else {
                userRepository.addUser(user)
                appPrefs.authId = user.authId
                appPrefs.userId = user.userId
                UserLoginType.SELLER
            }
            _isUserLoggedIn.emit(userLoginType)

        }
    }

    fun registerUser(
        userFirstName : String,
        userLastName : String,
        userEmail : String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            val user = UserDTO(
                authId = authId,
                firstName = userFirstName,
                lastName = userLastName,
                displayName = "$userFirstName $userLastName",
                mobileNumber = phoneNumber,
                email = userEmail,
                userType = 1
            )
            val registeredUser = authRepository.onRegister(user)
            if (registeredUser != null) {
                appPrefs.authId = registeredUser.authId
                appPrefs.userId = registeredUser.userId
                userRepository.addUser(registeredUser)
            }
            _isUserRegistered.emit(registeredUser != null)
        }
    }
}