package com.orangecoffeeapp.ui.useradmission

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentSignUpBinding

import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.constants.UserTypes
import com.orangecoffeeapp.utils.UserSharedPreferenceManager
import com.orangecoffeeapp.utils.admission.AdmissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val TAG = "SignUpFragment"
    private lateinit var signInBinding: FragmentSignUpBinding
    private val admissionViewModel: AdmissionViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signInBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        return signInBinding.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {

        subscribeObserver()
        //Access View
        signInBinding.signUpBtn.setOnClickListener {
            it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }
            admissionViewModel.viewModelScope.launch {
                admissionViewModel.signUp(UserModel(
                    firstName = signInBinding.signUpFNameTxt.text.toString(),
                    lastName = signInBinding.signUpLNameTxt.text.toString(),
                    phone =  signInBinding.signUpPhoneTxt.text.toString(),
                    password = signInBinding.signUpPasswordTxt.text.toString(),
                    email = signInBinding.signUpEmailTxt.text.toString(),
                    access = true,
                    type = UserTypes.Customer
                ))
            }
        }
    }

    private fun subscribeObserver() {
        admissionViewModel.getUserStates().observe(viewLifecycleOwner, { result ->
            when(result) {
                is AdmissionState.Success -> {
                    displayProgressbar(false)
                    requireActivity().finish()
                    UserSharedPreferenceManager(requireActivity()).saveSharedPreferenceData(result.data)
                   // findNavController().navigate(R.id.action_signUpFragment_to_adminHomeFragment)

                }
                is AdmissionState.Loading->{
                    displayProgressbar(true)
                }
                is AdmissionState.Error->{
                    when(result.e){
                        ErrorMessage.ERROR_EMPTY_FNAME_MSG -> {
                            signInBinding.signUpFNameTxt.error = result.e
                        }
                        ErrorMessage.ERROR_EMPTY_LNAME_MSG -> {
                            signInBinding.signUpLNameTxt.error = result.e
                        }
                        ErrorMessage.ERROR_EMAIL_IS_EMPTY -> {
                            signInBinding.signUpEmailTxt.error = result.e
                        }
                        ErrorMessage.ERROR_EMAIL_NOT_VALID_MSG -> {
                            signInBinding.signUpEmailTxt.error = result.e
                        }
                        ErrorMessage.ERROR_PASSWORD_IS_EMPTY -> {
                            signInBinding.signUpPasswordTxt.error = result.e
                        }
                        ErrorMessage.ERROR_PASSWORD_LENGTH_LESS_THAN_8 -> {
                            signInBinding.signUpPasswordTxt.error = result.e
                        }
                        ErrorMessage.ERROR_EMPTY_PHONE_MSG -> {
                            signInBinding.signUpPhoneTxt.error = result.e
                        }
                        ErrorMessage.ERROR_INVALID_PHONE_MSG -> {
                            signInBinding.signUpPhoneTxt.error = result.e
                        }
                    }
                    displayProgressbar(false)
                    displaySnackbar(result.e,R.color.Red_200)

                }
            }
        })
    }

    private fun displaySnackbar(message: String,color:Int) {
        Snackbar.make(signInBinding.parent, message, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(resources.getColor(color))
            .show()
    }

    private fun displayProgressbar(isDisplayed: Boolean) {
        signInBinding.progressCircular.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

}



/*val user = UserModel(
          firstName = "Aasd",
          lastName = "dasd",
          email = "ahmed@gmail.com",
          password = "123456789",
          phone = "dsadsad",
          type = "Customer",
      )*/
