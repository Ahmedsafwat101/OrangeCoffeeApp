package com.orangecoffeeapp.ui.useradmission

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangecoffeeapp.data.models.LoginFormModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.data.repository.AdmissionRepository
import com.orangecoffeeapp.utils.admission.AdmissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class AdmissionViewModel
@Inject
constructor(private val repo: AdmissionRepository)
    :ViewModel() {
    private  val TAG = "MyViewModel"
    private val user = MutableLiveData<AdmissionState<UserModel>>()

    fun getUser() =  user

    fun logIn(data:Any){
        user.postValue(AdmissionState.Loading)
        val currUser = data as LoginFormModel
        viewModelScope.launch(Dispatchers.IO) {
            val response =  repo.validateUserLogin(currUser)
            if(response!=null) {
                Log.d(TAG, "Debug " + response.lastName)
                if (response.password == currUser.password) {
                    user.postValue(AdmissionState.Success(response))
                }
            }else{
                user.postValue(AdmissionState.Error("Errror"))
            }
        }
    }

}



