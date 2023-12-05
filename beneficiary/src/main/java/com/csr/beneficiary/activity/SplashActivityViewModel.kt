package com.csr.beneficiary.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csr.beneficiary.routes.SplashScreenRoutes
import com.csr.common.data.AppPrefs
import com.csr.beneficiary.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SplashActivityViewModel @Inject constructor(
    private val repository: UserRepository,
    val appPrefs: AppPrefs
) : ViewModel() {

    private val _splashScreenRoute: MutableSharedFlow<SplashScreenRoutes> = MutableSharedFlow()
    val splashScreenRoute: SharedFlow<SplashScreenRoutes> = _splashScreenRoute.asSharedFlow()


    fun fetchUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(3.seconds)
            repository.getUserAsFlow().collect {users->
                val myUser = users?.firstOrNull()
                when (myUser?.userType) {
                    1 -> _splashScreenRoute.emit(
                        SplashScreenRoutes.GoToOnMainActivity
                    )
                    else -> _splashScreenRoute.emit(SplashScreenRoutes.GoToOnBoarding)
                }
            }
        }
    }



}