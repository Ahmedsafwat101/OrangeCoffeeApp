package com.orangecoffeeapp.data.models


data class UserModel(
    var firstName:String = "",
    var lastName:String = "",
    var email: String= "",
    var password: String="",
    var phone: String= "",
    var type:String="",
    var access:Boolean = true
)