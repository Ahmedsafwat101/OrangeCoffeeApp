package com.orangecoffeeapp.utils.addcar

import com.google.common.truth.Truth
import com.orangecoffeeapp.constants.ErrorMessage
import org.junit.Assert.*
import org.junit.Test

class LinkFormValidationTest{


    @Test
    fun `empty ownerName return error`(){
        val result = LinkFormValidation.validateLinkForm("","Car1")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_NO_OWNER_SELECTED)
    }

    @Test
    fun `empty carName return error`(){
        val result = LinkFormValidation.validateLinkForm("Ahmed","")
        Truth.assertThat(result).isEqualTo(ErrorMessage.ERROR_NO_CAR_SELECTED)
    }

    @Test
    fun `valid carName return error`(){
        val result = LinkFormValidation.validateLinkForm("Ahmed","Car1")
        Truth.assertThat(result).isEqualTo(ErrorMessage.NONE)
    }

}