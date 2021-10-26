package com.orangecoffeeapp.ui.customer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orangecoffeeapp.R
import com.orangecoffeeapp.databinding.ActivityAdminMainBinding
import com.orangecoffeeapp.databinding.ActivityCustomerMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_customer_main.view.*
import android.content.pm.PackageManager


import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat

import android.app.Activity
import android.app.AlertDialog

import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.os.Handler
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import kotlinx.coroutines.runBlocking
import android.os.Looper
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.content.DialogInterface
import android.content.res.Resources
import android.util.Log
import com.akexorcist.localizationactivity.core.LocalizationActivityDelegate
import com.akexorcist.localizationactivity.core.OnLocaleChangedListener
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import java.util.*


@AndroidEntryPoint
class CustomerHomeActivity : AppCompatActivity(), OnLocaleChangedListener {
    private val TAG = "CustomerHomeActivity"
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var customerActivityBinding: ActivityCustomerMainBinding
    private val localizationDelegate = LocalizationActivityDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        localizationDelegate.addOnLocaleChangedListener(this)
        localizationDelegate.onCreate()
        super.onCreate(savedInstanceState)
        customerActivityBinding = ActivityCustomerMainBinding.inflate(layoutInflater)
        val view = customerActivityBinding.root
        setContentView(view)

        setLanguage("eng")
        Log.d(TAG,currentLanguage.toLanguageTag().toString())

        bottomNavigation = customerActivityBinding.customerBottomNavigationView
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.customerFragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigation.itemIconTintList = null;

        bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.checkOutFragment -> {
                    supportActionBar?.hide()
                    hideBottomNav()
                }
                R.id.navigationFragment -> {
                    supportActionBar?.hide()
                    hideBottomNav()
                }
                R.id.customerMenuFragment -> {
                    supportActionBar?.hide()
                    hideBottomNav()
                }
                R.id.customerHomeFragment -> {
                    supportActionBar?.show()
                    showBottomNav()
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

    //Localization

    public override fun onResume() {
        super.onResume()
        localizationDelegate.onResume(this)
    }

    override fun attachBaseContext(newBase: Context) {
        applyOverrideConfiguration(localizationDelegate.updateConfigurationLocale(newBase))
        super.attachBaseContext(newBase)
    }

    override fun getApplicationContext(): Context {
        return localizationDelegate.getApplicationContext(super.getApplicationContext())
    }

    override fun getResources(): Resources {
        return localizationDelegate.getResources(super.getResources())
    }

    fun setLanguage(language: String?) {
        localizationDelegate.setLanguage(this, language!!)
    }

    fun setLanguage(locale: Locale?) {
        localizationDelegate.setLanguage(this, locale!!)
    }

    val currentLanguage: Locale
        get() = localizationDelegate.getLanguage(this)

    // Just override method locale change event
    override fun onBeforeLocaleChanged() {}
    override fun onAfterLocaleChanged() {}
}

