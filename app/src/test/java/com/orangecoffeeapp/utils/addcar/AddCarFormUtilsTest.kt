package com.orangecoffeeapp.utils.addcar

import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth
import com.orangecoffeeapp.constants.ErrorMessage
import org.junit.Test

class AddCarFormUtilsTest{
    /**
     * Unit Testing
     * I have 3 input
     * carName & carAddress & carLocation on Map
     * Check if the carName is empty or not
     * Check if the carAddress is empty or not
     * Check if the carLocation is empty or not
     * All is good
     **/

    @Test
    fun`carName is empty return error`(){
        val result = AddCarFormUtils.validateAddCarForm("","Cairo,Maddi",1.0,1.0)
        Truth.assertThat(result).isEqualTo(ErrorMessage.Error_Car_NAME_IS_EMPTY)
    }

    @Test
    fun`carAddress is empty return error`(){
        val result = AddCarFormUtils.validateAddCarForm("Car1","", 1.0,1.0)
        Truth.assertThat(result).isEqualTo(ErrorMessage.Error_Car_Address_IS_EMPTY)
    }

    @Test
    fun`carLocation  is empty return error`(){
        val result = AddCarFormUtils.validateAddCarForm("Car1","Cairo,Maddi", 0.0,0.0)
        Truth.assertThat(result).isEqualTo(ErrorMessage.Error_Car_Location_IS_EMPTY)
    }

    @Test
    fun` input is valid return success`(){
        val result = AddCarFormUtils.validateAddCarForm("Car1","Cairo,Maddi", 1.0,1.0)
        Truth.assertThat(result).isEqualTo(ErrorMessage.NONE)
    }

}