package com.orangecoffeeapp.ui.customer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.data.models.LinkedCarsWithOwners
import com.orangecoffeeapp.databinding.FragmentCustomerHomeBinding
import com.orangecoffeeapp.ui.adapters.CustomerCarRecyclerAdapter
import com.orangecoffeeapp.ui.viewmodels.LinkingViewModel
import com.orangecoffeeapp.utils.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerHomeFragment : Fragment() {


    private  val TAG = "CustomerHomeFragment"
    private lateinit var linkedCarAdapter: CustomerCarRecyclerAdapter

    private val linkingViewModel: LinkingViewModel by viewModels()
    private var linkedData:ArrayList<LinkedCarsWithOwners> = ArrayList()
    private lateinit var binding:FragmentCustomerHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCustomerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeObserverToLinkedData()
        linkingViewModel.getAllLinkedData()

    }

    private fun subscribeObserverToLinkedData() {
        linkingViewModel.getLinkedState().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    Log.d(TAG, "data " + result.data)
                    displayProgressbar(false)
                    linkedData = ArrayList()
                    linkedData.addAll(result.data)
                    initRecyclerView()

                }
                is DataState.Loading -> {
                    displayProgressbar(true)

                }
                is DataState.Error -> {
                    displayProgressbar(false)
                    displaySnackbar(result.e)
                    Log.d("here", "Error ${result.e}")

                }
                else -> {
                    Log.d(TAG, "")
                }
            }

        })

    }


    private fun displayProgressbar(isDisplayed: Boolean) {
        binding.progressCircular.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun displaySnackbar(error: String) {
        Snackbar.make(binding.parent, error, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
            .show()
    }



    private fun initRecyclerView() {
        binding.CarsRecView.apply {
            this.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            linkedCarAdapter = CustomerCarRecyclerAdapter()
            this.adapter = linkedCarAdapter
        }
        addCarsDataSet()

    }

    private fun addCarsDataSet() {
        linkedCarAdapter.submitList(linkedData)
    }




}