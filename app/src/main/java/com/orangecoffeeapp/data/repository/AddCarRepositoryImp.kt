package com.orangecoffeeapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.utils.Hashing
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class AddCarRepositoryImp @Inject constructor(private val db: FirebaseFirestore):AddCarRepository {
    private  val TAG = "AddCarRepositoryImp"

    override suspend fun addCarToDB(car: CarModel): CarModel? {
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

    override suspend fun removeCarFromBB(car: CarModel): CarModel? {
        TODO("Not yet implemented")
    }

    override suspend fun updateCarFromDB(car: CarModel): CarModel? {
        TODO("Not yet implemented")
    }
}