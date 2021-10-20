package com.orangecoffeeapp.ui.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.R
import com.orangecoffeeapp.data.models.LinkedCarsWithOwners
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentAdminHomeBinding
import com.orangecoffeeapp.ui.adapters.CarRecyclerAdapter
import com.orangecoffeeapp.ui.adapters.LinkedRecyclerAdapter
import com.orangecoffeeapp.ui.addcar.LinkingViewModel
import com.orangecoffeeapp.ui.edituser.EditUserActivity
import com.orangecoffeeapp.utils.admission.AdmissionState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdminHomeFragment : Fragment(),LinkedRecyclerAdapter.OnItemListener {
    private  val TAG = "AdminHomeFragment"
    private lateinit var linkedAdapter: LinkedRecyclerAdapter
    private lateinit var homeBinding: FragmentAdminHomeBinding
    private var linkedData:ArrayList<LinkedCarsWithOwners> = ArrayList()
    private val linkingViewModel: LinkingViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finish()
        }
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscribeObserverToOwners()
        linkingViewModel.getAllLinkedData()

    }


    private fun subscribeObserverToOwners() {
        linkingViewModel.getLinkedState().observe(viewLifecycleOwner, { result ->
            when (result) {
                is AdmissionState.Success -> {
                    Log.d(TAG, "data " + result.data)
                    displayProgressbar(false)
                    linkedData = ArrayList()
                    linkedData.addAll(result.data)
                    initRecyclerView()

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
        homeBinding.progressCircular.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun displaySnackbar(error: String) {
        Snackbar.make(homeBinding.parent, error, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
            .show()
    }



    private fun initRecyclerView() {
        homeBinding.linkedRecView.apply {
            this.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            linkedAdapter = LinkedRecyclerAdapter(this@AdminHomeFragment)
            this.adapter = linkedAdapter
        }
        addCarsDataSet()

    }

    private fun addCarsDataSet() {
        linkedAdapter.submitList(linkedData)
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

    override fun onItemClicked(position: Int) {
        val currItem = linkedData[position]
        val bundle = Bundle()
        bundle.putString("carName",currItem.carModel.carName)
        bundle.putString("ownerName",currItem.userModel.firstName +" "+currItem.userModel.lastName)
        bundle.putString("address",currItem.carModel.address)
        bundle.putString("phone",currItem.userModel.phone)
        bundle.putString("email",currItem.userModel.email)

        findNavController().navigate(R.id.action_HomeFragment_to_linkedDetailsFragment,bundle)
    }
}