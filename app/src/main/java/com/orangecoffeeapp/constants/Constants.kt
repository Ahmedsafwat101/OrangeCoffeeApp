package com.orangecoffeeapp.constants

import com.orangecoffeeapp.BuildConfig


object Constants {
    const val SECRET_KEY =  BuildConfig.secretKey
    const val SALT = BuildConfig.salt
    const val IV = BuildConfig.iv
}