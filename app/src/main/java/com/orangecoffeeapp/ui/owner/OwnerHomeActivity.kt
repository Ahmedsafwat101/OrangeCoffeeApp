package com.orangecoffeeapp.ui.owner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orangecoffeeapp.R
import com.orangecoffeeapp.databinding.ActivityOwnerMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnerHomeActivity : AppCompatActivity() {

    private lateinit var ownerBinding:ActivityOwnerMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ownerBinding = ActivityOwnerMainBinding.inflate(layoutInflater)
        val view = ownerBinding.root
        setContentView(view)


        bottomNavigation = ownerBinding.ownerBottomNavigationView
        navHostFragment = supportFragmentManager.findFragmentById(R.id.ownerFragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigation.itemIconTintList = null;
        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
             when (destination.id) {
                 R.id.menuSetUpFragment -> {
                     supportActionBar?.hide()
                     hideBottomNav()
                 }
                 else -> hideBottomNav()
             }
        }


    }

    private fun showBottomNav() {
        bottomNavigation.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottomNavigation.visibility = View.GONE
    }



}

