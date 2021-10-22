package com.orangecoffeeapp.ui.addcar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.orangecoffeeapp.MainCoroutineRule
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_NETWORK_ERROR
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_NO_CAR_FOUND
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_NO_OWNERS_FOUND
import com.orangecoffeeapp.constants.ErrorMessage.ERROR_OWNER_OR_CAR_NOT_EXISTS
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.data.repository.FakeLinkingRepositoryImpTest
import com.orangecoffeeapp.getOrAwaitValueTest
import com.orangecoffeeapp.ui.viewmodels.LinkingViewModel
import com.orangecoffeeapp.utils.DataState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LinkingViewModelTest {

    private lateinit var linkingViewModel: LinkingViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        linkingViewModel = LinkingViewModel(FakeLinkingRepositoryImpTest())
    }


    @Test
    fun `get all owners in the db return Data`() {
       val  ret = listOf(UserModel(
           "Ahmed",
           "Safwat",
           "ahmedsafwat@gmail.com",
           "123456789",
           "01126638220",
           "Owner",
           "",
           true
       ) )
        linkingViewModel.getAllOwnerTest()
        val value = linkingViewModel.getOwnersState().getOrAwaitValueTest()
        assertThat(value).isEqualTo(DataState.Success(ret))
    }


    @Test
    fun `no owners are found in the db return error`(){
        linkingViewModel.getAllOwnerTestReturnErrorNoOwners()
        val value = linkingViewModel.getOwnersState().getOrAwaitValueTest()
        assertThat(value).isEqualTo(DataState.Error(ERROR_NO_OWNERS_FOUND))

    }


    @Test
    fun `couldn't get data from  owners data from db return error`(){
        linkingViewModel.getAllOwnerTestReturnNetworkError()
        val value = linkingViewModel.getOwnersState().getOrAwaitValueTest()
        assertThat(value).isEqualTo(DataState.Error(ERROR_NETWORK_ERROR))
    }

    @Test
    fun `get all cars in the db return Data`() {
        val  ret = listOf(CarModel(
            carName = "Car1",
            address = "Cairo,Maddi",
            latitude = 29.9601561,
            longitude = 31.2569138
        ) )
        linkingViewModel.getAllCarsTest()
        val value = linkingViewModel.getCarsState().getOrAwaitValueTest()
        assertThat(value).isEqualTo(DataState.Success(ret))
    }


    @Test
    fun `no cars are found in the db return error`(){
        linkingViewModel.getAllCarsTestReturnErrorNoCars()
        val value = linkingViewModel.getCarsState().getOrAwaitValueTest()
        assertThat(value).isEqualTo(DataState.Error(ERROR_NO_CAR_FOUND))

    }

    @Test
    fun `couldn't get data from  cars data from db return error`(){
        linkingViewModel.getAllCarsTestReturnNetworkError()
        val value = linkingViewModel.getCarsState().getOrAwaitValueTest()
        assertThat(value).isEqualTo(DataState.Error(ERROR_NETWORK_ERROR))
    }

    @Test
    fun `link car and owner return data`(){
        val owner = UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat@gmail.com",
            "123456789",
            "01126638220",
            "Owner",
            "",
            true
        )
        val car =   CarModel(
            carName = "Car1",
            address = "Cairo,Maddi",
            latitude = 29.9601561,
            longitude = 31.2569138
        )
        linkingViewModel.linkTest(owner,car)
        val value = linkingViewModel.getLinkingState().getOrAwaitValueTest()
        assertThat(value).isEqualTo(DataState.Success(true))
    }


    @Test
    fun `link invalid car and owner return error`(){
        val owner = UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat@gmail.com",
            "123456789",
            "01126638220",
            "Owner",
            "",
            true
        )
        val car =   CarModel(
            carName = "Car3",
            address = "Cairo,Maddi",
            latitude = 29.9601561,
            longitude = 31.2569138
        )
        linkingViewModel.linkTest(owner,car)
        val value = linkingViewModel.getLinkingState().getOrAwaitValueTest()
        assertThat(value).isEqualTo(DataState.Error(ERROR_OWNER_OR_CAR_NOT_EXISTS))
    }


    @Test
    fun `link  car and invalid owner return error`(){
        val owner = UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat3@gmail.com",
            "123456789",
            "01126638220",
            "Owner",
            "",
            true
        )
        val car =   CarModel(
            carName = "Car1",
            address = "Cairo,Maddi",
            latitude = 29.9601561,
            longitude = 31.2569138
        )
        linkingViewModel.linkTest(owner,car)
        val value = linkingViewModel.getLinkingState().getOrAwaitValueTest()
        assertThat(value).isEqualTo(DataState.Error(ERROR_OWNER_OR_CAR_NOT_EXISTS))
    }

    //linkTestReturnNetworkError


}