package com.orangecoffeeapp.ui.edituser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentEditUserBinding
import com.orangecoffeeapp.ui.viewmodels.AdmissionViewModel
import com.orangecoffeeapp.utils.common.UserSharedPreferenceManager
import com.orangecoffeeapp.utils.common.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EditUserFragment : Fragment() {
    private val admissionViewModel: AdmissionViewModel by viewModels()
    private lateinit var editUserBinding: FragmentEditUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editUserBinding = FragmentEditUserBinding.inflate(inflater, container, false)
        return editUserBinding.root
    }


    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        //Access View
        //Access SharedPreferenceManager


        val currUser = UserSharedPreferenceManager(requireActivity()).getSharedPreferenceData()
        setFields(currUser)

        subscribeObserver()


        editUserBinding.updateBtn.setOnClickListener {
            it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }

            admissionViewModel.viewModelScope.launch {
                admissionViewModel.update(UserModel(
                    firstName = editUserBinding.updateFNameTxt.text.toString(),
                    lastName = editUserBinding.updateLNameTxt.text.toString(),
                    phone =  editUserBinding.updatePhoneTxt.text.toString(),
                    password = editUserBinding.updatePasswordTxt.text.toString(),
                    email = currUser.email,
                    access = currUser.access,
                    type = currUser.type
                ))
            }
        }

    }

    private fun setFields(user: UserModel) {
        editUserBinding.updateFNameTxt.setText(user.firstName)
        editUserBinding.updateLNameTxt.setText(user.lastName)
        editUserBinding.updatePasswordTxt.setText(user.password)
        editUserBinding.updatePhoneTxt.setText(user.phone)
    }

    private fun subscribeObserver() {
        admissionViewModel.getUserStates().observe(viewLifecycleOwner, { result ->
            when (result) {
                is DataState.Success -> {
                    displayProgressbar(false)
                    displaySnackbar("Data Updates Successfully! ", com.orangecoffeeapp.R.color.Green_300)
                    UserSharedPreferenceManager(requireActivity()).saveSharedPreferenceData(result.data)

                }
                is DataState.Loading -> {
                    displayProgressbar(true)
                }
                is DataState.Error -> {
                    when (result.e) {
                        ErrorMessage.ERROR_EMPTY_FNAME_MSG -> {
                            editUserBinding.updateFNameTxt.error = result.e
                        }
                        ErrorMessage.ERROR_EMPTY_LNAME_MSG -> {
                            editUserBinding.updateLNameTxt.error = result.e
                        }
                        ErrorMessage.ERROR_PASSWORD_IS_EMPTY -> {
                            editUserBinding.updatePasswordTxt.error = result.e
                        }
                        ErrorMessage.ERROR_PASSWORD_LENGTH_LESS_THAN_8 -> {
                            editUserBinding.updatePasswordTxt.error = result.e
                        }
                        ErrorMessage.ERROR_EMPTY_PHONE_MSG -> {
                            editUserBinding.updatePhoneTxt.error = result.e
                        }
                        ErrorMessage.ERROR_INVALID_PHONE_MSG -> {
                            editUserBinding.updatePhoneTxt.error = result.e
                        }
                    }
                    displayProgressbar(false)
                    displaySnackbar(result.e, com.orangecoffeeapp.R.color.Red_200)

                }
            }
        })
    }

    private fun displayProgressbar(isDisplayed: Boolean) {
        editUserBinding.progressCircular.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

    private fun displaySnackbar(message: String, color: Int) {
        Snackbar.make(editUserBinding.parent, message, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(resources.getColor(color))
            .show()
    }
}