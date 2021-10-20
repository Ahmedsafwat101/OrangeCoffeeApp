package com.orangecoffeeapp.ui.addcar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.R
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentLinkingBinding
import com.orangecoffeeapp.ui.adapters.CarRecyclerAdapter
import com.orangecoffeeapp.ui.adapters.OwnerRecyclerAdapter
import com.orangecoffeeapp.ui.edituser.EditUserActivity
import com.orangecoffeeapp.utils.admission.AdmissionState
import com.orangecoffeeapp.utils.Hashing
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinkingFragment : Fragment(), CarRecyclerAdapter.OnCarItemListener,
    OwnerRecyclerAdapter.OnOwnerItemListener {
    private val TAG = "LinkingFragment"

    private lateinit var ownerAdapter: OwnerRecyclerAdapter
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
                is AdmissionState.Success -> {
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
                is AdmissionState.Loading -> {
                    displayProgressbar(true)

                }
                is AdmissionState.Error -> {
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
                is AdmissionState.Success -> {
                    Log.d(TAG, "data " + result.data)
                    displayProgressbar(false)
                    carsData.addAll(result.data)

                    initCarRecyclerView()
                    Log.d(TAG, "LIST " + ownersData.toString())

                }
                is AdmissionState.Loading -> {
                    displayProgressbar(true)

                }
                is AdmissionState.Error -> {
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
                is AdmissionState.Success -> {
                    Log.d(TAG, "data " + result.data)
                    displayProgressbar(false)
                    ownersData.addAll(result.data)
                    initOwnerRecyclerView()
                    Log.d(TAG, "LIST " + ownersData.toString())

                }
                is AdmissionState.Loading -> {
                    displayProgressbar(true)

                }
                is AdmissionState.Error -> {
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
            ownerAdapter = OwnerRecyclerAdapter(this@LinkingFragment)
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
            carAdapter = CarRecyclerAdapter(this@LinkingFragment)
            this.adapter = carAdapter
        }
        addCarsDataSet()

    }

    private fun addCarsDataSet() {
        carAdapter.submitList(carsData)
    }


    override fun onCarItemClicked(position: Int) {
        currSelectedCar = carsData[position]
        currCarPos = position
       linkingBinding.coffeeCarTxt.setText(carsData[position].carName)
    }

    override fun onCarItemClickedShowDetails(position: Int) {
        val bundle = Bundle()
        bundle.putString("name",carsData[position].carName)
        bundle.putString("address",carsData[position].address)
        findNavController().navigate(R.id.action_linkingFragment_to_carDetailsFragment,bundle)
    }

    override fun onOwnerItemClicked(position: Int) {
        currSelectedOwner = ownersData[position]
        currOwnerPos = position
        linkingBinding.ownerTxt.setText(ownersData[position].email)
    }

    override fun onOwnerItemClickedShowDetails(position: Int) {
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