package com.orangecoffeeapp.utils.admission

import com.google.common.truth.Truth
import com.orangecoffeeapp.constants.ErrorMessage
import org.junit.Assert.*
import org.junit.Test

class SignUpFormUtilsTest{
    /**
     * Unit Testing
     * I have 5 input
     * fName & lName &Email & Password & Phone
     * Check if the fName is empty or not
     * Check if the lName is empty or not
     * Check if the email is empty or not
     * Check if the password is empty or not
     * Check if the email email includes @
     * Check if the password has appropriate length
     * Check if the phone is valid
     **/


    @Test
    fun`empty fName return false`(){
        val result = SignUpFormUtils.validateSignUpForm("","Safwat","Ahmed@gamil.com","123456789","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_EMPTY_FNAME_MSG)
    }

    @Test
    fun`empty LName return false`(){
        val result = SignUpFormUtils.validateSignUpForm("Ahmed","","Ahmed@gamil.com","123456789","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_EMPTY_LNAME_MSG)
    }

    @Test
    fun`empty email return false`(){
        val result = SignUpFormUtils.validateSignUpForm("Ahmed","Safwat","","123456789","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_EMAIL_IS_EMPTY)
    }

    @Test
    fun`empty password return false`(){
        val result = SignUpFormUtils.validateSignUpForm("Ahmed","Safwat","Ahmed@gamil.com","","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_PASSWORD_IS_EMPTY)
    }

    @Test
    fun`invalid email return false`(){
        val result = SignUpFormUtils.validateSignUpForm("Ahmed","Safwat","Ahmedgamil.com","123456789","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_EMAIL_NOT_VALID_MSG)
    }

    @Test
    fun`password is less than 8 characters return false`(){
        val result = SignUpFormUtils.validateSignUpForm("Ahmed","Safwat","Ahmed@gamil.com","123456","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_PASSWORD_LENGTH_LESS_THAN_8)
    }

    @Test
    fun`phone is not valid return false`(){
        val result = SignUpFormUtils.validateSignUpForm("Ahmed","Safwat","Ahmed@gamil.com","123456789","")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_INVALID_PHONE_MSG)
    }


    @Test
    fun`valid fName & lName& & password & email & phone return true`(){
        val result = SignUpFormUtils.validateSignUpForm("Ahmed","Safwat","Ahmed@gamil.com","123456789","01126638220")
        Truth.assertThat(result).isEqualTo(ErrorMessage.NONE)
    }



}