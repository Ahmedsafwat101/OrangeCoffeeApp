package com.orangecoffeeapp.data.models

import com.orangecoffeeapp.constants.OrdersStatus
import java.io.Serializable

data class Order(
    val userID:String ="",
    val orderPrice:Float=0f,
    val carName:String="",
    val date:String="",
    val orderContentKeys:List<MenuItemModel> = ArrayList(),
    val orderContentValues:List<Int> = ArrayList(),
    var orderStatus: OrdersStatus = OrdersStatus.Pending
    ):Serializable