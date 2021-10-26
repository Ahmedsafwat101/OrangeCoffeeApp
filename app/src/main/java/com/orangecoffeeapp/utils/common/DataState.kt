package com.orangecoffeeapp.utils.common

sealed class DataState<out T> {
    //Success
    data class Success<out T>(val data:T): DataState<T>()
    //Do Operation
    data class OperationWithFeedback <out T>(val data:T,var msg:String): DataState<T>()
    //Error
    data class Error(var e:String): DataState<Nothing>()
    //Loading
    object Loading: DataState<Nothing>()

}