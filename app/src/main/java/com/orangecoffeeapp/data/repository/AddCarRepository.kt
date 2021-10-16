package com.orangecoffeeapp.data.repository

import com.orangecoffeeapp.data.models.CarModel

interface AddCarRepository {
    suspend fun addCarToDB(car:CarModel):CarModel?
    suspend fun removeCarFromBB(car:CarModel):CarModel?
    suspend fun updateCarFromDB(car:CarModel):CarModel?
}