package com.orangecoffeeapp.data.models


import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class CarModel(
    var carID: String? ="",
    var carName: String ="",
    var ownerID: String ="",
    var address: String ="",
    var location: LatLng = LatLng(0.0,0.0),
    var menu: List<MenuModel> = listOf(),
    var inventory: InventoryItemModel = InventoryItemModel(),
    var active: Boolean = true,
    var isTaken:Boolean = false
):Serializable
