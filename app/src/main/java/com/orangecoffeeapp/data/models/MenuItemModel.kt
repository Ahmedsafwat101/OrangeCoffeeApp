package com.orangecoffeeapp.data.models

import java.io.Serializable

data class MenuItemModel(
    var coffeeName: String ="",
    var available: Boolean = true,
    var price:Float = 0f,
    var ingredients: InventoryItemModel = InventoryItemModel(),
): Serializable
