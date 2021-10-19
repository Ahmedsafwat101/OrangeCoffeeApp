package com.orangecoffeeapp.data.repository

import com.orangecoffeeapp.data.models.CarModel

interface AddCarRepository {
    suspend fun addCar(car:CarModel):CarModel?
    suspend fun removeCar(car:CarModel):CarModel?
    suspend fun updateCar(car:CarModel):CarModel?
}