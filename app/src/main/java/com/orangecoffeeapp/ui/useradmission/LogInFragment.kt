package com.orangecoffeeapp.ui.useradmission

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMAIL_IS_EMPTY
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMAIL_NOT_VALID_MSG
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_PASSWORD_IS_EMPTY
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_PASSWORD_LENGTH_LESS_THAN_8
import com.orangecoffeeapp.constants.ErrorMessage.NONE
import com.orangecoffeeapp.constants.UserTypes
import com.orangecoffeeapp.data.models.LoginFormModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentLogInBinding
import com.orangecoffeeapp.utils.admission.AdmissionState
import com.orangecoffeeapp.utils.admission.LoginFormUtils
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private val TAG = "LogInFragment"
    private val admissionViewModel: AdmissionViewModel by viewModels()
    //  private var editor = loginSharedPref?.edit()

    private lateinit var logInBinding: FragmentLogInBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        logInBinding = FragmentLogInBinding.inflate(inflater, container, false)
        return logInBinding.root
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        //Applying Shared Presence
        if (isLogged()) {
            //val currUser = getSharedPreferenceData()
            //navigateToHomeScreen(currUser.type)
        }

        subscribeObserver()

        // Access Views

        logInBinding.sigInTxt.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }

        //Check
        logInBinding.logInBtn.setOnClickListener {
            // If valid input return NONE
            it?.apply { isEnabled = false; postDelayed({isEnabled=true},400) }
            Log.d(TAG,"else before"+isLogged().toString())
            if (setInputError() == NONE) {

                admissionViewModel.viewModelScope.launch {
                    admissionViewModel.logIn(
                        LoginFormModel(
                            logInBinding.logInEmailTxt.text.toString().trim(),
                            logInBinding.loginInPasswordTxt.text.toString().trim()
                        )
                    )
                }
            }
        }
    }

    private fun setInputError(): String {
        val result = LoginFormUtils.validateLoginForm(
            logInBinding.logInEmailTxt.text.toString(),
            logInBinding.loginInPasswordTxt.text.toString()
        )
        if (result != NONE) {
            when (result) {
                ERROR_EMAIL_IS_EMPTY -> {
                    logInBinding.logInEmailTxt.error = result
                }
                ERROR_EMAIL_NOT_VALID_MSG -> {
                    logInBinding.logInEmailTxt.error = result
                }
                ERROR_PASSWORD_IS_EMPTY -> {
                    logInBinding.loginInPasswordTxt.error = result
                }
                ERROR_PASSWORD_LENGTH_LESS_THAN_8 -> {
                    logInBinding.loginInPasswordTxt.error = result
                }
            }
            displaySnackbar(result)
        }
        return result
    }

    private fun subscribeObserver(){
        admissionViewModel.getUser().observe(viewLifecycleOwner,{ result ->
            when(result){
                is AdmissionState.Success->{
                    displayProgressbar(false)
                    //save in SharedPreference
                    saveSharedPreferenceData(result.data)
                    when (result.data.type) {
                        UserTypes.Admin -> findNavController().navigate(R.id.action_logInFragment_to_adminHomeFragment)
                        UserTypes.Customer -> findNavController().navigate(R.id.action_logInFragment_to_customerHomeFragment)
                        UserTypes.Owner -> findNavController().navigate(R.id.action_logInFragment_to_ownerHomeFragment)
                    }

                }
                is AdmissionState.Loading->{
                    displayProgressbar(true)
                }
                is AdmissionState.Error->{
                    displayProgressbar(false)
                    Log.d("here", "Error ${result.e}")
                    displaySnackbar(result.e)
                }
            }
        })
    }


    private fun displayProgressbar(isDisplayed: Boolean) {
        logInBinding.progressCircular.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }


    private fun displaySnackbar(error: String) {
        Snackbar.make(logInBinding.parent, error, Snackbar.LENGTH_LONG)
            .setAction("CLOSE") { }
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
            .show()
    }

    private fun isLogged(): Boolean {
        val loginSharedPref = requireActivity().getSharedPreferences("loginRPref", Context.MODE_PRIVATE)
        return  loginSharedPref.getBoolean("logged", false)
    }

    private fun saveSharedPreferenceData(data: UserModel){
        var loginSharedPref = requireActivity().getSharedPreferences("loginRPref", Context.MODE_PRIVATE)
        val editor = loginSharedPref.edit()
        editor.apply {
            putString("fName",data.firstName)
            putString("lName",data.lastName)
            putString("email",data.email)
            putString("password",data.password)
            putString("phone",data.phone)
            putBoolean("access",data.access)
            putString("type",data.type)
            putBoolean("logged",true)
        }.apply()
    }


    private fun getSharedPreferenceData(): UserModel {
        val loginSharedPref = requireActivity().getSharedPreferences("loginRPref", Context.MODE_PRIVATE)

        return UserModel(
            firstName = loginSharedPref?.getString("fName", null).toString(),
            lastName = loginSharedPref?.getString("lName", null).toString(),
            email = loginSharedPref?.getString("email", null).toString(),
            phone = loginSharedPref?.getString("phone", null).toString(),
            access = loginSharedPref.getBoolean("access",false),
            type = loginSharedPref?.getString("type", null).toString(),
            password = loginSharedPref?.getString("password", null).toString(),
        )
    }


    private fun navigateToHomeScreen(type:String){
        Log.d(TAG,"else navigateToHomeScreen"+type)
        when (type) {
            UserTypes.Admin ->{
                Log.d(TAG,"else navigateToHomeScreen in in"+type)

                findNavController().navigate(R.id.action_logInFragment_to_adminHomeFragment)}
            UserTypes.Customer -> findNavController().navigate(R.id.action_logInFragment_to_customerHomeFragment)
            UserTypes.Owner -> findNavController().navigate(R.id.action_logInFragment_to_ownerHomeFragment)
            else->{
                Log.d(TAG,"else nav"+type)

            }
        }
    }
}