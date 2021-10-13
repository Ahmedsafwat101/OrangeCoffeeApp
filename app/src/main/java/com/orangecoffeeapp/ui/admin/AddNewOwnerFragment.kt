package com.orangecoffeeapp.ui.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.constants.UserTypes
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentAdminAddNewOwnerBinding
import com.orangecoffeeapp.ui.useradmission.AdmissionViewModel
import com.orangecoffeeapp.utils.SharedPreferenceManager
import com.orangecoffeeapp.utils.admission.AdmissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNewOwnerFragment : Fragment() {
    lateinit var addNewOwnerBinding: FragmentAdminAddNewOwnerBinding
    private val admissionViewModel: AdmissionViewModel by viewModels()

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
        admissionViewModel.getUser().observe(viewLifecycleOwner, { result ->
            when (result) {
                is AdmissionState.Success -> {
                    displayProgressbar(false)
                    displaySnackbar("Owner is added Successfully! ", R.color.Green_300)
                }
                is AdmissionState.Loading -> {
                    displayProgressbar(true)
                }
                is AdmissionState.Error -> {
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
                    displaySnackbar(result.e, R.color.Red_200)

                }
            }
        })

    }

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


}