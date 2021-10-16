package com.orangecoffeeapp.data.repository

import com.google.android.gms.maps.model.LatLng
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.utils.Hashing

class FakeAddCarRepositoryTest:AddCarRepository {

    private var fakeDB = mutableMapOf<String, CarModel>(
        "Car1" to CarModel(
          carName = "Car1",
          address = "Cairo,Maddi",
          location = LatLng(29.9601561,31.2569138)
        )
    )

    override suspend fun addCarToDB(car: CarModel): CarModel? {
        val dbKey = car.carName
        if (!fakeDB.containsKey(dbKey)) {
            fakeDB[dbKey] = car
            return car
        }
        return null
    }

    override suspend fun removeCarFromBB(car: CarModel): CarModel? {
        TODO("Not yet implemented")
    }

    override suspend fun updateCarFromDB(car: CarModel): CarModel? {
        TODO("Not yet implemented")
    }
}