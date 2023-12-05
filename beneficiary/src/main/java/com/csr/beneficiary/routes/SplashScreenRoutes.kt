package com.csr.beneficiary.routes

sealed class SplashScreenRoutes {
    object GoToOnBoarding : SplashScreenRoutes()
    object GoToOnMainActivity : SplashScreenRoutes()

    object Loading : SplashScreenRoutes()
    data class Error(val message : String) : SplashScreenRoutes()
}