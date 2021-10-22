package com.orangecoffeeapp.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.R

@SuppressLint("ShowToast")
object Helper {

     fun displayProgressbar(isDisplayed: Boolean, progressBar:ProgressBar ) {
        progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

     fun displaySnack(error: String, parent: View) {
        Snackbar.make(parent, error, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(Color.RED)
            .show()


    }
}