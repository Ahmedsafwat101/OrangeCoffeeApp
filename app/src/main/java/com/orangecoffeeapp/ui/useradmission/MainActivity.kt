package com.orangecoffeeapp.ui.useradmission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import com.orangecoffeeapp.R
import com.orangecoffeeapp.utils.UserSharedPreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var userSharedPref: UserSharedPreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()


        checkSharedPreference()
    }

    private fun checkSharedPreference(){
        userSharedPref = UserSharedPreferenceManager(applicationContext)

        if (userSharedPref.isLogged()) {
            val currUser = userSharedPref.getSharedPreferenceData()
            startActivity(
                com.orangecoffeeapp.utils.admission.NavigateToActivity.moveToHomeActivity(
                    currUser.type,
                    this@MainActivity
                )
            )
        }
    }

}