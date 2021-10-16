package com.orangecoffeeapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng
import com.orangecoffeeapp.data.models.CarModel

class CarSharedPreferenceManager(context: Context) {
    private val carSharedPref: SharedPreferences = context.getSharedPreferences("carRPref", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = carSharedPref.edit()

    fun saveSharedPreferenceData(data: CarModel){
        editor.apply {
            putString("carName", data.carName)
            putString("carAddress", data.address)
            putFloat("latitude", data.location.latitude.toFloat())
            putFloat("longitude", data.location.longitude.toFloat())
            putBoolean("active", data.active)
            putBoolean("isTaken", data.isTaken)
        }.apply()
    }


    fun getSharedPreferenceData(): CarModel {
        return CarModel(
            carName = carSharedPref.getString("carName", null).toString(),
            address = carSharedPref.getString("carAddress", null).toString(),
            location = LatLng( carSharedPref.getFloat("latitude", 0f).toDouble(), carSharedPref.getFloat("longitude", 0f).toDouble() ),
            active = carSharedPref.getBoolean("active", false),
            isTaken = carSharedPref.getBoolean("isTaken", false) ,
        )
    }


}