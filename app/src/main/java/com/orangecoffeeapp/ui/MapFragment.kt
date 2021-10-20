package com.orangecoffeeapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.orangecoffeeapp.R
import com.orangecoffeeapp.databinding.FragmentMapBinding
import java.io.IOException
import java.util.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.orangecoffeeapp.ui.addcar.AddCarFragment


class MapFragment : Fragment(), OnMapReadyCallback {


    private lateinit var mapBinding: FragmentMapBinding
    private var addressList: List<Address>? = null
    lateinit var map: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    private var currLatLng: LatLng = LatLng(0.0, 0.0)




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mapBinding = FragmentMapBinding.inflate(inflater, container, false)
        return mapBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)


        mapBinding.mapFloatBtn.setOnClickListener{
            //go to inventory fragment

            val args = this.arguments
            val bundle= Bundle()
            if (args != null) {

                val carName = args.getString("carName")
                val carAddress = args.getString("carAddress")
                bundle.putString("carName",carName)
                bundle.putString("carAddress",carAddress)
                bundle.putDouble("latitude",currLatLng.latitude)
                bundle.putDouble("longitude",currLatLng.longitude)

                findNavController().navigate(R.id.action_mapFragment2_to_addInventoryFragment2,bundle)
            }
            else {
                it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }
                Toast.makeText(requireActivity(),"Please add Location !",Toast.LENGTH_SHORT).show()
            }




        }



        mapBinding.searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            var address: Address = Address(null)
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

                    if (addressList == null) {
                        Toast.makeText(
                            activity,
                            "Please rewrite the location correctly :)",
                            Toast.LENGTH_SHORT
                        ).show()
                        return false
                    }

                    addressList?.let {
                        address = it[0]
                    }

                    val latLng = LatLng(address.latitude, address.longitude)
                    currLatLng = latLng
                    // mapBinding.addCarAddressTxt.setText(location)

                    val mapFragment =
                        childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
                    mapFragment?.getMapAsync { googleMap ->
                        googleMap.clear()
                        mapBinding.mapFloatBtn.visibility = VISIBLE
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
    }

    override fun onMapReady(googleMap: GoogleMap) {
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
        map.setOnMapClickListener { latLng ->
            mapBinding.mapFloatBtn.visibility = VISIBLE
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

    }


}