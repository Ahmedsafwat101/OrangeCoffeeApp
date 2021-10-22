package com.orangecoffeeapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.MenuItemModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.utils.Hashing
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AddCarRepositoryImp @Inject constructor(private val db: FirebaseFirestore) :
    AddCarRepository {
    private val TAG = "AddCarRepositoryImp"

    override suspend fun addCarMenu(car: CarModel): CarModel? {
        val dbKey = car.carName
        val snapShot = db.collection("Cars").document(dbKey).get().await()
        Log.d(TAG, "add " + snapShot.data.toString())

        return if (!snapShot.exists()) {
            db.collection("Cars").document(dbKey).set(car).await()
            Log.d(TAG, "Add Done")
            car
        } else {
            null
        }
    }

    override suspend fun getCar(carName: String): CarModel? {


        val snapShot = db.collection("Cars").document(carName).get().await()
        Log.d(TAG, snapShot.data.toString())
        snapShot?.let {
            return it.toObject(CarModel::class.java)
        }
        return null // Car Not found
    }


    override suspend fun updateCarMenu(car: CarModel, menuItem: MenuItemModel): CarModel? {
        val snapShot = db.collection("Cars").document(car.carName).get().await()
        return if (snapShot.exists()) {
            db.collection("Cars").document(car.carName).set(car).await()
            car
        } else {
            null
        }
    }

    override suspend fun removeCar(car: CarModel): CarModel? {
        TODO("Not yet implemented")
    }
}