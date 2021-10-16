package com.orangecoffeeapp.ui.addcar

import android.annotation.SuppressLint
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

import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.appcompat.app.AppCompatActivity





@AndroidEntryPoint
class AddInventoryFragment : BottomSheetDialogFragment() {
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

            addCarViewModel.addCar(CarModel(
                carName = currCar.carName,
                address = currCar.address,
                location = currCar.location,
                inventory = InventoryItemModel(
                    coffeeBeans = coffeeQTxt.text.toString().toInt(),
                    milk = milkQTxt.text.toString().toInt(),
                    sugar = sugarQTxt.text.toString().toInt(),
                    water = waterQTxt.text.toString().toInt()
                )
            ))
        }
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    private fun subscribeObserver() {
        addCarViewModel.getCarStates().observe(viewLifecycleOwner, { result ->
            when (result){
                is AdmissionState.Success -> {
                    displayProgressbar(false)
                    displaySnackbar("Car and Inventory are added Successfully! ", R.color.Green_300)
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
                    displaySnackbar(result.e, R.color.Red_200)
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

    @SuppressLint("ResourceAsColor")
    private fun displaySnackbar(message: String, color: Int) {
        val snack: Snackbar = Snackbar.make(parentAddInventory, message, Snackbar.LENGTH_LONG)
        val view = snack.view
        view.bringToFront()
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.topMargin = 15
        snack.setBackgroundTintMode(PorterDuff.Mode.LIGHTEN)
        snack.setBackgroundTint(R.color.white)
        snack.setAction("CLOSE") { }
        snack.show()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }



}
