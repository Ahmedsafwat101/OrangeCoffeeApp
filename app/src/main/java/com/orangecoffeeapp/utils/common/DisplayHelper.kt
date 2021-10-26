package com.orangecoffeeapp.utils.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.R

@SuppressLint("ShowToast")
object DisplayHelper {

     fun displayProgressbar(isDisplayed: Boolean, progressBar:ProgressBar ) {
        progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

     fun displaySnack(error: String, parent: View) {
        Snackbar.make(parent, error, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(Color.RED)
            .show()
    }

    fun displayToast(msg:String,context: Context){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }
}