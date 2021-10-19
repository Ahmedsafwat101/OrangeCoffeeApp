package com.orangecoffeeapp.data.repository

import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.utils.Hashing
import kotlinx.coroutines.tasks.await

class FakeLinkingRepositoryImpTest : LinkingRepository {

    private var fakeCarsDB = mutableMapOf<String, CarModel>(
        "Car1" to CarModel(
            carName = "Car1",
            address = "Cairo,Maddi",
            latitude = 29.9601561,
            longitude = 31.2569138
        ), "Car2" to CarModel(
            carName = "Car1",
            address = "Cairo,Maddi",
            latitude = 29.9601561,
            longitude = 31.2569138,
            carID = "185174"
        )
    )

    private var fakeUsersDB = mutableMapOf<String, UserModel>(
        Hashing.sha256("ahmedsafwat@gmail.com").toString() to UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat@gmail.com",
            "123456789",
            "01126638220",
            "Owner",
            "",
            true
        ),
        Hashing.sha256("ahmedsafwat1@gmail.com").toString() to UserModel(
            "Ahmed",
            "Safwat",
            "ahmedsafwat1@gmail.com",
            "123456789",
            "01126638220",
            "Customer",
            "",
            true
        )
    )

    override suspend fun getAllOwners(): List<UserModel>? {
        var ownersList: ArrayList<UserModel> = ArrayList()
        if (fakeUsersDB.values.isNotEmpty() ) {
            for (owner in fakeUsersDB.values) {
                if(owner.type=="Owner")
                      ownersList.add(owner)
            }
            return if(ownersList.isEmpty()) null else ownersList
        }
        return null
    }

    override suspend fun getAllCars(): List<CarModel>? {
        var carsList: ArrayList<CarModel> = ArrayList()
        if (fakeCarsDB.values.isNotEmpty() ) {
            for (car in fakeCarsDB.values) {
                if(car.carID == "")
                    carsList.add(car)
            }
            return if(carsList.isEmpty()) null else carsList
        }
        return null
    }

    override suspend fun linkOwnerAndCar(owner: UserModel, car: CarModel): Boolean {
        val dbKey = Hashing.sha256(owner.email).toString() // hashed email
        if (fakeCarsDB.containsKey(car.carName) && fakeUsersDB.containsKey(dbKey)) {
            owner.carID = car.carName
            car.ownerID = dbKey
            fakeUsersDB[dbKey] = owner
            fakeCarsDB[car.carName] = car
            return  true
        }
        return false
    }

}