package com.orangecoffeeapp.ui.admin

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orangecoffeeapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminAnalyticsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_analytics, container, false)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.updateProfile -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}