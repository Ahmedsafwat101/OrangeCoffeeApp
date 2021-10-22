package com.orangecoffeeapp.data.models

import java.io.Serializable

data class InventoryItemModel(
    var coffeeBeans:Int = 0,
    var milk:Int = 0,
    var sugar:Int = 0,
    var water:Int = 0
): Serializable
