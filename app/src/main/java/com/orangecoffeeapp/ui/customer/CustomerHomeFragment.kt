package com.orangecoffeeapp.ui.customer

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangecoffeeapp.R
import com.orangecoffeeapp.data.models.LinkedCarsWithOwners
import com.orangecoffeeapp.databinding.FragmentCustomerHomeBinding
import com.orangecoffeeapp.ui.adapters.CustomerCarRecyclerAdapter
import com.orangecoffeeapp.ui.viewmodels.LinkingViewModel
import com.orangecoffeeapp.utils.common.DataState
import com.orangecoffeeapp.utils.common.DisplayHelper
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import com.orangecoffeeapp.ui.viewmodels.LocationViewModel
import com.orangecoffeeapp.utils.customer.LocationComparator
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@AndroidEntryPoint
class CustomerHomeFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val TAG = "CustomerHomeFragment"
    private lateinit var linkedCarAdapter: CustomerCarRecyclerAdapter
    private val gpsViewModel: LocationViewModel by viewModels()
    private val linkingViewModel: LinkingViewModel by viewModels()
    private var linkedData: ArrayList<LinkedCarsWithOwners> = ArrayList()
    private lateinit var binding: FragmentCustomerHomeBinding
    private var myLastLocation: Location? = null
    private lateinit var locationManager: LocationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeObserverToLinkedData()
        subscribeObserverToGPS()
        gpsViewModel.checkStatus(locationManager, fusedLocationClient)
    }

    private fun subscribeObserverToGPS() {
        gpsViewModel.getGPSStatus().observe(viewLifecycleOwner, { result ->
            if (result) {
                linkingViewModel.getAllLinkedData()
            } else {
                buildAlertMessageNoGps()
            }
        })
    }

    private fun subscribeObserverToLinkedData() {
        linkingViewModel.getLinkedState().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    Log.d(TAG, "data " + result.data)
                    DisplayHelper.displayProgressbar(false, binding.progressCircular)
                    linkedData = ArrayList()
                    //getLocation()
                    sortCarsOnLocation(result.data)
                    linkedData.addAll(result.data)
                    initRecyclerView()


                }
                is DataState.Loading -> {
                    DisplayHelper.displayProgressbar(true, binding.progressCircular)

                }
                is DataState.Error -> {
                    DisplayHelper.displayProgressbar(false, binding.progressCircular)
                    DisplayHelper.displaySnack(result.e, binding.parent)
                    Log.d("here", "Error ${result.e}")

                }
                else -> {
                    Log.d(TAG, "")
                }
            }

        })

    }

    private fun initRecyclerView() {
        binding.CarsRecView.apply {
            this.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            linkedCarAdapter = CustomerCarRecyclerAdapter(::recyclerOnClick)
            this.adapter = linkedCarAdapter
        }
        addCarsDataSet()

    }

    private fun addCarsDataSet() {
        linkedCarAdapter.submitList(linkedData)
    }

    private fun recyclerOnClick(position: Int) {
        val selectedItem = linkedCarAdapter.getItem(position)
        val bundle = Bundle()
        bundle.putSerializable("currLinkedCar", selectedItem)
        findNavController().navigate(
            R.id.action_customerHomeFragment_to_customerMenuFragment,
            bundle
        )
    }


    private fun sortCarsOnLocation(carList: List<LinkedCarsWithOwners>):ArrayList<LinkedCarsWithOwners>{

        val locationObject: HashMap<Location, LinkedCarsWithOwners> = HashMap()
        for (item in carList) {
            var location = Location("")
            location.latitude = item.carModel.latitude
            location.longitude = item.carModel.longitude
            locationObject[location] = item
        }

        var sortedLocations = locationObject.keys.toList()

        myLastLocation = gpsViewModel.loc
        myLastLocation?.let {
            Collections.sort(sortedLocations, LocationComparator(myLastLocation!!));
        }

        val sortedCarList:ArrayList<LinkedCarsWithOwners> = ArrayList()
        for(sortedLocation in sortedLocations ){
            locationObject[sortedLocation]?.let { sortedCarList.add(it)}
        }

        return sortedCarList
    }




    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(R.string.gps_meg)
            .setCancelable(false)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    dialog.dismiss()
                })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    /*override fun onResume() {
        super.onResume()
        Log.d(TAG, "RESUME")
    }*/

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Start")
        gpsViewModel.checkStatus(locationManager, fusedLocationClient)
    }


}