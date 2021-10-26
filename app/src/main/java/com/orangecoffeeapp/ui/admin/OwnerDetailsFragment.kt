package com.orangecoffeeapp.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orangecoffeeapp.R
import com.orangecoffeeapp.databinding.FragmentOwnerDetailsBinding


class OwnerDetailsFragment : BottomSheetDialogFragment() {


    private lateinit var ownerDetailsBinding:FragmentOwnerDetailsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        ownerDetailsBinding =  FragmentOwnerDetailsBinding.inflate(inflater, container, false)
        return ownerDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = this.arguments
        if (args != null) {
            ownerDetailsBinding.ownerNameTxt.setText(args.getString("name"))
            ownerDetailsBinding.ownerEmailTxt.setText(args.getString("email"))
            ownerDetailsBinding.ownerPhoneTxt.setText(args.getString("phone"))
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }


}