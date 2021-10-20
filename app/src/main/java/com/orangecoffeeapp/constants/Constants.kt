package com.orangecoffeeapp.constants

import com.orangecoffeeapp.BuildConfig


object Constants {
    const val SECRET_KEY =  BuildConfig.secretKey
    const val SALT = BuildConfig.salt
    const val IV = BuildConfig.iv
    const val CAR_IMG_URL = "https://firebasestorage.googleapis.com/v0/b/coffeeapp-35492.appspot.com/o/coffee-truck.png?alt=media&token=9e70599e-e433-472d-b875-09aa4f76dd67"
    const val OWNER_IMG_URL = "https://firebasestorage.googleapis.com/v0/b/coffeeapp-35492.appspot.com/o/waitress.png?alt=media&token=f950e4f3-67ab-4221-b4e8-6e40303cb017"
    const val LINKED_IMG_URL= "https://firebasestorage.googleapis.com/v0/b/coffeeapp-35492.appspot.com/o/linked.png?alt=media&token=f342b2aa-1fbf-4d87-b216-f4244e3b4ded"
}