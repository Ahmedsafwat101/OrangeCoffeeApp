package com.orangecoffeeapp.data.repository

import com.orangecoffeeapp.data.models.Order

interface OrderRepository {
    suspend fun addOrder(currOrder:Order):Boolean
    suspend fun getOrdersForUser(userName:String):List<Order>?
    suspend fun getOrdersForCAR(carName:String):List<Order>?

}