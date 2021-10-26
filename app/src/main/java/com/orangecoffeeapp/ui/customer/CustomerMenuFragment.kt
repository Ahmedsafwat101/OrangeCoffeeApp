package com.orangecoffeeapp.ui.customer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.data.models.LinkedCarsWithOwners
import com.orangecoffeeapp.data.models.MenuItemModel
import com.orangecoffeeapp.databinding.FragmentCutomerMenuBinding
import com.orangecoffeeapp.ui.adapters.CarMenuAdapter
import com.orangecoffeeapp.ui.viewmodels.CarViewModel
import com.orangecoffeeapp.utils.common.DataState
import com.orangecoffeeapp.utils.common.DisplayHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cutomer_menu.*

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController


@AndroidEntryPoint
class CustomerMenuFragment : Fragment() {

    private  val TAG = "CustomerMenuFragment"
    private lateinit var menuAdapter: CarMenuAdapter
    private var menuData: ArrayList<MenuItemModel> = ArrayList()
    private lateinit var customerMenuBinding:FragmentCutomerMenuBinding
    private val carViewModel: CarViewModel by viewModels()
    private val ordersCart:HashMap<MenuItemModel,Int> = HashMap()
    private var counter = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        customerMenuBinding =  FragmentCutomerMenuBinding.inflate(inflater, container, false)
       return customerMenuBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val currSelectedItem = arguments?.getSerializable("currLinkedCar") as LinkedCarsWithOwners

        subscribeObserver()
        carViewModel.getCarMenu(currSelectedItem.carModel.carName)

        customerMenuBinding.swipeContainer.setOnRefreshListener {
            fetchTimelineAsync(currSelectedItem.carModel.carName)
        }

        customerMenuBinding.cartBtn.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable("currLinkedCar",currSelectedItem)
            bundle.putSerializable("orderMap",ordersCart)
            findNavController().navigate(R.id.action_customerMenuFragment_to_checkOutFragment,bundle)
        }

    }


    private fun subscribeObserver() {
        carViewModel.getCarStates().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    DisplayHelper.displayProgressbar(false, customerMenuBinding.progressCircular)
                    menuData = result.data.menuItems
                    initRecyclerView()
                }
                is DataState.OperationWithFeedback -> {
                    DisplayHelper.displayProgressbar(false, customerMenuBinding.progressCircular)
                    DisplayHelper.displaySnack(
                        ErrorMessage.ERROR_CAR_DONT_HAVE_MENU,
                        customerMenuBinding.parent
                    )

                }
                is DataState.Loading -> {
                    DisplayHelper.displayProgressbar(true, customerMenuBinding.progressCircular)
                }
                is DataState.Error -> {
                    DisplayHelper.displayProgressbar(false, customerMenuBinding.progressCircular)
                    DisplayHelper.displaySnack(result.e, customerMenuBinding.parent)
                    Log.d("here", "Error ${result.e}")

                }
                else -> {
                    Log.d(TAG, "")
                }
            }
        })
    }
    private fun fetchTimelineAsync(carName: String) {
        carViewModel.getCarMenu(carName)
        swipeContainer.isRefreshing = false
    }

    private fun initRecyclerView() {
        customerMenuBinding.menuRecView.apply {
            this.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            menuAdapter = CarMenuAdapter(::onClick)
            this.adapter = menuAdapter
        }
        addCarsDataSet()
    }

    private fun addCarsDataSet() {
        menuAdapter.submitList(menuData)
    }

    private fun onClick(position: Int){
        ordersCart[menuData[position]] = (ordersCart[menuData[position]] ?: 0)+1
        Log.d(TAG,ordersCart.toString())
        counter++
        customerMenuBinding.counterTxt.text = counter.toString()
        val animation: Animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.bounce)
        customerMenuBinding.counterTxt.startAnimation(animation);
    }

}