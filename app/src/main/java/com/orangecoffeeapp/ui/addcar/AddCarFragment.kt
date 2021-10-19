package com.orangecoffeeapp.ui.addcar

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.databinding.FragmentAddCarBinding
import com.orangecoffeeapp.utils.CarSharedPreferenceManager
import com.orangecoffeeapp.utils.admission.AdmissionState
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class AddCarFragment : Fragment() {
    private val TAG = "MapFragment"
    private lateinit var addCarBinding: FragmentAddCarBinding
    private val addCarViewModel: AddCarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addCarBinding = FragmentAddCarBinding.inflate(inflater, container, false)

        getActionBar()?.title = "Add Car"

        return addCarBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObserver()



        addCarBinding.addCarBtn.setOnClickListener {
            // If valid input return NONE
            it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }


            if (addCarViewModel.validateAddCarFields(
                    carName = addCarBinding.addCarNameTxt.text.toString(),
                    address = addCarBinding.addCarAddressTxt.text.toString(),
                    latitude = 1.0,
                    longitude = 1.0
                )
            ) {
                val bundle= Bundle()
                bundle.putString("carName",addCarBinding.addCarNameTxt.text.toString())
                bundle.putString("carAddress", addCarBinding.addCarAddressTxt.text.toString())
                findNavController().navigate(R.id.action_addCarFragment_to_mapFragment,bundle)
            }

        }

    }


    private fun subscribeObserver() {
        addCarViewModel.getCarStates().observe(viewLifecycleOwner, { result ->
            when (result) {
                is AdmissionState.Error -> {
                    displayProgressbar(false)
                    when (result.e) {
                        ErrorMessage.Error_Car_NAME_IS_EMPTY -> {
                            addCarBinding.addCarNameTxt.error = result.e
                        }
                        ErrorMessage.Error_Car_Address_IS_EMPTY -> {
                            addCarBinding.addCarAddressTxt.error = result.e
                        }
                    }
                    displaySnackbar(result.e, R.color.Red_200)
                    Log.d("here", "Error ${result.e}")
                }
                else -> {
                    Log.d(TAG, "")
                }
            }
        })
    }

    private fun displayProgressbar(isDisplayed: Boolean) {
        addCarBinding.progressCircular.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun displaySnackbar(message: String, color: Int) {
        Snackbar.make(addCarBinding.parent, message, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(resources.getColor(color))
            .show()
    }


    private fun getActionBar(): ActionBar? {
        return (activity as AddCarActivity).supportActionBar
    }


}

//Move all logic in separate class  and fix the stack call