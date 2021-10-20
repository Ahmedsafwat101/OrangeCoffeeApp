package com.orangecoffeeapp.data.repository

import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.LinkedCarsWithOwners
import com.orangecoffeeapp.data.models.UserModel

interface LinkingRepository {

    suspend fun getAllOwners():List<UserModel>?
    suspend fun getAllCars():List<CarModel>?
    suspend fun linkOwnerAndCar(owner:UserModel,car:CarModel):Boolean
    suspend fun getLinkedCarWithOwner():List<LinkedCarsWithOwners>?


}