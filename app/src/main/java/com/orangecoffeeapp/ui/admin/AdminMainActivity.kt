package com.orangecoffeeapp.ui.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.orangecoffeeapp.R
import com.orangecoffeeapp.databinding.ActivityAdminMainBinding
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminMainActivity : AppCompatActivity() {
    lateinit var navController:NavController
    lateinit var navHostFragment:NavHostFragment
    private lateinit var bottomNavigation:BottomNavigationView
   // private lateinit var navHostFragment

    private lateinit var adminMainActivityBinding: ActivityAdminMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adminMainActivityBinding = ActivityAdminMainBinding.inflate(layoutInflater)
        val view = adminMainActivityBinding.root
        setContentView(view)

         bottomNavigation = adminMainActivityBinding.adminBottomNavigationView
        navHostFragment = supportFragmentManager.findFragmentById(R.id.adminFragmentContainerView) as NavHostFragment
         navController = navHostFragment.navController

        bottomNavigation.itemIconTintList = null;

        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when (destination.id) {
                R.id.mapFragment2 -> {
                    supportActionBar?.hide()
                    hideBottomNav()
                }
                R.id.addInventoryFragment2 ->{
                    supportActionBar?.hide()
                    hideBottomNav()
                }
                R.id.HomeFragment -> {
                    supportActionBar?.show()
                    showBottomNav()
                }
                R.id.addCarFragment2 -> {
                    supportActionBar?.hide()
                    showBottomNav()
                }
                R.id.linkingFragment -> {
                    supportActionBar?.hide()
                    showBottomNav()
                }
                R.id.analyticsFragment ->{
                    supportActionBar?.hide()
                    showBottomNav()
                }
                R.id.addNewCarFragment ->{
                    supportActionBar?.hide()
                    showBottomNav()
                }
                else -> hideBottomNav()
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.HomeFragment,R.id.addNewCarFragment,R.id.analyticsFragment,R.id.linkingFragment,R.id.addCarFragment2)
        )

        setupActionBarWithNavController(navController,appBarConfiguration)
    }

    private fun showBottomNav() {

        bottomNavigation.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottomNavigation.visibility = View.GONE
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.updateProfile -> {
                //navController.navigate(R.id.action_global_editUser)
                false
            }

            else -> super.onOptionsItemSelected(item)
        }
    }




}