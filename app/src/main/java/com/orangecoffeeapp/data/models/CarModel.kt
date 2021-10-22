package com.orangecoffeeapp.data.models

import java.io.Serializable


data class CarModel(
    var carID: String? ="",
    var carName: String ="",
    var ownerID: String ="",
    var address: String ="",
    var latitude:Double=0.0,
    var longitude:Double = 0.0,
    var menuItems: ArrayList<MenuItemModel> = ArrayList(),
    var inventory: InventoryItemModel = InventoryItemModel(),
    var active: Boolean = true,
    var isTaken:Boolean = false
):Serializable


