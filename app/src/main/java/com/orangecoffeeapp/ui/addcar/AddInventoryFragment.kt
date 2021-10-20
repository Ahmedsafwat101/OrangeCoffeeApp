package com.orangecoffeeapp.ui.addcar

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.InventoryItemModel
import com.orangecoffeeapp.utils.CarSharedPreferenceManager
import com.orangecoffeeapp.utils.admission.AdmissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_car.*
import kotlinx.android.synthetic.main.fragment_add_inventory.*
import android.view.Gravity
import android.view.View.GONE

import android.widget.FrameLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orangecoffeeapp.ui.admin.AdminMainActivity
import com.orangecoffeeapp.utils.admission.NavigateToActivity
import kotlinx.android.synthetic.main.activity_admin_main.view.*


@AndroidEntryPoint
class AddInventoryFragment : Fragment() {
    private val TAG = "AddInventoryFragment"

   private lateinit var currCar:CarModel

    private val addCarViewModel:AddCarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeObserver()
        currCar = CarSharedPreferenceManager(requireActivity()).getSharedPreferenceData()
        addInventoryBtn.setOnClickListener {
            it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }

            val args = this.arguments
            if (args != null) {

                val carName = args.getString("carName").toString()
                val carAddress = args.getString("carAddress").toString()
                val latitude = args.getDouble("latitude")
                val longitude = args.getDouble("longitude")

                addCarViewModel.addCar(
                    CarModel(
                        carName = carName,
                        address = carAddress,
                        latitude = latitude,
                        longitude=  longitude,
                        inventory = InventoryItemModel(
                            coffeeBeans = coffeeQTxt.text.toString().toInt(),
                            milk = milkQTxt.text.toString().toInt(),
                            sugar = sugarQTxt.text.toString().toInt(),
                            water = waterQTxt.text.toString().toInt()
                        )
                    )
                )
            }
        }
    }



    private fun subscribeObserver() {
        addCarViewModel.getCarStates().observe(viewLifecycleOwner, { result ->
            when (result){
                is AdmissionState.Success -> {
                    displayProgressbar(false)
                    DisplayToast("DONE!")
                    requireActivity().finish()
                    val intent = Intent( requireActivity(),AdminMainActivity::class.java)
                    startActivity(intent)
                    //dismiss()
                }
                is AdmissionState.Loading -> {
                    displayProgressbar(true)
                }
                is AdmissionState.Error -> {
                    displayProgressbar(false)
                    when (result.e) {
                        ErrorMessage.ERROR_INVENTORY_IS_EMPTY -> {
                            coffeeQTxt.error = result.e
                            waterQTxt.error = result.e
                            milkQTxt.error = result.e
                            sugarQTxt.error = result.e
                        }
                        ErrorMessage.ERROR_CANT_MAKE_COFFEE -> {
                            coffeeQTxt.error = result.e
                            waterQTxt.error = result.e                        }
                    }
                    DisplayToast(result.e)
                    Log.d("here", "Error ${result.e}")
                }
                else -> {
                    Log.d(TAG,"")
                }
            }
        })
    }


    private fun displayProgressbar(isDisplayed: Boolean) {
        Log.d(TAG,"PROGRESS IS wOEKING")
        progress_circularAddInventory.bringToFront()
        progress_circularAddInventory.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun DisplayToast(message: String) {
       Toast.makeText(requireActivity(),message,Toast.LENGTH_LONG).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

    }






}
