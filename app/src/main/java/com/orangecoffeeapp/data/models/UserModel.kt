package com.orangecoffeeapp.data.models

import java.io.Serializable


data class UserModel(
    var firstName:String = "",
    var lastName:String = "",
    var email: String= "",
    var password: String="",
    var phone: String= "",
    var type:String="",
    var carID:String="",
    var access:Boolean = true
):Serializable