package com.orangecoffeeapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.MenuItemModel
import com.orangecoffeeapp.data.repository.AddCarRepository
import com.orangecoffeeapp.utils.addcar.AddCarFormUtils
import com.orangecoffeeapp.utils.addcar.AddInventoryUtils
import com.orangecoffeeapp.utils.DataState
import com.orangecoffeeapp.utils.SingleLiveEvent
import com.orangecoffeeapp.utils.owner.AddMenuItemsUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(private val repo: AddCarRepository) : ViewModel() {

    private val TAG = "MyViewModel"
    private val carStates = SingleLiveEvent<DataState<CarModel>>()

    fun getCarStates() = carStates


    fun addCar(data: Any) {
        carStates.postValue(DataState.Loading)
        val currCar = data as CarModel
        if (validateInventoryFields(
                currCar.inventory.coffeeBeans,
                currCar.inventory.milk,
                currCar.inventory.water,
                currCar.inventory.sugar
            )
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.addCarMenu(currCar)
                if (response != null) {
                    carStates.postValue(DataState.Success(response))
                } else {
                    carStates.postValue(DataState.Error(ErrorMessage.ERROR_CAR_ALREADY_EXISTS_MSG))
                }
            }
        }
    }

    fun getCarMenu(carName: String) {
        carStates.postValue(DataState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getCar(carName)
            if (response != null) {
                if(response.menuItems.isEmpty())
                    carStates.postValue(DataState.OperationWithFeedback(response,ErrorMessage.ERROR_CAR_DONT_HAVE_MENU))
                else
                  carStates.postValue(DataState.Success(response))
            } else {
                carStates.postValue(DataState.Error(ErrorMessage.ERROR_CAR_NOT_EXISTS_MSG))
            }
        }
    }


    fun updateCarMenu(currCar: CarModel, currMenuItems: MenuItemModel) {
        carStates.postValue(DataState.Loading)

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
                val response = repo.updateCarMenu(currCar, currMenuItems)
                if (response != null) {
                    carStates.postValue(DataState.Success(response))
                } else {
                    carStates.postValue(DataState.Error(ErrorMessage.ERROR_CAR_NOT_EXISTS_MSG))
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
            carStates.postValue(DataState.Error(result))
            return false
        }
        return true
    }


    private fun validateInventoryFields(coffee: Int, milk: Int, water: Int, sugar: Int): Boolean {
        val result = AddInventoryUtils.validateAddItemForm(coffee, milk, water, sugar)
        if (result != ErrorMessage.NONE) {
            carStates.postValue(DataState.Error(result))
            return false
        }
        return true
    }

    fun validateAddCarFields(
        carName: String,
        address: String,
        latitude: Double,
        longitude: Double
    ): Boolean {
        val result = AddCarFormUtils.validateAddCarForm(carName, address, latitude, longitude)
        if (result != ErrorMessage.NONE) {
            carStates.postValue(DataState.Error(result))
            return false
        }
        return true
    }


    /**Test**/

    fun addCarTest(data: Any) {
        val currCar = data as CarModel
        if (validateAddCarFields(
                currCar.carName,
                currCar.address,
                currCar.latitude,
                currCar.longitude
            )
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.addCarMenu(currCar)
                if (response != null) {
                    carStates.postValue(DataState.Success(response))
                } else {
                    carStates.postValue(DataState.Error(ErrorMessage.ERROR_CAR_ALREADY_EXISTS_MSG))
                }
            }
        }
    }



}