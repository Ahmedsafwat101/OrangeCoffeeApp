package com.orangecoffeeapp.ui.addcar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orangecoffeeapp.R
import com.orangecoffeeapp.databinding.FragmentCarDetailsBinding

class CarDetailsFragment : BottomSheetDialogFragment() {

    private lateinit var carDetailsBinding: FragmentCarDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        carDetailsBinding = FragmentCarDetailsBinding.inflate(inflater, container, false)
        return carDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = this.arguments
        if (args != null) {
            carDetailsBinding.carNameTxt.setText(args.getString("name"))
            carDetailsBinding.carAdressTxt.setText(args.getString("address"))
        }

    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }



}