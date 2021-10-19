package com.orangecoffeeapp.utils.admission

import com.google.common.truth.Truth.assertThat
import com.orangecoffeeapp.constants.ErrorMessage
import org.junit.Test

class LoginFormUtilsTest{
    /**
     * Unit Testing
     * I have 2 input
     * Email & Password
     * Check if the email is empty or not
     * Check if the password is empty or not
     * Check if the email email includes @
     * Check if the password has appropriate length
     * **/

    @Test
    fun`empty email return false`(){
        val result = LoginFormUtils.validateLoginForm("","123456789")
        assertThat(result).isEqualTo(ErrorMessage.ERROR_EMAIL_IS_EMPTY)
    }

    @Test
    fun`empty password return false`(){
        val result = LoginFormUtils.validateLoginForm("ahmedsafwat172@gmail.com","")
        assertThat(result).isEqualTo(ErrorMessage.ERROR_PASSWORD_IS_EMPTY)
    }

    @Test
    fun`invalid email return false`(){
        val result = LoginFormUtils.validateLoginForm("ahmedsafwat172gmail.com","123456789")
        assertThat(result).isEqualTo(ErrorMessage.ERROR_EMAIL_NOT_VALID_MSG)
    }

    @Test
    fun`password is less than 8 characters return false`(){
        val result = LoginFormUtils.validateLoginForm("ahmedsafwat172@gmail.com","1234")
        assertThat(result).isEqualTo(ErrorMessage.ERROR_PASSWORD_LENGTH_LESS_THAN_8)
    }

    @Test
    fun`valid password & email return true`(){
        val result = LoginFormUtils.validateLoginForm("ahmedsafwat172@gmail.com","123456789")
        assertThat(result).isEqualTo(ErrorMessage.NONE)
    }
}