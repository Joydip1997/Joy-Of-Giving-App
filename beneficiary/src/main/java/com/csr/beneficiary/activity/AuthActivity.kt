package com.csr.beneficiary.activity


import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.csr.beneficiary.R
import com.csr.common.data.AppPrefs
import com.csr.beneficiary.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {


    @Inject
    lateinit var appPrefs: AppPrefs
    private var _binding: ActivityAuthBinding? = null
    private val binding: ActivityAuthBinding get() = _binding!!
    private lateinit var alertDialog: AlertDialog

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_primary)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.auth_navigation_graph)

        if (appPrefs.isOnboardingAlreadyDonne) {
            graph.setStartDestination(R.id.loginFragment)
        }

        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)
    }


}