package com.orangecoffeeapp.utils.admission

sealed class AdmissionState<out T> {
    //Success
    data class Success<out T>(val data:T): AdmissionState<T>()
    //Error
    data class Error(val e:String): AdmissionState<Nothing>()
    //Idle
    object Idle: AdmissionState<Nothing>()
    //Loading
    object Loading: AdmissionState<Nothing>()

    val extractData: T?
        get() = when (this) {
            is Success -> data
            is Error -> null
            else -> null // Loading
        }

}