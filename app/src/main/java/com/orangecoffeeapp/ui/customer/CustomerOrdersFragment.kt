package com.orangecoffeeapp.ui.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orangecoffeeapp.R
import com.orangecoffeeapp.databinding.FragmentCustomerOrdersBinding


class CustomerOrdersFragment : Fragment() {

    private lateinit var binding:FragmentCustomerOrdersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentCustomerOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }


}