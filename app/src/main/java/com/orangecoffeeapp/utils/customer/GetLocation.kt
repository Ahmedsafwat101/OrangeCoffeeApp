package com.orangecoffeeapp.utils.customer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await

object GetLocation {
    suspend fun getLastKnownLocation(fusedLocationClient: FusedLocationProviderClient, context: Context):Location? {
       if (ActivityCompat.checkSelfPermission(
              context,
              Manifest.permission.ACCESS_FINE_LOCATION
          ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
               context,
              Manifest.permission.ACCESS_COARSE_LOCATION
          ) != PackageManager.PERMISSION_GRANTED
      ) {

          return fusedLocationClient.lastLocation.await()
       }
        return null
    }
}