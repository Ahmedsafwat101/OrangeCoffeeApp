package com.orangecoffeeapp.utils.admission

import com.google.common.truth.Truth
import com.orangecoffeeapp.constants.ErrorMessage
import org.junit.Assert.*
import org.junit.Test

class EditFormUtilsTest{
    /**
     * Unit Testing
     * I have 4 input
     * fName & lName &Email & Password & Phone
     * Check if the fName is empty or not
     * Check if the lName is empty or not
     * Check if the password is empty or not
     * Check if the password has appropriate length
     * Check if the phone is valid
     **/

    @Test
    fun`empty fName return false`(){
        val result = EditFormUtils.validateEditForm("","Safwat","123456789","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_EMPTY_FNAME_MSG)
    }

    @Test
    fun`empty LName return false`(){
        val result = EditFormUtils.validateEditForm("Ahmed","","123456789","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_EMPTY_LNAME_MSG)
    }

    @Test
    fun`empty password return false`(){
        val result = EditFormUtils.validateEditForm("Ahmed","Safwat","","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_PASSWORD_IS_EMPTY)
    }

    @Test
    fun`password is less than 8 characters return false`(){
        val result = EditFormUtils.validateEditForm("Ahmed","Safwat","123456","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_PASSWORD_LENGTH_LESS_THAN_8)
    }

    @Test
    fun`phone is not valid return false`(){
        val result = EditFormUtils.validateEditForm("Ahmed","Safwat","123456789","")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_INVALID_PHONE_MSG)
    }


    @Test
    fun`valid fName & lName& & password & email & phone return true`(){
        val result = EditFormUtils.validateEditForm("Ahmed","Safwat","123456789","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.NONE)
    }




}