package com.csr.beneficiary.partner_screens.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csr.beneficiary.data.auth.AuthRepository
import com.csr.beneficiary.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelProfileFragment @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _onLogout: MutableSharedFlow<Unit> = MutableSharedFlow()
    val onLogout: SharedFlow<Unit> = _onLogout


    fun userDetails() = userRepository.getUserAsFlow()

    fun logOut() {
        viewModelScope.launch {
            authRepository.onLogOut()
            _onLogout.emit(Unit)
        }
    }
}