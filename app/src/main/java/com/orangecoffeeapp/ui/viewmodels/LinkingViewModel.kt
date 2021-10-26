package com.orangecoffeeapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.constants.ErrorMessage.NONE
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.LinkedCarsWithOwners
import com.orangecoffeeapp.data.models.MenuItemModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.data.repository.LinkingRepository
import com.orangecoffeeapp.utils.addcar.LinkFormValidation.validateLinkForm
import com.orangecoffeeapp.utils.common.DataState
import com.orangecoffeeapp.utils.owner.AddMenuItemsUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LinkingViewModel @Inject constructor(private val repo: LinkingRepository) : ViewModel() {
    private val TAG = "GetOwnersViewModel"
    private val ownersState = MutableLiveData<DataState<List<UserModel>>>()
    private val carsState = MutableLiveData<DataState<List<CarModel>>>()
    private val linkingState = MutableLiveData<DataState<Boolean>>()
    private val linkedState = MutableLiveData<DataState<List<LinkedCarsWithOwners>>>()
    private val updateLinkingState = MutableLiveData<DataState<Boolean>>()

    fun getOwnersState() = ownersState
    fun getCarsState() = carsState
    fun getLinkingState() = linkingState
    fun getLinkedState() = linkedState
    fun getUpdateLinkedState() = updateLinkingState

    fun getAllOwners() {
        ownersState.postValue(DataState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllOwners()
            if (response != null) {
                if (response.isEmpty())
                    ownersState.postValue(DataState.Error(ErrorMessage.ERROR_NO_OWNERS_FOUND))
                else
                    ownersState.postValue(DataState.Success(response))
            } else {
                ownersState.postValue(DataState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
            }
        }
    }


    fun getAllCars() {
        carsState.postValue(DataState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllCars()
            if (response != null) {
                if (response.isEmpty())
                    carsState.postValue(DataState.Error(ErrorMessage.ERROR_NO_CAR_FOUND))
                else
                    carsState.postValue(DataState.Success(response))
            } else {
                carsState.postValue(DataState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
            }
        }
    }

    fun link(owner: UserModel, car: CarModel, ownerName: String, carName: String) {
        linkingState.postValue(DataState.Loading)
        if (validateLinkingFields(ownerName, carName)) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.linkOwnerAndCar(owner, car)
                if (response) {
                    linkingState.postValue(DataState.Success(response))
                } else {
                    linkingState.postValue(DataState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
                }
            }
        }
    }

    fun getAllLinkedData() {
        linkedState.postValue(DataState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getLinkedCarWithOwner()
            if (response != null) {
                if (response.isEmpty())
                    linkedState.postValue(DataState.Error(ErrorMessage.ERROR_NO_LINKED_DATA_FOUND))
                else
                    linkedState.postValue(DataState.Success(response))
            } else {
                linkedState.postValue(DataState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
            }
        }
    }


    fun updateLinkedData(currCar: CarModel, currOwner: UserModel, currMenuItems: MenuItemModel) {
        updateLinkingState.postValue(DataState.Loading)
        if (validateMenuItemFields(
                currMenuItems.coffeeName,
                currMenuItems.price,
                currMenuItems.ingredients.coffeeBeans,
                currMenuItems.ingredients.milk,
                currMenuItems.ingredients.water,
                currMenuItems.ingredients.sugar,
            )
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.updateLinkedCarWithOwner(currCar, currOwner)
                if (response) {
                    updateLinkingState.postValue(DataState.Success(response))
                } else {
                    updateLinkingState.postValue(DataState.Error(ErrorMessage.Error_UPDATE_NOT_AVAILABLE))
                }
            }
        }
    }


    private fun validateMenuItemFields(
        name: String,
        price: Float,
        coffee: Int,
        milk: Int,
        water: Int,
        sugar: Int
    ): Boolean {
        val result = AddMenuItemsUtils.validateAddMenuItem(name, price, coffee, milk, water, sugar)
        if (result != ErrorMessage.NONE) {
            updateLinkingState.postValue(DataState.Error(result))
            return false
        }
        return true
    }


    private fun validateLinkingFields(ownerName: String, carName: String): Boolean {
        val result = validateLinkForm(ownerName, carName)
        if (result != NONE) {
            linkingState.postValue(DataState.Error(result))
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
                    ownersState.postValue(DataState.Error(ErrorMessage.ERROR_NO_OWNERS_FOUND))
                else
                    ownersState.postValue(DataState.Success(response))
            } else {
                ownersState.postValue(DataState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
            }
        }
    }

    fun getAllOwnerTestReturnErrorNoOwners() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = listOf<UserModel>()
            if (response.isEmpty())
                ownersState.postValue(DataState.Error(ErrorMessage.ERROR_NO_OWNERS_FOUND))
        }
    }

    fun getAllOwnerTestReturnNetworkError() {
        viewModelScope.launch(Dispatchers.IO) {
            ownersState.postValue(DataState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
        }
    }

    fun getAllCarsTest() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllCars()
            if (response != null) {
                if (response.isEmpty())
                    carsState.postValue(DataState.Error(ErrorMessage.ERROR_NO_CAR_FOUND))
                else
                    carsState.postValue(DataState.Success(response))
            } else {
                carsState.postValue(DataState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
            }
        }
    }


    fun getAllCarsTestReturnErrorNoCars() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = listOf<UserModel>()
            if (response.isEmpty())
                carsState.postValue(DataState.Error(ErrorMessage.ERROR_NO_CAR_FOUND))
        }
    }

    fun getAllCarsTestReturnNetworkError() {
        viewModelScope.launch(Dispatchers.IO) {
            carsState.postValue(DataState.Error(ErrorMessage.ERROR_NETWORK_ERROR))
        }
    }

    fun linkTest(owner: UserModel, car: CarModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.linkOwnerAndCar(owner, car)
            if (response) {
                linkingState.postValue(DataState.Success(response))
            } else {
                linkingState.postValue(DataState.Error(ErrorMessage.ERROR_OWNER_OR_CAR_NOT_EXISTS))
            }
        }
    }


}