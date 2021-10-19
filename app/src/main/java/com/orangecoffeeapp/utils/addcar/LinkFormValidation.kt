package com.orangecoffeeapp.utils.addcar

import com.orangecoffeeapp.constants.ErrorMessage.ERROR_NO_CAR_SELECTED
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_NO_OWNER_SELECTED
import com.orangecoffeeapp.constants.ErrorMessage.NONE

object LinkFormValidation {
    fun validateLinkForm( ownerName:String,carName:String):String{
        if(ownerName.isEmpty())
            return ERROR_NO_OWNER_SELECTED
        if(carName.isEmpty())
            return ERROR_NO_CAR_SELECTED
        return NONE
    }
}