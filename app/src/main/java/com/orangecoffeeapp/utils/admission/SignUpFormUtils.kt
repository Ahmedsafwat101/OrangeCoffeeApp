package com.orangecoffeeapp.utils.admission

import com.orangecoffeeapp.constants.CustomREGEX.EMAIL_REGEX
import com.orangecoffeeapp.constants.CustomREGEX.PHONE_REGEX
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMAIL_IS_EMPTY
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMAIL_NOT_VALID_MSG
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMPTY_FNAME_MSG
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMPTY_LNAME_MSG
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_INVALID_PHONE_MSG
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_PASSWORD_IS_EMPTY
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_PASSWORD_LENGTH_LESS_THAN_8
import com.orangecoffeeapp.constants.ErrorMessage.NONE
import java.util.regex.Pattern

object SignUpFormUtils {
    fun validateSignUpForm(fName:String,lName:String,email:String, password:String,phone:String):String {
        if(fName.isEmpty()) return ERROR_EMPTY_FNAME_MSG
        if(lName.isEmpty()) return ERROR_EMPTY_LNAME_MSG
        if (email.isEmpty()) return ERROR_EMAIL_IS_EMPTY
        if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) return ERROR_EMAIL_NOT_VALID_MSG
        if (password.isEmpty()) return ERROR_PASSWORD_IS_EMPTY
        if (password.length < 8) return ERROR_PASSWORD_LENGTH_LESS_THAN_8
        if (!Pattern.compile(PHONE_REGEX).matcher(phone).matches()) return ERROR_INVALID_PHONE_MSG
        return NONE
    }
}