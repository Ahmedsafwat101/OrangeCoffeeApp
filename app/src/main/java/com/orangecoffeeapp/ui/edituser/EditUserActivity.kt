package com.orangecoffeeapp.ui.edituser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.orangecoffeeapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()
    }
}