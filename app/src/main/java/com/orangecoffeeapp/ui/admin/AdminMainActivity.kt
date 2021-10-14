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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminMainActivity : AppCompatActivity() {
    lateinit var navController:NavController
    lateinit var navHostFragment:NavHostFragment

    private lateinit var adminMainActivityBinding: ActivityAdminMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adminMainActivityBinding = ActivityAdminMainBinding.inflate(layoutInflater)
        val view = adminMainActivityBinding.root
        setContentView(view)


        val bottomNavigation = adminMainActivityBinding.adminBottomNavigationView
         navHostFragment = supportFragmentManager.findFragmentById(R.id.adminFragmentContainerView) as NavHostFragment
         navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.HomeFragment,R.id.addNewCarFragment,R.id.analyticsFragment)
        )
        setupActionBarWithNavController(navController,appBarConfiguration)

        bottomNavigation.setupWithNavController(navController)

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
                navController.navigate(R.id.action_HomeFragment_to_editUser)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }

}