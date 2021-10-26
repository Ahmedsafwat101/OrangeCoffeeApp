package com.orangecoffeeapp.ui.owner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.data.models.CarModel

import com.orangecoffeeapp.data.models.MenuItemModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentOwnerHomeBinding
import com.orangecoffeeapp.ui.adapters.CarMenuAdapter
import com.orangecoffeeapp.ui.viewmodels.CarViewModel
import com.orangecoffeeapp.utils.common.DisplayHelper
import com.orangecoffeeapp.utils.common.UserSharedPreferenceManager
import com.orangecoffeeapp.utils.common.DataState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OwnerHomeFragment : Fragment() {

    private val TAG = "OwnerHomeFragment"
    private val carViewModel: CarViewModel by viewModels()
    private lateinit var userSharedPref: UserSharedPreferenceManager
    private lateinit var menuAdapter: CarMenuAdapter
    private var menuData: ArrayList<MenuItemModel> = ArrayList()
    private lateinit var ownerHomeBinding: FragmentOwnerHomeBinding
    private var currCar: CarModel? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        ownerHomeBinding = FragmentOwnerHomeBinding.inflate(inflater, container, false)

        return ownerHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userSharedPref = UserSharedPreferenceManager(requireContext())
        val currOwner: UserModel = userSharedPref.getSharedPreferenceData()

        Log.d(TAG,"DATA "+currOwner)
        subscribeObserver()
        carViewModel.getCarMenu(currOwner.carID)


        ownerHomeBinding.addMenuItemBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("ownerObj", currOwner)
            bundle.putSerializable("carObj", currCar)


            findNavController().navigate(R.id.action_ownerHomeFragment_to_menuSetUpFragment, bundle)
        }
    }

    private fun subscribeObserver() {
        carViewModel.getCarStates().observe(viewLifecycleOwner, {
                result ->

            when (result) {
                is DataState.Success -> {
                    DisplayHelper.displayProgressbar(false, ownerHomeBinding.progressCircular)
                    currCar = result.data
                    menuData = result.data.menuItems
                    initRecyclerView()
                    ownerHomeBinding.addMenuItemBtn.visibility = View.VISIBLE
                }
                is DataState.OperationWithFeedback -> {
                    DisplayHelper.displayProgressbar(false, ownerHomeBinding.progressCircular)
                    DisplayHelper.displaySnack(
                        ErrorMessage.ERROR_CAR_DONT_HAVE_MENU,
                        ownerHomeBinding.parent
                    )
                    currCar = result.data
                    ownerHomeBinding.addMenuItemBtn.visibility = View.VISIBLE

                }
                is DataState.Loading -> {
                    DisplayHelper.displayProgressbar(true, ownerHomeBinding.progressCircular)
                }
                is DataState.Error -> {
                    DisplayHelper.displayProgressbar(false, ownerHomeBinding.progressCircular)
                    DisplayHelper.displaySnack(result.e, ownerHomeBinding.parent)
                    Log.d("here", "Error ${result.e}")
                    ownerHomeBinding.addMenuItemBtn.visibility = View.VISIBLE

                }
                else -> {
                    Log.d(TAG, "")
                }

            }
        })


    }

    private fun initRecyclerView() {
        ownerHomeBinding.menuRecView.apply {
            this.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            menuAdapter = CarMenuAdapter()
            this.adapter = menuAdapter
        }
        addCarsDataSet()
    }


    private fun addCarsDataSet() {
        menuAdapter.submitList(menuData)
    }


}

