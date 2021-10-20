package com.orangecoffeeapp.ui.addcar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.constants.ErrorMessage.NONE
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.LinkedCarsWithOwners
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.data.repository.LinkingRepository
import com.orangecoffeeapp.utils.addcar.LinkFormValidation.validateLinkForm
import com.orangecoffeeapp.utils.admission.AdmissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LinkingViewModel @Inject constructor(private val repo: LinkingRepository) : ViewModel() {
    private val TAG = "GetOwnersViewModel"
    private val ownersState = MutableLiveData<AdmissionState<List<UserModel>>>()
    private val carsState = MutableLiveData<AdmissionState<List<CarModel>>>()
    private val linkingState = MutableLiveData<AdmissionState<Boolean>>()
    private val linkedState = MutableLiveData<AdmissionState<List<LinkedCarsWithOwners>>>()

    fun getOwnersState() = ownersState
    fun getCarsState() = carsState
    fun getLinkingState() = linkingState
    fun getLinkedState() = linkedState


    fun getAllOwners() {
        ownersState.postValue(AdmissionState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllOwners()
            if (response != null) {
                if (response.isEmpty())
                    ownersState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NO_OWNERS_FOUND))
                else
                    ownersState.postValue(AdmissionState.Success(response))
            } else {
                ownersState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
            }
        }
    }


    fun getAllCars() {
        carsState.postValue(AdmissionState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllCars()
            if (response != null) {
                if (response.isEmpty())
                    carsState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NO_CAR_FOUND))
                else
                    carsState.postValue(AdmissionState.Success(response))
            } else {
                carsState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
            }
        }
    }

    fun link(owner: UserModel, car: CarModel, ownerName: String, carName: String) {
        linkingState.postValue(AdmissionState.Loading)
        if (validateLinkingFields(ownerName, carName)) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.linkOwnerAndCar(owner, car)
                if (response) {
                    linkingState.postValue(AdmissionState.Success(response))
                } else {
                    linkingState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
                }
            }
        }
    }

    fun getAllLinkedData() {
        linkedState.postValue(AdmissionState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getLinkedCarWithOwner()
            if (response != null) {
                if (response.isEmpty())
                    linkedState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NO_LINKED_DATA_FOUND))
                else
                    linkedState.postValue(AdmissionState.Success(response))
            } else {
                linkedState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
            }
        }
    }



    private fun validateLinkingFields(ownerName: String, carName: String): Boolean {
        val result = validateLinkForm(ownerName, carName)
        if (result != NONE) {
            linkingState.postValue(AdmissionState.Error(result))
            return false
        }
        return true
    }

                                            /**Test**/
    fun getAllOwnerTest() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllOwners()
            if (response != null) {
                if (response.isEmpty())
                    ownersState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NO_OWNERS_FOUND))
                else
                    ownersState.postValue(AdmissionState.Success(response))
            } else {
                ownersState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
            }
        }
    }

    fun getAllOwnerTestReturnErrorNoOwners () {
        viewModelScope.launch(Dispatchers.IO) {
            val response = listOf<UserModel>()
            if (response.isEmpty())
                ownersState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NO_OWNERS_FOUND))
        }
    }

    fun getAllOwnerTestReturnNetworkError () {
        viewModelScope.launch(Dispatchers.IO) {
                ownersState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
        }
    }

    fun getAllCarsTest(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllCars()
            if (response != null) {
                if (response.isEmpty())
                    carsState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NO_CAR_FOUND))
                else
                    carsState.postValue(AdmissionState.Success(response))
            } else {
                carsState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
            }
        }
    }


    fun getAllCarsTestReturnErrorNoCars () {
        viewModelScope.launch(Dispatchers.IO) {
            val response = listOf<UserModel>()
            if (response.isEmpty())
                carsState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NO_CAR_FOUND))
        }
    }

    fun getAllCarsTestReturnNetworkError () {
        viewModelScope.launch(Dispatchers.IO) {
            carsState.postValue(AdmissionState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
        }
    }

    fun linkTest(owner: UserModel, car: CarModel){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.linkOwnerAndCar(owner, car)
            if (response) {
                linkingState.postValue(AdmissionState.Success(response))
            } else {
                linkingState.postValue(AdmissionState.Error(ErrorMessage.ERROR_OWNER_OR_CAR_NOT_EXISTS))
            }
        }
    }


}