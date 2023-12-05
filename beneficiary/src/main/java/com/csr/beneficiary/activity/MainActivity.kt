package com.csr.beneficiary.activity


import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.csr.beneficiary.R
import com.csr.beneficiary.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private lateinit var alertDialog: AlertDialog

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_primary)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
       binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.itemIconTintList = null
        binding.bottomNavigation.itemActiveIndicatorColor = null
    }

    private val destinationChangedListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
        // Handle destination changes here
        val destinationId = destination.id
        binding.bottomNavigation.isVisible = isAtBottomNavFragments(destinationId)
    }


    override fun onStart() {
        super.onStart()
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    override fun onStop() {
        super.onStop()
        navController.removeOnDestinationChangedListener(destinationChangedListener)
    }

    private fun isAtBottomNavFragments(id : Int): Boolean {
        val bottomNavFragments = listOf(R.id.partner_homeFragment,R.id.partner_profileFragment)
        return bottomNavFragments.contains(id)
    }



}