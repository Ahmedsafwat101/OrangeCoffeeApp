package com.orangecoffeeapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun getOrderState() = orderState


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

    private fun validateOrder(orderContent: List<Int>): Boolean {
        val result = AddOrderUtils.validateOrder(orderContent)
        if (result== NONE) return true
        orderState.postValue(DataState.Error(ERROR_EMPTY_ORDER_CONTENT))
        return false
    }


}