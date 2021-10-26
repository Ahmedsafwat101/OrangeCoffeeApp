package com.orangecoffeeapp.ui.owner

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.InventoryItemModel
import com.orangecoffeeapp.data.models.MenuItemModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentMenuSetUpBinding
import com.orangecoffeeapp.ui.viewmodels.LinkingViewModel
import com.orangecoffeeapp.utils.common.DataState
import com.orangecoffeeapp.utils.common.DisplayHelper

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuSetUpFragment : Fragment() {

    private val TAG = "MenuSetUpFragment"
    private lateinit var menuBinding: FragmentMenuSetUpBinding
    private val linkingViewModel: LinkingViewModel by viewModels()

    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        menuBinding = FragmentMenuSetUpBinding.inflate(inflater, container, false)
        return menuBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currCar = arguments?.getSerializable("carObj") as CarModel
        val currOwner = arguments?.getSerializable("ownerObj") as UserModel


        subscribeObserver()


        menuBinding.addCoffeeBtn.setOnClickListener {

            val sugarTxt = menuBinding.sugarQTxt.text.toString()
            val coffeeBeansTxt = menuBinding.coffeeQTxt.text.toString()
            val milkTxt = menuBinding.milkQTxt.text.toString()
            val waterTxt = menuBinding.waterQTxt.text.toString()
            val price = menuBinding.priceTxt.text.toString()

            val currMenuIngredients = InventoryItemModel(
                sugar = if (sugarTxt.isEmpty()) 0 else (sugarTxt.toInt()),
                coffeeBeans = if (coffeeBeansTxt.isEmpty()) 0 else (coffeeBeansTxt.toInt()),
                milk = if (milkTxt.isEmpty()) 0 else (milkTxt.toInt()),
                water = if (waterTxt.isEmpty()) 0 else (waterTxt.toInt())
            )


            val currMenu = MenuItemModel(
                coffeeName = menuBinding.coffeeNameTxt.text.toString(),
                price = if(price.isEmpty()) 0f else (price.toFloat()),
                ingredients = currMenuIngredients,
            )
            currCar.menuItems.add(currMenu)

            Log.d(TAG, "data " + currCar.toString() + currCar.toString())

            linkingViewModel.updateLinkedData(currCar, currOwner, currMenu)
        }

    }


    private fun subscribeObserver() {
        linkingViewModel.getUpdateLinkedState().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    DisplayHelper.displayProgressbar(false, menuBinding.progressCircular)
                    Toast.makeText(requireActivity(), "Done", Toast.LENGTH_SHORT).show()
                }
                is DataState.Loading -> {
                    DisplayHelper.displayProgressbar(true, menuBinding.progressCircular)
                }
                is DataState.Error -> {
                    DisplayHelper.displayProgressbar(false, menuBinding.progressCircular)
                    DisplayHelper.displaySnack(result.e, menuBinding.parent)
                    Log.d("here", "Error ${result.e}")
                }
                else -> {
                    Log.d(TAG, "")
                }
            }
        })
    }


}