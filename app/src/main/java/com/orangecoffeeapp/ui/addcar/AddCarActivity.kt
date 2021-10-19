package com.orangecoffeeapp.ui.addcar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.ActionBar
import androidx.viewbinding.ViewBinding
import com.orangecoffeeapp.R
import com.orangecoffeeapp.databinding.ActivityAddCarBinding
import com.orangecoffeeapp.ui.admin.AdminMainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

    }



    override fun onBackPressed() {
        this.finish()
        val intent = Intent(this, AdminMainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun setActionBarTitle(title: String?) {
        actionBar?.title = title
    }
}