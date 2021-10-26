package com.orangecoffeeapp.data.repository

import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.MenuItemModel

class FakeAddCarRepositoryTest:AddCarRepository {

    private var fakeDB = mutableMapOf<String, CarModel>(
        "Car1" to CarModel(
          carName = "Car1",
          address = "Cairo,Maddi",
            latitude = 29.9601561,
            longitude = 31.2569138
        )
    )

    override suspend fun addCarMenu(car: CarModel): CarModel? {
        val dbKey = car.carName
        if (!fakeDB.containsKey(dbKey)) {
            fakeDB[dbKey] = car
            return car
        }
        return null
    }

    override suspend fun getCar(carNam: String): CarModel? {
        TODO("Not yet implemented")
    }

    override suspend fun removeCar(car: CarModel): CarModel? {
        TODO("Not yet implemented")
    }

    override suspend fun updateCarMenu(car: CarModel, menuItem: MenuItemModel): CarModel? {
        TODO("Not yet implemented")
    }

}