package com.orangecoffeeapp.ui.admin

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
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.databinding.FragmentAddCarBinding
import com.orangecoffeeapp.ui.edituser.EditUserActivity
import com.orangecoffeeapp.ui.viewmodels.CarViewModel
import com.orangecoffeeapp.utils.common.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCarFragment : Fragment() {
    private val TAG = "MapFragment"
    private lateinit var addCarBinding: FragmentAddCarBinding
    private val carViewModel: CarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        addCarBinding = FragmentAddCarBinding.inflate(inflater, container, false)

        return addCarBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObserver()

        addCarBinding.addCarBtn.setOnClickListener {
            // If valid input return NONE
            it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }


            if (carViewModel.validateAddCarFields(
                    carName = addCarBinding.addCarNameTxt.text.toString(),
                    address = addCarBinding.addCarAddressTxt.text.toString(),
                    latitude = 1.0,
                    longitude = 1.0
                )
            ) {
                val bundle= Bundle()
                bundle.putString("carName",addCarBinding.addCarNameTxt.text.toString())
                bundle.putString("carAddress", addCarBinding.addCarAddressTxt.text.toString())
                findNavController().navigate(R.id.action_addCarFragment2_to_mapFragment2,bundle)
            }

        }

    }


    private fun subscribeObserver() {
        carViewModel.getCarStates().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Error -> {
                    displayProgressbar(false)
                    when (result.e) {
                        ErrorMessage.Error_Car_NAME_IS_EMPTY -> {
                            addCarBinding.addCarNameTxt.error = result.e
                        }
                        ErrorMessage.Error_Car_Address_IS_EMPTY -> {
                            addCarBinding.addCarAddressTxt.error = result.e
                        }
                    }
                    displaySnackbar(result.e, R.color.Red_200)
                    Log.d("here", "Error ${result.e}")
                }
                else -> {
                    Log.d(TAG, "")
                }
            }
        })
    }

    private fun displayProgressbar(isDisplayed: Boolean) {
        addCarBinding.progressCircular.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun displaySnackbar(message: String, color: Int) {
        Snackbar.make(addCarBinding.parent, message, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(resources.getColor(color))
            .show()
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
