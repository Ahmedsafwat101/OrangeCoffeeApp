package com.orangecoffeeapp.data.models

data class MenuModel(
    var coffeeName: String,
    var capacity: Int,
    var available: Boolean,
    var price:Float,
    var inventoryUse: List<InventoryItemModel>,
)
