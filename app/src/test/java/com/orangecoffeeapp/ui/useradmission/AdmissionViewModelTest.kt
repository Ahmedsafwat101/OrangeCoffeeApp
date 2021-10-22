package com.orangecoffeeapp.ui.useradmission

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.orangecoffeeapp.MainCoroutineRule
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_DONT_HAVE_ACCESS
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_EMAIL_NOT_FOUND_MSG
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_PASSWORD_WRONG_MSG
import com.orangecoffeeapp.data.models.LoginFormModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.data.repository.FakeAdmissionRepositoryImplTest
import com.orangecoffeeapp.getOrAwaitValueTest
import com.orangecoffeeapp.ui.viewmodels.AdmissionViewModel
import com.orangecoffeeapp.utils.DataState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class AdmissionViewModelTest {
    private lateinit var admissionViewModel: AdmissionViewModel


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        admissionViewModel = AdmissionViewModel(FakeAdmissionRepositoryImplTest())
    }


    @Test
    fun `signUp valid data field return data`() {
        val user = UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat3@gmail.com",
            "123456789",
            "01126638220",
            "Customer",
            "",
            true
        )


        admissionViewModel.signUpTest(user)


        val value = admissionViewModel.getUserStates().getOrAwaitValueTest()

        assertThat(value).isEqualTo(DataState.Success(user))

    }



    @Test
    fun `signUp with taken email return error`() {

        val user = UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat@gmail.com",
            "123456789",
            "01126638220",
            "Customer",
            "",
            true
        )

        admissionViewModel.signUpTest(user)

        val value = admissionViewModel.getUserStates().getOrAwaitValueTest()


        assertThat(value).isEqualTo(DataState.Error(ErrorMessage.EMAIL_ALREADY_EXISTS_MSG))
    }


    @Test
    fun `logIn with valid input field return data`() {


         val userLoginModel = LoginFormModel(
             "ahmedsafwat2@gmail.com",
             "123456789"
         )

        val user = UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat2@gmail.com",
            "123456789",
            "01126638220",
            "Customer",
            "",
            true
        )

        admissionViewModel.logInTest(userLoginModel)

        val value = admissionViewModel.getUserStates().getOrAwaitValueTest()

        assertThat(value).isEqualTo(DataState.Success(user))
    }


    @Test
    fun `logIn with no existing email return error`() {

        val userLoginModel = LoginFormModel(
            "ahmedsafwat3@gmail.com",
            "123456789"
        )

        admissionViewModel.logInTest(userLoginModel)

        val value = admissionViewModel.getUserStates().getOrAwaitValueTest()

        assertThat(value).isEqualTo(DataState.Error(ERROR_EMAIL_NOT_FOUND_MSG))
    }


   @Test
    fun `logIn with wrong password or email return error`() {


        val userLoginModel = LoginFormModel(
            "ahmedsafwat2@gmail.com",
            "12345896388"
        )

        admissionViewModel.logInTest(userLoginModel)

        val value = admissionViewModel.getUserStates().getOrAwaitValueTest()

        assertThat(value).isEqualTo(DataState.Error(ERROR_PASSWORD_WRONG_MSG))
    }


    @Test
    fun `edit User successfully return data`() {

        val retUser = UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat@gmail.com",
            "123456789",
            "01126638330",
            "Customer",
            "",
            true

        )

        admissionViewModel.updateTest(retUser)

        val value = admissionViewModel.getUserStates().getOrAwaitValueTest()


        assertThat(value).isEqualTo(DataState.Success(retUser))
    }


    @Test
    fun `edit User with no access return error`() {

        val retUser = UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat2@gmail.com",
            "123456789",
            "01126638330",
            "Customer",
            "",
            false

            )

        admissionViewModel.updateTest(retUser)

        val value = admissionViewModel.getUserStates().getOrAwaitValueTest()


        assertThat(value).isEqualTo(DataState.Error(ERROR_DONT_HAVE_ACCESS))
    }



}