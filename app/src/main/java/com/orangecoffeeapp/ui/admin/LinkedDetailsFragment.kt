package com.orangecoffeeapp.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orangecoffeeapp.R
import com.orangecoffeeapp.databinding.FragmentLinkedDetailsBinding

class LinkedDetailsFragment : BottomSheetDialogFragment() {


    private lateinit var binding:FragmentLinkedDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentLinkedDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = this.arguments
        if (args != null) {
            binding.ownerNameTxt.setText(args.getString("ownerName"))
            binding.carNameTxt.setText(args.getString("carName"))
            binding.carAdressTxt.setText(args.getString("address"))
            binding.ownerEmailTxt.setText(args.getString("email"))
            binding.ownerPhoneTxt.setText(args.getString("phone"))
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }


}