package com.csr.beneficiary.activity


import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.csr.beneficiary.routes.SplashScreenRoutes
import com.csr.beneficiary.databinding.ActivitySplashBinding
import com.csr.beneficiary.utils.collectIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }
    private lateinit var appUpdateInfoTask: Task<AppUpdateInfo>
    private var _binding: ActivitySplashBinding? = null
    private val binding: ActivitySplashBinding get() = _binding!!

    private val viewModel: SplashActivityViewModel by viewModels()


    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                checkIfAppUpdateIsThere()
            } else {
                viewModel.fetchUserData()
            }
        }


    private fun checkIfAppUpdateIsThere() {
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activityResultLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
                )
            } else {
                viewModel.fetchUserData()
            }
        }
        appUpdateInfoTask.addOnFailureListener {
            viewModel.fetchUserData()
        }
    }

    override fun onResume() {
        super.onResume()
        checkIfAppUpdateIsThere()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appUpdateInfoTask = appUpdateManager.appUpdateInfo
        checkIfAppUpdateIsThere()
        viewModel.splashScreenRoute.collectIn(this) {
            when (it) {
                is SplashScreenRoutes.GoToOnMainActivity -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                SplashScreenRoutes.GoToOnBoarding -> {
                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                }

                else -> Unit
            }
        }
    }




}