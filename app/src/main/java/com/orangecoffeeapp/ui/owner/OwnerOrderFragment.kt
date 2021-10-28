package com.orangecoffeeapp.ui.owner

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orangecoffeeapp.constants.OrdersStatus
import com.orangecoffeeapp.data.models.Order
import com.orangecoffeeapp.databinding.FragmentOwnerOrderBinding
import com.orangecoffeeapp.ui.adapters.OrderStatusAdapter
import com.orangecoffeeapp.ui.viewmodels.OrderViewModel
import com.orangecoffeeapp.utils.SwipeToUpdate
import com.orangecoffeeapp.utils.common.DataState
import com.orangecoffeeapp.utils.common.DisplayHelper
import com.orangecoffeeapp.utils.common.UserSharedPreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class OwnerOrderFragment : Fragment() {
    private val TAG = "OwnerOrderFragment"
    private lateinit var ordersAdapter: OrderStatusAdapter
    private val orderViewModel: OrderViewModel by viewModels()
    lateinit var ordersBinding: FragmentOwnerOrderBinding
    private var ordersData: ArrayList<Order> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        ordersBinding = FragmentOwnerOrderBinding.inflate(inflater, container, false)
        return ordersBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currUser = UserSharedPreferenceManager(requireActivity()).getSharedPreferenceData()
        subscribeObserver()
        orderViewModel.carOrders(currUser.carID)
    }

    private fun subscribeObserver() {
        orderViewModel.getOrdersState().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    Log.d(TAG, "data " + result.data)
                    DisplayHelper.displayProgressbar(false, ordersBinding.progressCircular)
                    ordersData = ArrayList()
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


        val swipeHandler = object : SwipeToUpdate(requireContext(),ordersData) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val currOrder = ordersData[pos]
                //ordersAdapter.notifyItemRemoved(pos)

                if (currOrder.orderStatus == OrdersStatus.Pending)
                    currOrder.orderStatus = OrdersStatus.Processing
                else if (currOrder.orderStatus == OrdersStatus.Processing)
                    currOrder.orderStatus = OrdersStatus.Delivered

                ordersData[pos] = currOrder
                ordersAdapter.notifyItemChanged(pos)

            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(ordersBinding.ordersRecView)

    }

    private fun addCarsDataSet() {
        ordersAdapter.submitList(ordersData)
    }




}