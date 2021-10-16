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
class AddCarFragment : Fragment(), OnMapReadyCallback {
    private val TAG = "MapFragment"
    private lateinit var addCarBinding: FragmentAddCarBinding
    private val addCarViewModel: AddCarViewModel by viewModels()

    private var addressList: List<Address>? = null
    lateinit var map: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    private var currLatLng:LatLng = LatLng(0.0,0.0)


    lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        addCarBinding = FragmentAddCarBinding.inflate(inflater, container, false)

        getActionBar()?.title = "Add Car"

        searchView = addCarBinding.searchBar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(location: String?): Boolean {
                if (location == null || location == "") {
                    Toast.makeText(activity, "provide location", Toast.LENGTH_SHORT).show()
                } else {
                    val geoCoder = Geocoder(activity)
                    try {
                        addressList = geoCoder.getFromLocationName(location, 1)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    if (addressList == null || addressList!!.isEmpty()) {
                        Toast.makeText(
                            activity,
                            "Please rewrite the location correctly ;)",
                            Toast.LENGTH_SHORT
                        ).show()
                        return false
                    }
                    val address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    currLatLng = latLng
                    addCarBinding.addCarAddressTxt.setText(location)

                    Log.d("Lan", "Lang" + address.latitude + "," + address.longitude)

                    Log.i("map", "here")
                    val mapFragment =
                        childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                    mapFragment?.getMapAsync { googleMap ->
                        googleMap.clear()
                        googleMap.addMarker(
                            MarkerOptions().position(latLng).title(location).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                            )
                        )
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false;

            }
        })

        return addCarBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, " MAP onViewCreated")

        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        subscribeObserver()

        addCarBinding.addCarBtn.setOnClickListener {
            // If valid input return NONE
            it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }


            if (addCarViewModel.validateAddCarFields(
                    carName = addCarBinding.addCarNameTxt.text.toString(),
                    address = addCarBinding.addCarAddressTxt.text.toString(),
                    location = currLatLng
                )
            ){
                CarSharedPreferenceManager(requireActivity()).saveSharedPreferenceData(CarModel(
                    carName = addCarBinding.addCarNameTxt.text.toString(),
                    address = addCarBinding.addCarAddressTxt.text.toString(),
                    location = currLatLng
                    )
                )
                findNavController().navigate(R.id.action_addCarFragment_to_addInventoryFragment)
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


    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, " MAP oNonMapReadym")
        map = googleMap
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity(), R.raw.style_json))
        val Egypt = LatLng(30.0444196, 31.2357116)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Egypt, 15f))

        setMapClick(map)
        //setMapLongClick(map)
        setPoiClick(map)
        //setMapStyle(map)
        enableMyLocation()
    }


    // Called when user makes a long press gesture on the map.
    private fun setMapClick(map: GoogleMap) {
        Log.d(TAG, " MAP setMapClick")


        map.setOnMapClickListener { latLng ->
            currLatLng = latLng
            val snippet = makeSnippet(latLng)

            map.clear()

            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }

    }

    private fun makeSnippet(latLng: LatLng): String? {
        return String.format(
            Locale.getDefault(),
            "Lat: %1$.5f, Long: %2$.5f",
            latLng.latitude,
            latLng.longitude
        )
    }


    // Places a marker on the map and displays an info window that contains POI name.
    private fun setPoiClick(map: GoogleMap) {
        Log.d(TAG, " MAP setPoiClick")

        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker.showInfoWindow()
        }
    }

    // Checks that users have given permission
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Checks if users have given their location and sets location enabled if so.
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                /*fun onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

                }*/
                return
            }
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    // Callback for the result from requesting permissions.
    // This method is invoked for every call on requestPermissions(android.app.Activity, String[],
    // int).
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }


    private fun getActionBar(): ActionBar? {
        return (activity as AddCarActivity).supportActionBar
    }


}