package com.orangecoffeeapp.ui.addcar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.orangecoffeeapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

    }

    fun setActionBarTitle(title: String?) {
        actionBar?.title = title
    }
}