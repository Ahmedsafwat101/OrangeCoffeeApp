package com.orangecoffeeapp.data.models

import java.io.Serializable

data class LinkedCarsWithOwners(
    var carModel: CarModel = CarModel(),
    var userModel: UserModel = UserModel()
):Serializable
