package com.orangecoffeeapp.ui.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.constants.UserTypes
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentAdminAddNewOwnerBinding
import com.orangecoffeeapp.ui.viewmodels.AdmissionViewModel
import com.orangecoffeeapp.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.view.*
import com.orangecoffeeapp.R
import com.orangecoffeeapp.ui.edituser.EditUserActivity


@AndroidEntryPoint
class AddNewOwnerFragment : Fragment() {
    private val TAG = "AddNewOwnerFragment"
    lateinit var addNewOwnerBinding: FragmentAdminAddNewOwnerBinding
    private val admissionViewModel: AdmissionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addNewOwnerBinding = FragmentAdminAddNewOwnerBinding.inflate(inflater, container, false)

        return addNewOwnerBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //AccessView
        subscribeObserver()

        addNewOwnerBinding.addOwnerBtn.setOnClickListener {
            it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }

            admissionViewModel.viewModelScope.launch {
                admissionViewModel.signUp(
                    UserModel(
                        firstName = addNewOwnerBinding.ownerFNameTxt.text.toString(),
                        lastName = addNewOwnerBinding.ownerLNameTxt.text.toString(),
                        phone = addNewOwnerBinding.ownerPhoneTxt.text.toString(),
                        password = addNewOwnerBinding.ownerPasswordTxt.text.toString(),
                        email = addNewOwnerBinding.ownerEmailTxt.text.toString(),
                        access = true,
                        type = UserTypes.Owner
                    )
                )
            }
        }
    }

    private fun subscribeObserver() {
        admissionViewModel.getUserStates().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    displayProgressbar(false)
                    displaySnackbar("Owner is added Successfully! ", R.color.green_300)
                   // val intent = Intent(requireActivity(),AddCarActivity::class.java)
                   // startActivity(intent)
                //    requireActivity().finish()
                }
                is DataState.Loading -> {
                    displayProgressbar(true)
                }
                is DataState.Error -> {
                    when (result.e) {
                        ErrorMessage.ERROR_EMPTY_FNAME_MSG -> {
                            addNewOwnerBinding.ownerFNameTxt.error = result.e
                        }
                        ErrorMessage.ERROR_EMPTY_LNAME_MSG -> {
                            addNewOwnerBinding.ownerLNameTxt.error = result.e
                        }
                        ErrorMessage.ERROR_EMAIL_IS_EMPTY -> {
                            addNewOwnerBinding.ownerEmailTxt.error = result.e
                        }
                        ErrorMessage.ERROR_EMAIL_NOT_VALID_MSG -> {
                            addNewOwnerBinding.ownerEmailTxt.error = result.e
                        }
                        ErrorMessage.ERROR_PASSWORD_IS_EMPTY -> {
                            addNewOwnerBinding.ownerPasswordTxt.error = result.e
                        }
                        ErrorMessage.ERROR_PASSWORD_LENGTH_LESS_THAN_8 -> {
                            addNewOwnerBinding.ownerPasswordTxt.error = result.e
                        }
                        ErrorMessage.ERROR_EMPTY_PHONE_MSG -> {
                            addNewOwnerBinding.ownerPhoneTxt.error = result.e
                        }
                        ErrorMessage.ERROR_INVALID_PHONE_MSG -> {
                            addNewOwnerBinding.ownerPhoneTxt.error = result.e
                        }
                    }
                    displayProgressbar(false)
                    displaySnackbar(result.e, com.orangecoffeeapp.R.color.green_300)

                }
                else -> {
                    Log.d(TAG,"Log else ")
                }
            }
        })

    }

    //Update to Toast
    private fun displaySnackbar(message: String, color: Int) {
        Snackbar.make(addNewOwnerBinding.parent, message, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(resources.getColor(color))
            .show()
    }

    private fun displayProgressbar(isDisplayed: Boolean) {
        addNewOwnerBinding.progressCircular.visibility =
            if (isDisplayed) View.VISIBLE else View.GONE
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