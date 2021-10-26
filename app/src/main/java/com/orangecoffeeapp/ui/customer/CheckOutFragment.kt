package com.orangecoffeeapp.ui.customer

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangecoffeeapp.R
import com.orangecoffeeapp.data.models.LinkedCarsWithOwners
import com.orangecoffeeapp.data.models.MenuItemModel
import com.orangecoffeeapp.data.models.Order
import com.orangecoffeeapp.databinding.FragmentCheckOutBinding
import com.orangecoffeeapp.ui.adapters.OrderRecyclerAdapter
import com.orangecoffeeapp.ui.viewmodels.OrderViewModel
import com.orangecoffeeapp.utils.common.DataState
import com.orangecoffeeapp.utils.common.DisplayHelper.displayProgressbar
import com.orangecoffeeapp.utils.common.DisplayHelper.displaySnack
import com.orangecoffeeapp.utils.common.DisplayHelper.displayToast
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@SuppressLint("SimpleDateFormat")
@AndroidEntryPoint
class CheckOutFragment : Fragment() {
    private val TAG = "CheckOutFragment"
    private lateinit var checkOutBinding: FragmentCheckOutBinding
    private lateinit var orderAdapter: OrderRecyclerAdapter
    private val orderViewModel: OrderViewModel by viewModels()
    private var total = 0f
    private var flag = true
    private var itemsCount = 0
    private val bundle = Bundle()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        checkOutBinding = FragmentCheckOutBinding.inflate(inflater, container, false)
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_checkOutFragment_to_customerHomeFragment)
                }
            }
            )
        return checkOutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val currSelectedItem = arguments?.getSerializable("currLinkedCar") as LinkedCarsWithOwners
        val orders = arguments?.getSerializable("orderMap") as HashMap<MenuItemModel, Int>

        initRecyclerView()
        orderAdapter.submitList(orders)
        subscribeObserver()

        checkOutBinding.checkoutBtn.setOnClickListener {
            val currOrder = Order(
                userID = currSelectedItem.userModel.email,
                carName = currSelectedItem.carModel.carName,
                date = getLocalDate(),
                orderPrice = total,
                orderContentKeys = orderAdapter.getMap().keys.toList(),
                orderContentValues = orderAdapter.getMap().values.toList(),
                )

            bundle.putSerializable("currLinkedCar",currSelectedItem)

            orderViewModel.addOrder(currOrder)
        }
    }


    private fun subscribeObserver() {
        orderViewModel.getOrderState().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    displayProgressbar(false,checkOutBinding.progressCircular)
                    displayToast("Linked Successfully!",requireActivity())
                    findNavController().navigate(R.id.action_checkOutFragment_to_navigationFragment,bundle)

                }
                is DataState.Loading -> {
                    displayProgressbar(true,checkOutBinding.progressCircular)

                }
                is DataState.Error -> {
                    displayProgressbar(false,checkOutBinding.progressCircular)
                    displaySnack(result.e,checkOutBinding.parent)
                    Log.d("here", "Error ${result.e}")
                }
                else -> {
                    Log.d(TAG, "")
                }
            }
        })
    }



    private fun initRecyclerView() {
        checkOutBinding.orderRecView.apply {
            this.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            orderAdapter =
                OrderRecyclerAdapter(::getValues, ::getUpdatedMap, ::incrementAndDecrement)
            this.adapter = orderAdapter
        }
    }

    private fun getValues(currItemPrice: Float) {
        if (itemsCount < orderAdapter.itemCount) {
            total += currItemPrice
            checkOutBinding.totalPriceTxt.text = total.toString()
            itemsCount++
        } else {
            flag = false
        }
    }


    private fun incrementAndDecrement(currItemPrice: Float) {
        total += currItemPrice
        checkOutBinding.totalPriceTxt.text = total.toString()
    }

    private fun getUpdatedMap(itemModel: MenuItemModel, count: Int) {
        orderAdapter.updateMap(itemModel, count)
    }

    private fun getLocalDate(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return sdf.format(Date()).toString()
    }

}