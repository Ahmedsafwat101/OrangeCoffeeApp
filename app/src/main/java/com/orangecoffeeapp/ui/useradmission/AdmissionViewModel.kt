package com.orangecoffeeapp.ui.useradmission

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.constants.ErrorMessage.EMAIL_ALREADY_EXISTS_MSG
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_DONT_HAVE_ACCESS
import com.orangecoffeeapp.data.models.LoginFormModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.data.repository.AdmissionRepository
import com.orangecoffeeapp.utils.admission.AdmissionState
import com.orangecoffeeapp.utils.admission.LoginFormUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMAIL_NOT_FOUND_MSG
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_PASSWORD_WRONG_MSG
import com.orangecoffeeapp.utils.admission.EditFormUtils
import com.orangecoffeeapp.utils.admission.SignUpFormUtils


@HiltViewModel

class AdmissionViewModel
@Inject
constructor(private val repo: AdmissionRepository) : ViewModel() {
    private val TAG = "MyViewModel"
    private val user = MutableLiveData<AdmissionState<UserModel>>()

    fun getUser() = user

    fun logIn(data: Any) {
        user.postValue(AdmissionState.Loading)
        val currUser = data as LoginFormModel
        if (validateLoginFields(currUser.email, currUser.password)) {
             viewModelScope.launch(Dispatchers.IO) {
                val response = repo.validateUserLogin(currUser)
                if (response != null) {
                    Log.d(TAG, "Debug " + response.lastName)
                    if (response.password == currUser.password) {
                        user.postValue(AdmissionState.Success(response))
                    } else {
                        user.postValue(AdmissionState.Error(ERROR_PASSWORD_WRONG_MSG))
                    }
                } else {
                    user.postValue(AdmissionState.Error(ERROR_EMAIL_NOT_FOUND_MSG))
                }
            }
        }
    }

    fun signUp(data: Any) {
        user.postValue(AdmissionState.Loading)
        val currUser = data as UserModel
        if (validateSignUpFields(currUser.firstName, currUser.lastName, currUser.email, currUser.password, currUser.phone)) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.validateUserSignIn(currUser)
                if (response != null) {
                    user.postValue(AdmissionState.Success(response))
                    Log.d(TAG, "Add Done")
                }else {
                    user.postValue(AdmissionState.Error(EMAIL_ALREADY_EXISTS_MSG))
                }
            }
        }
    }

    fun update(data:Any){
        user.postValue(AdmissionState.Loading)
        val currUser = data as UserModel
        if (validateEditFields(currUser.firstName, currUser.lastName, currUser.password, currUser.phone)) {

            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.updateUser(currUser)
                if (response != null) {
                    if (response.access)
                        user.postValue(AdmissionState.Success(response))
                    else
                        user.postValue(AdmissionState.Error(ERROR_DONT_HAVE_ACCESS))
                } else {
                    user.postValue(AdmissionState.Error(EMAIL_ALREADY_EXISTS_MSG))
                }
            }
        }
    }

    private fun validateLoginFields(email: String, password: String): Boolean {
        val result = LoginFormUtils.validateLoginForm(email, password)
        if (result != ErrorMessage.NONE) {
            user.postValue(AdmissionState.Error(result))
            return false
        }
        return true
    }

    private fun validateSignUpFields(
        fName: String,
        lName: String,
        email: String,
        password: String,
        phone: String
    ): Boolean {
        val result = SignUpFormUtils.validateSignUpForm(fName, lName, email, password, phone)
        if (result != ErrorMessage.NONE) {
            user.postValue(AdmissionState.Error(result))
            return false
        }
        return true
    }

    private fun validateEditFields(
        fName: String,
        lName: String,
        password: String,
        phone: String):Boolean{
        val result = EditFormUtils.validateEditForm(fName, lName, password, phone)
        if (result != ErrorMessage.NONE) {
            user.postValue(AdmissionState.Error(result))
            return false
        }
        return true
    }




}



