package com.orangecoffeeapp.ui.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.R
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentLinkingBinding
import com.orangecoffeeapp.ui.adapters.CarRecyclerAdapter
import com.orangecoffeeapp.ui.adapters.OwnerAdapter
import com.orangecoffeeapp.ui.edituser.EditUserActivity
import com.orangecoffeeapp.ui.viewmodels.LinkingViewModel
import com.orangecoffeeapp.utils.common.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinkingFragment : Fragment(){
    private val TAG = "LinkingFragment"

    private lateinit var ownerAdapter: OwnerAdapter
    private lateinit var carAdapter: CarRecyclerAdapter;

    lateinit var linkingBinding: FragmentLinkingBinding
    private var ownersData: ArrayList<UserModel> = ArrayList()
    private var carsData: ArrayList<CarModel> = ArrayList()
    private val linkingViewModel: LinkingViewModel by viewModels()

    private var currSelectedOwner = UserModel()
    private var currSelectedCar = CarModel()

    private var currCarPos = -1;
    private var currOwnerPos = -1;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        linkingBinding = FragmentLinkingBinding.inflate(inflater, container, false)
        return linkingBinding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObserverToOwners()
        subscribeObserverToCars()
        subscribeObserver()

        linkingViewModel.getAllOwners()
        linkingViewModel.getAllCars()

        linkingBinding.linkBtn.setOnClickListener {
            linkingViewModel.link(currSelectedOwner,currSelectedCar,linkingBinding.ownerTxt.text.toString(),linkingBinding.coffeeCarTxt.text.toString())
        }

    }

    private fun subscribeObserver() {
        linkingViewModel.getLinkingState().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    displayProgressbar(false)
                    displaySnackbar("Linked Successfully!")
                    carsData.removeAt(currCarPos)
                    ownersData.removeAt(currOwnerPos)
                    addOwnerDataSet()
                    addCarsDataSet()
                    carAdapter.notifyDataSetChanged()
                    ownerAdapter.notifyDataSetChanged()
                    linkingBinding.ownerTxt.setText("")
                    linkingBinding.coffeeCarTxt.setText("")

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
    private fun subscribeObserverToCars() {
        linkingViewModel.getCarsState().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    Log.d(TAG, "data " + result.data)
                    displayProgressbar(false)
                    carsData.addAll(result.data)

                    initCarRecyclerView()
                    Log.d(TAG, "LIST " + ownersData.toString())

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



    private fun subscribeObserverToOwners() {
        linkingViewModel.getOwnersState().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    Log.d(TAG, "data " + result.data)
                    displayProgressbar(false)
                    ownersData.addAll(result.data)
                    initOwnerRecyclerView()
                    Log.d(TAG, "LIST " + ownersData.toString())

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
        linkingBinding.progressCircular.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun displaySnackbar(error: String) {
        Snackbar.make(linkingBinding.parent, error, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
            .show()
    }

    private fun initOwnerRecyclerView() {

        linkingBinding.ownerRecView.apply {
            this.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            ownerAdapter = OwnerAdapter(::onOwnerItemClicked,::onOwnerItemClickedShowDetails)
            this.adapter = ownerAdapter
        }
        addOwnerDataSet()

    }

    private fun addOwnerDataSet() {
        ownerAdapter.submitList(ownersData)
    }

    private fun initCarRecyclerView() {

        linkingBinding.carsRecView.apply {
            this.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            carAdapter = CarRecyclerAdapter(::onCarItemClicked,::onCarItemClickedShowDetails)
            this.adapter = carAdapter
        }
        addCarsDataSet()

    }

    private fun addCarsDataSet() {
        carAdapter.submitList(carsData)
    }

    private fun onCarItemClicked(position: Int){
        currSelectedCar = carsData[position]
        currCarPos = position
        linkingBinding.coffeeCarTxt.setText(carsData[position].carName)
    }

    private fun onCarItemClickedShowDetails(position: Int) {
        val bundle = Bundle()
        bundle.putString("name",carsData[position].carName)
        bundle.putString("address",carsData[position].address)
        findNavController().navigate(R.id.action_linkingFragment_to_carDetailsFragment,bundle)
    }

    private fun onOwnerItemClicked(position: Int) {
        currSelectedOwner = ownersData[position]
        currOwnerPos = position
        linkingBinding.ownerTxt.setText(ownersData[position].email)
    }

    private fun onOwnerItemClickedShowDetails(position: Int) {
        val bundle = Bundle()
        bundle.putString("name",ownersData[position].firstName+" "+ownersData[position].lastName)
        bundle.putString("email",ownersData[position].email)
        bundle.putString("phone",ownersData[position].phone)

        findNavController().navigate(R.id.action_linkingFragment_to_ownerDetailsFragment,bundle)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.updateProfile -> {
                val intent = Intent(requireActivity(), EditUserActivity::class.java)
                startActivity(intent)
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}