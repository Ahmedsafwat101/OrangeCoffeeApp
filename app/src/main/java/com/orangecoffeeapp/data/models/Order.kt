package com.orangecoffeeapp.data.models

data class Order(
    val orderID:String,
    val userID:String,
    val orderContent:String,
    val carName:String,
    val date:String
)