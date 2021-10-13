package com.orangecoffeeapp.data.repository

import com.orangecoffeeapp.data.models.LoginFormModel
import com.orangecoffeeapp.data.models.UserModel

interface AdmissionRepository {

    suspend fun validateUserLogin(user: LoginFormModel): UserModel?
    suspend fun validateUserSignIn(user: UserModel): UserModel?
    suspend fun updateUser(user: UserModel): UserModel?


}