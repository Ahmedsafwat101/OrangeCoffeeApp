package com.orangecoffeeapp.ui.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orangecoffeeapp.R
import com.orangecoffeeapp.databinding.ActivityAdminMainBinding
import com.orangecoffeeapp.databinding.ActivityCustomerMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_customer_main.view.*

@AndroidEntryPoint
class CustomerHomeActivity : AppCompatActivity() {
   private lateinit var navController: NavController
   private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var customerActivityBinding:ActivityCustomerMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customerActivityBinding = ActivityCustomerMainBinding.inflate(layoutInflater)
        val view = customerActivityBinding.root
        setContentView(view)

        bottomNavigation = customerActivityBinding.customerBottomNavigationView
        navHostFragment = supportFragmentManager.findFragmentById(R.id.customerFragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigation.itemIconTintList = null;

        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
           /* when (destination.id) {
                R.id.mapFragment2 -> {
                    supportActionBar?.hide()
                    hideBottomNav()
                }
                R.id.addInventoryFragment2 -> {
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
                R.id.analyticsFragment -> {
                    supportActionBar?.hide()
                    showBottomNav()
                }
                R.id.addNewCarFragment -> {
                    supportActionBar?.hide()
                    showBottomNav()
                }
                else -> hideBottomNav()
            }*/
        }


    }

    private fun showBottomNav() {

        bottomNavigation.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        bottomNavigation.visibility = View.GONE
    }


}