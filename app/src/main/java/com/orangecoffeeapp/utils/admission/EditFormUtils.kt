package com.orangecoffeeapp.utils.admission

import com.orangecoffeeapp.constants.CustomREGEX
import com.orangecoffeeapp.constants.ErrorMessage
import java.util.regex.Pattern

object EditFormUtils {
    fun validateEditForm(fName:String,lName:String, password:String,phone:String):String {
        if(fName.isEmpty()) return ErrorMessage.ERROR_EMPTY_FNAME_MSG
        if(lName.isEmpty()) return ErrorMessage.ERROR_EMPTY_LNAME_MSG
        if (password.isEmpty()) return ErrorMessage.ERROR_PASSWORD_IS_EMPTY
        if (password.length < 8) return ErrorMessage.ERROR_PASSWORD_LENGTH_LESS_THAN_8
        if (!Pattern.compile(CustomREGEX.PHONE_REGEX).matcher(phone).matches()) return ErrorMessage.ERROR_INVALID_PHONE_MSG
        return ErrorMessage.NONE
    }
}