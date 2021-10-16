package com.orangecoffeeapp.utils.addcar

import com.google.android.gms.maps.model.LatLng
import com.orangecoffeeapp.constants.ErrorMessage

object AddCarFormUtils {
    fun validateAddCarForm(carName:String,address:String,location:LatLng):String{
        if(carName.isEmpty()) return ErrorMessage.Error_Car_NAME_IS_EMPTY
        if(address.isEmpty()) return ErrorMessage.Error_Car_Address_IS_EMPTY
        if(location.latitude==0.0 || location.latitude==0.0 ) return ErrorMessage.Error_Car_Location_IS_EMPTY
        return ErrorMessage.NONE
    }
}