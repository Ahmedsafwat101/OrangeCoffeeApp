package com.orangecoffeeapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMPTY_ORDER_CONTENT
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_NETWORK_ERROR
import com.orangecoffeeapp.constants.ErrorMessage.NONE
import com.orangecoffeeapp.data.models.Order
import com.orangecoffeeapp.data.repository.OrderRepository
import com.orangecoffeeapp.utils.common.DataState
import com.orangecoffeeapp.utils.common.SingleLiveEvent
import com.orangecoffeeapp.utils.customer.AddOrderUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val repo: OrderRepository) : ViewModel() {
    private val orderState = SingleLiveEvent<DataState<Boolean>>()
    private val ordersState = MutableLiveData<DataState<List<Order>>>()

    fun getOrderState() = orderState
    fun getOrdersState() = ordersState


    fun addOrder(order: Order) {
        orderState.postValue(DataState.Loading)
        if (validateOrder(orderContent = order.orderContentValues)) {
            viewModelScope.launch(Dispatchers.IO) {
                val response = repo.addOrder(order)
                if (response) {
                    orderState.postValue(DataState.Success(response))
                } else {
                    orderState.postValue(DataState.Error(ERROR_NETWORK_ERROR))
                }
            }
        }
    }


    fun userOrders(userEmail:String){
        ordersState.postValue(DataState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getOrdersForUser(userEmail)
            if (response != null) {
                if (response.isEmpty())
                    ordersState.postValue(DataState.Error(ErrorMessage.ERROR_NO_ORDERS_FOUND))
                else
                    ordersState.postValue(DataState.Success(response))
            } else {
                ordersState.postValue(DataState.Error(ERROR_NETWORK_ERROR))
            }

        }
    }


    fun carOrders(carName:String){
        ordersState.postValue(DataState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getOrdersForCAR(carName)
            if (response != null) {
                if (response.isEmpty())
                    ordersState.postValue(DataState.Error(ErrorMessage.ERROR_NO_ORDERS_FOUND))
                else
                    ordersState.postValue(DataState.Success(response))
            } else {
                ordersState.postValue(DataState.Error(ERROR_NETWORK_ERROR))
            }

        }
    }

    private fun validateOrder(orderContent: List<Int>): Boolean {
        val result = AddOrderUtils.validateOrder(orderContent)
        if (result == NONE) return true
        orderState.postValue(DataState.Error(ERROR_EMPTY_ORDER_CONTENT))
        return false
    }


}