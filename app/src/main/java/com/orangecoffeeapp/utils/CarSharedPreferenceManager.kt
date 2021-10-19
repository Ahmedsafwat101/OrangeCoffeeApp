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
            putFloat("latitude", data.latitude.toFloat())
            putFloat("longitude", data.longitude.toFloat())
            putBoolean("active", data.active)
            putBoolean("isTaken", data.isTaken)
        }.apply()
    }


    fun getSharedPreferenceData(): CarModel {
        return CarModel(
            carName = carSharedPref.getString("carName", null).toString(),
            address = carSharedPref.getString("carAddress", null).toString(),
            latitude = carSharedPref.getFloat("latitude", 0f).toDouble(),
            longitude = carSharedPref.getFloat("longitude", 0f).toDouble() ,
            active = carSharedPref.getBoolean("active", false),
            isTaken = carSharedPref.getBoolean("isTaken", false) ,
        )
    }


}