package com.orangecoffeeapp.ui.addcar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.repository.AddCarRepository
import com.orangecoffeeapp.utils.addcar.AddCarFormUtils
import com.orangecoffeeapp.utils.addcar.AddInventoryUtils
import com.orangecoffeeapp.utils.admission.AdmissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCarViewModel @Inject constructor(private val repo: AddCarRepository) : ViewModel() {

    private val TAG = "MyViewModel"
    private val carStates = MutableLiveData<AdmissionState<CarModel>>()

    fun getCarStates() = carStates


    fun addCar(data: Any) {
        carStates.postValue(AdmissionState.Loading)
        val currCar = data as CarModel
        if (validateInventoryFields(currCar.inventory.coffeeBeans,currCar.inventory.milk,currCar.inventory.water,currCar.inventory.sugar)) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.addCarToDB(currCar)
                if (response != null) {
                    carStates.postValue(AdmissionState.Success(response))
                } else {
                    carStates.postValue(AdmissionState.Error(ErrorMessage.ERROR_CAR_ALREADY_EXISTS_MSG))
                }
            }
        }
    }


    private fun validateInventoryFields(coffee:Int, milk:Int, water:Int, sugar:Int):Boolean{
        val result = AddInventoryUtils.validateAddItemForm(coffee, milk, water,sugar)
        if (result != ErrorMessage.NONE) {
            carStates.postValue(AdmissionState.Error(result))
            return false
        }
        return true
    }

     fun validateAddCarFields(carName: String, address: String, location: LatLng): Boolean {
        val result = AddCarFormUtils.validateAddCarForm(carName, address, location)
        if (result != ErrorMessage.NONE) {
            carStates.postValue(AdmissionState.Error(result))
            return false
        }
        return true
    }


                                                 /**Test**/

    fun addCarTest(data: Any) {
        val currCar = data as CarModel
        if (validateAddCarFields(currCar.carName, currCar.address, currCar.location)) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.addCarToDB(currCar)
                if (response != null) {
                    carStates.postValue(AdmissionState.Success(response))
                } else {
                    carStates.postValue(AdmissionState.Error(ErrorMessage.ERROR_CAR_ALREADY_EXISTS_MSG))
                }
            }
        }
    }


}