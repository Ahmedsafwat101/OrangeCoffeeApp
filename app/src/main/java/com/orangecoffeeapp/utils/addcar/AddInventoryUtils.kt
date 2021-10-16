package com.orangecoffeeapp.utils.addcar

import com.orangecoffeeapp.constants.ErrorMessage

object AddInventoryUtils {
    fun validateAddItemForm(coffee:Int,milk:Int,water:Int,sugar:Int):String{
        if(coffee == 0 && milk == 0 && water == 0 && sugar == 0)  return ErrorMessage.ERROR_INVENTORY_IS_EMPTY
        if(coffee == 0 &&  water == 0)  return ErrorMessage.ERROR_CANT_MAKE_COFFEE
        return ErrorMessage.NONE
    }
}