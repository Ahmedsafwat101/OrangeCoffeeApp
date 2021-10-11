package com.orangecoffeeapp.ui.useradmission

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import com.orangecoffeeapp.R
import com.orangecoffeeapp.databinding.FragmentLogInBinding

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private val TAG = "LogInFragment"
  //  private var editor = loginSharedPref?.edit()

    private lateinit var logInBinding:FragmentLogInBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        logInBinding = FragmentLogInBinding.inflate(inflater, container, false)
        return logInBinding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {

        logInBinding.sigInTxt.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }
    }




}