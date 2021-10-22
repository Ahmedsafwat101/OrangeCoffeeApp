package com.orangecoffeeapp.data.repository

import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.MenuItemModel

interface AddCarRepository {
    suspend fun addCarMenu(car:CarModel):CarModel?
    suspend fun getCar(carNam:String):CarModel?
    suspend fun removeCar(car:CarModel):CarModel?
    suspend fun updateCarMenu(car:CarModel,menuItem:MenuItemModel):CarModel?
}