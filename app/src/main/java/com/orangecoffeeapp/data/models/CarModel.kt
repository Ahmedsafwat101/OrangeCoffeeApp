package com.orangecoffeeapp.data.models

data class CarModel(
    var carID: String,
    var carName: String,
    var ownerID: String,
    var address: String,
    var location: Pair<Float, Float>,
    var menu: List<MenuModel>,
    var inventory: List<InventoryItemModel>,
    var active: Boolean
)