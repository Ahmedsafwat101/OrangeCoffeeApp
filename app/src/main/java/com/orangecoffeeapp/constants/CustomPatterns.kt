package com.orangecoffeeapp.constants


object CustomREGEX {
    const val PHONE_REGEX = "^\\+([0-9\\-]?){9,11}[0-9]$"
    const val EMAIL_REGEX = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")"
}

