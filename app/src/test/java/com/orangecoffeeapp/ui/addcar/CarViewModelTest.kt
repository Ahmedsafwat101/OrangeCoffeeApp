package com.orangecoffeeapp.ui.addcar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.orangecoffeeapp.MainCoroutineRule
import com.orangecoffeeapp.constants.ErrorMessage
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.repository.FakeAddCarRepositoryTest
import com.orangecoffeeapp.getOrAwaitValueTest
import com.orangecoffeeapp.ui.viewmodels.CarViewModel
import com.orangecoffeeapp.utils.DataState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CarViewModelTest{

    // I already check the validation of the input in AddCatFormUtilsTest under Test module

    private lateinit var carViewModel: CarViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        carViewModel = CarViewModel(FakeAddCarRepositoryTest())
    }


    @Test
    fun `add car is already exists return error`() {

        val car = CarModel(
            carName = "Car1",
            address = "Cairo,Maddi",
            latitude = 29.9601561,
            longitude = 31.2569138
        )

        carViewModel.addCarTest(car)

        val value = carViewModel.getCarStates().getOrAwaitValueTest()


        Truth.assertThat(value).isEqualTo(DataState.Error(ErrorMessage.ERROR_CAR_ALREADY_EXISTS_MSG))
    }

    @Test
    fun `add car is successfully return data`() {

        val car = CarModel(
            carName = "Car2",
            address = "Cairo,Maddi",
            latitude = 29.9601561,
            longitude = 31.2569138
        )

        carViewModel.addCarTest(car)

        val value = carViewModel.getCarStates().getOrAwaitValueTest()


        Truth.assertThat(value).isEqualTo(DataState.Success(car))
    }

}