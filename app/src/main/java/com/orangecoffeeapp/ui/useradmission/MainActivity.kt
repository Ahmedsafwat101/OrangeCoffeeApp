package com.orangecoffeeapp.ui.useradmission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orangecoffeeapp.R
import com.orangecoffeeapp.utils.SharedPreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var sharedPref: SharedPreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      // checkSharedPreference()
    }

    private fun checkSharedPreference(){
        sharedPref = SharedPreferenceManager(applicationContext)

        if (sharedPref.isLogged()) {
            val currUser = sharedPref.getSharedPreferenceData()
            startActivity(
                com.orangecoffeeapp.utils.admission.NavigateToActivity.moveToHomeActivity(
                    currUser.type,
                    this@MainActivity
                )
            )
        }
    }

}