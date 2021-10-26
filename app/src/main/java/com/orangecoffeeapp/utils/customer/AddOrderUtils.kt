package com.orangecoffeeapp.utils.customer

import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMPTY_ORDER_CONTENT
import com.orangecoffeeapp.constants.ErrorMessage.NONE


object AddOrderUtils {
    fun validateOrder(orderContent: List<Int>):String{
        if(orderContent.isNotEmpty()) return ERROR_EMPTY_ORDER_CONTENT
        return NONE
    }
}