package com.orangecoffeeapp.utils

import android.content.Context
import com.orangecoffeeapp.data.models.UserModel
import android.content.SharedPreferences


class UserSharedPreferenceManager(context: Context) {

    private val userSharedPref: SharedPreferences = context.getSharedPreferences("loginRPref", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = userSharedPref.edit()

    fun saveSharedPreferenceData(data: UserModel){
        editor.apply {
            putString("fName", data.firstName)
            putString("carID",data.carID)
            putString("lName", data.lastName)
            putString("email", data.email)
            putString("password", data.password)
            putString("phone", data.phone)
            putBoolean("access", data.access)
            putString("type", data.type)
            putBoolean("logged", true)
        }.apply()
    }


    fun getString(key:String): String {
        return userSharedPref.getString(key,"NA").toString()
    }

    fun getInt(key:String):Int{
        return userSharedPref.getInt(key,0).toInt()
    }

    fun getBoolean(key:String):Boolean{
        return userSharedPref.getBoolean(key,false) as Boolean
    }

    fun setSharedPreferenceData(key:String,value:String){
        editor.apply {
            putString(key, value)
        }.apply()
    }

    fun setSharedPreferenceData(key:String,value:Boolean){
        editor.apply {
            putBoolean(key, value)
        }.apply()
    }

    fun setSharedPreferenceData(key:String,value:Int){
        editor.apply {
            putInt(key, value)
        }.apply()
    }



     fun getSharedPreferenceData(): UserModel {
        return UserModel(
            firstName = userSharedPref.getString("fName", null).toString(),
            lastName = userSharedPref.getString("lName", null).toString(),
            email = userSharedPref.getString("email", null).toString(),
            phone = userSharedPref.getString("phone", null).toString(),
            carID=userSharedPref.getString("carID",null).toString(),
            access = userSharedPref.getBoolean("access", false),
            type = userSharedPref.getString("type", null).toString(),
            password = userSharedPref.getString("password", null).toString(),
        )
    }

     fun isLogged(): Boolean {
        return userSharedPref.getBoolean("logged", false)
    }



}