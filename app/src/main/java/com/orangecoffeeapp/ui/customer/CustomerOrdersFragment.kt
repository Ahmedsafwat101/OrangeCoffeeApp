package com.orangecoffeeapp.ui.customer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangecoffeeapp.data.models.LinkedCarsWithOwners
import com.orangecoffeeapp.data.models.Order
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentCustomerOrdersBinding
import com.orangecoffeeapp.ui.adapters.CustomerCarAdapter
import com.orangecoffeeapp.ui.adapters.OrderStatusAdapter
import com.orangecoffeeapp.ui.viewmodels.OrderViewModel
import com.orangecoffeeapp.utils.common.DataState
import com.orangecoffeeapp.utils.common.DisplayHelper
import com.orangecoffeeapp.utils.common.UserSharedPreferenceManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CustomerOrdersFragment : Fragment() {
    private  val TAG = "CustomerOrdersFragment"
    lateinit var ordersAdapter:OrderStatusAdapter
    private val orderViewModel: OrderViewModel by viewModels()
    private lateinit var currUser:UserModel
    private lateinit var ordersBinding:FragmentCustomerOrdersBinding
    private var ordersData: ArrayList<Order> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        ordersBinding =  FragmentCustomerOrdersBinding.inflate(inflater, container, false)
        return ordersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currUser = UserSharedPreferenceManager(requireActivity()).getSharedPreferenceData()
        subscribeObserver()
        orderViewModel.userOrders(currUser.email)

    }


    private fun subscribeObserver() {
        orderViewModel.getOrdersState().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    Log.d(TAG, "data " + result.data)
                    DisplayHelper.displayProgressbar(false, ordersBinding.progressCircular)
                    ordersData = ArrayList()
                    //getLocation()
                    ordersData.addAll(result.data)
                    initRecyclerView()
                }
                is DataState.Loading -> {
                    DisplayHelper.displayProgressbar(true, ordersBinding.progressCircular)

                }
                is DataState.Error -> {
                    DisplayHelper.displayProgressbar(false, ordersBinding.progressCircular)
                    DisplayHelper.displaySnack(result.e, ordersBinding.parent)
                    Log.d("here", "Error ${result.e}")

                }
                else -> {
                    Log.d(TAG, "")
                }
            }
        })
    }

    private fun initRecyclerView() {
        ordersBinding.ordersRecView.apply {
            this.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            ordersAdapter = OrderStatusAdapter()
            this.adapter = ordersAdapter
        }
        addCarsDataSet()

    }

    private fun addCarsDataSet() {
        ordersAdapter.submitList(ordersData)
    }





}