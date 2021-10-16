package com.orangecoffeeapp.ui.addcar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth
import com.orangecoffeeapp.MainCoroutineRule
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.data.repository.FakeAddCarRepositoryTest
import com.orangecoffeeapp.data.repository.FakeAdmissionRepositoryImplTest
import com.orangecoffeeapp.getOrAwaitValueTest
import com.orangecoffeeapp.ui.useradmission.AdmissionViewModel
import com.orangecoffeeapp.utils.admission.AdmissionState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class AddCarViewModelTest{

    // I already check the validation of the input in AddCatFormUtilsTest under Test module 

    private lateinit var addCarViewModel: AddCarViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        addCarViewModel = AddCarViewModel(FakeAddCarRepositoryTest())
    }


    @Test
    fun `add car is already exists return error`() {

        val car = CarModel(
            carName = "Car1",
            address = "Cairo,Maddi",
            location = LatLng(29.9601561,31.2569138)
        )

        addCarViewModel.addCarTest(car)

        val value = addCarViewModel.getCarStates().getOrAwaitValueTest()


        Truth.assertThat(value).isEqualTo(AdmissionState.Error(ErrorMessage.ERROR_CAR_ALREADY_EXISTS_MSG))
    }

    @Test
    fun `add car is successfully return data`() {

        val car = CarModel(
            carName = "Car2",
            address = "Cairo,Maddi",
            location = LatLng(29.9601561,31.2569138)
        )

        addCarViewModel.addCarTest(car)

        val value = addCarViewModel.getCarStates().getOrAwaitValueTest()


        Truth.assertThat(value).isEqualTo(AdmissionState.Success(car))
    }

}