package com.orangecoffeeapp.ui.useradmission

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMAIL_IS_EMPTY
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMAIL_NOT_VALID_MSG
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_PASSWORD_IS_EMPTY
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_PASSWORD_LENGTH_LESS_THAN_8
import com.orangecoffeeapp.data.models.LoginFormModel
import com.orangecoffeeapp.databinding.FragmentLogInBinding
import com.orangecoffeeapp.utils.SharedPreferenceManager
import com.orangecoffeeapp.utils.admission.AdmissionState
import com.orangecoffeeapp.utils.admission.NavigateToActivity

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private val TAG = "LogInFragment"
    private val admissionViewModel: AdmissionViewModel by viewModels()

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

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {

        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if(isConnected)
            Log.d(TAG, "Connection $isConnected")
        else
            Log.d(TAG, "Connection false")

        // Access Views

        logInBinding.sigInTxt.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
        }

        subscribeObserver()

        //Check
        logInBinding.logInBtn.setOnClickListener {
            // If valid input return NONE
            it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }
           // Log.d(TAG, "else before" + isLogged().toString())


                admissionViewModel.logIn(
                    LoginFormModel(
                        logInBinding.logInEmailTxt.text.toString().trim(),
                        logInBinding.loginInPasswordTxt.text.toString().trim()
                    )
                )
            }
        }


    private fun subscribeObserver() {
        admissionViewModel.getUserStates().observe(viewLifecycleOwner, { result ->
            when (result){
                is AdmissionState.Success -> {
                    displayProgressbar(false)
                    //save in SharedPreference
                   SharedPreferenceManager(requireActivity()).saveSharedPreferenceData(result.data)
                   startActivity(NavigateToActivity.moveToHomeActivity(result.data.type,requireActivity())) // Move to another activity
                }
                is AdmissionState.Loading -> {
                    displayProgressbar(true)
                }
                is AdmissionState.Error -> {
                    displayProgressbar(false)
                    when (result.e) {
                        ERROR_EMAIL_IS_EMPTY -> {
                            logInBinding.logInEmailTxt.error = result.e
                        }
                        ERROR_EMAIL_NOT_VALID_MSG -> {
                            logInBinding.logInEmailTxt.error = result.e
                        }
                        ERROR_PASSWORD_IS_EMPTY -> {
                            logInBinding.loginInPasswordTxt.error = result.e
                        }
                        ERROR_PASSWORD_LENGTH_LESS_THAN_8 -> {
                            logInBinding.loginInPasswordTxt.error = result.e
                        }

                    }
                    displaySnackbar(result.e)
                    Log.d("here", "Error ${result.e}")
                }
                else -> {
                    Log.d(TAG,"")
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


}