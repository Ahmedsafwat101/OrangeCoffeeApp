package com.orangecoffeeapp.ui.useradmission

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.orangecoffeeapp.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    private val TAG = "SignUpFragment"
    private lateinit var signInBinding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signInBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        return signInBinding.root    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {

    }

    companion object {

    }
}