package com.orangecoffeeapp.data.models

data class LinkedCarsWithOwners(
    var carModel: CarModel = CarModel(),
    var userModel: UserModel = UserModel()
)
