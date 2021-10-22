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
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.InventoryItemModel
import com.orangecoffeeapp.data.models.MenuItemModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentMenuSetUpBinding
import com.orangecoffeeapp.ui.viewmodels.CarViewModel
import com.orangecoffeeapp.ui.viewmodels.LinkingViewModel
import com.orangecoffeeapp.utils.DataState
import com.orangecoffeeapp.utils.Helper

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuSetUpFragment : Fragment() {

    private  val TAG = "MenuSetUpFragment"
    private lateinit var menuBinding: FragmentMenuSetUpBinding
    private val linkingViewModel: LinkingViewModel by viewModels()

    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        menuBinding =  FragmentMenuSetUpBinding.inflate(inflater, container, false)
        return  menuBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currCar = arguments?.getSerializable("carObj") as CarModel
        val currOwner = arguments?.getSerializable("ownerObj") as UserModel


        subscribeObserver()


        menuBinding.addCoffeeBtn.setOnClickListener {

            val currMenuIngredients = InventoryItemModel(
                sugar = menuBinding.sugarQTxt.text.toString().toInt(),
                coffeeBeans = menuBinding.coffeeQTxt.text.toString().toInt(),
                milk = menuBinding.milkQTxt.text.toString().toInt(),
                water = menuBinding.waterQTxt.text.toString().toInt()
            )
            val currMenu = MenuItemModel(
                coffeeName = menuBinding.coffeeNameTxt.text.toString(),
                price = menuBinding.priceTxt.text.toString().toFloat(),
                ingredients = currMenuIngredients,
            )
            currCar.menuItems.add(currMenu)

            Log.d(TAG,"data "+ currCar.toString() + currCar.toString())

            linkingViewModel.updateLinkedData(currCar,currOwner,currMenu)
        }

    }



    private fun subscribeObserver() {
        linkingViewModel.getUpdateLinkedState().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    Helper.displayProgressbar(false, menuBinding.progressCircular)
                    Toast.makeText(requireActivity(),"Done",Toast.LENGTH_SHORT).show()
                }
                is DataState.Loading -> {
                    Helper.displayProgressbar(true, menuBinding.progressCircular)
                }
                is DataState.Error -> {
                    Helper.displayProgressbar(false, menuBinding.progressCircular)
                    Helper.displaySnack(result.e, menuBinding.parent)
                    Log.d("here", "Error ${result.e}")
                }
                else -> {
                    Log.d(TAG, "")
                }
            }
        })
    }








}