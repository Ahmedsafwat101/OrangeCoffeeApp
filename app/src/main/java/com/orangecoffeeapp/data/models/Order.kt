package com.orangecoffeeapp.data.models

import java.io.Serializable

data class Order(
    val userID:String ="",
    val orderPrice:Float=0f,
    val carName:String="",
    val date:String="",
    val orderContentKeys:List<MenuItemModel> = ArrayList(),
    val orderContentValues:List<Int> = ArrayList(),

    ):Serializable