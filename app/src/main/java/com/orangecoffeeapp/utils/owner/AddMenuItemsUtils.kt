package com.orangecoffeeapp.utils.owner

import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_ITEM_NAME_IS_EMPTY
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_ITEM_PRICE_NOT_VALID

object AddMenuItemsUtils {
    fun validateAddMenuItem(name:String,price:Float,coffee:Int,milk:Int,water:Int,sugar:Int):String{
        if(name.isEmpty()) return  ERROR_ITEM_NAME_IS_EMPTY
        if(price <= 0) return ERROR_ITEM_PRICE_NOT_VALID
        if(coffee == 0 && milk == 0 && water == 0 && sugar == 0)  return ErrorMessage.ERROR_INVENTORY_IS_EMPTY
        if(coffee == 0 &&  water == 0)  return ErrorMessage.ERROR_CANT_MAKE_COFFEE
        return ErrorMessage.NONE
    }
}