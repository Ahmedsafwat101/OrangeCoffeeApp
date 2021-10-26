package com.orangecoffeeapp.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.orangecoffeeapp.utils.customer.GetLocation.getLastKnownLocation
import com.orangecoffeeapp.utils.common.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(application: Application):AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    var loc: Location = Location("")
    private val gpsStatus = SingleLiveEvent<Boolean>()
    fun getGPSStatus() = gpsStatus



    @SuppressLint("MissingPermission")
    fun checkStatus(locationManager: LocationManager, fusedLocationClient: FusedLocationProviderClient) {
        val currStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (currStatus) {
            viewModelScope.launch {
                val res = getLastKnownLocation(fusedLocationClient,context)
                if(res!=null) {
                    Log.d("TAG", res.toString())
                    loc = res
                }
                gpsStatus.postValue(true)
            }
        } else {
            gpsStatus.postValue(false)

        }
    }
}