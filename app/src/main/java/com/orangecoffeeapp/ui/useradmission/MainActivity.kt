package com.orangecoffeeapp.ui.useradmission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import com.orangecoffeeapp.R
import com.orangecoffeeapp.utils.SharedPreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var sharedPref: SharedPreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()


         checkSharedPreference()
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