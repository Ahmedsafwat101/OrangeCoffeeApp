package com.orangecoffeeapp.utils.admission

import com.orangecoffeeapp.constants.CustomREGEX.EMAIL_REGEX
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMAIL_IS_EMPTY
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMAIL_NOT_VALID_MSG
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_PASSWORD_IS_EMPTY
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_PASSWORD_LENGTH_LESS_THAN_8
import com.orangecoffeeapp.constants.ErrorMessage.NONE
import java.util.regex.Pattern

object LoginFormUtils {
   fun validateLoginForm(email:String, password:String):String{
       if(email.isEmpty()) return ERROR_EMAIL_IS_EMPTY
       if(!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) return ERROR_EMAIL_NOT_VALID_MSG
       if(password.isEmpty()) return ERROR_PASSWORD_IS_EMPTY
       if(password.length<8) return ERROR_PASSWORD_LENGTH_LESS_THAN_8
       return NONE
   }
}