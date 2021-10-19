package com.orangecoffeeapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.utils.Hashing
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LinkingRepositoryImp @Inject constructor(private val db: FirebaseFirestore) :
    LinkingRepository {
    private  val TAG = "GeUserRepositoryImp"
    override suspend fun getAllOwners(): List<UserModel>? {
        var ans:ArrayList<UserModel>? = null
        val snapShot = db.collection("Users").whereEqualTo("type","Owner").whereEqualTo("carID","").get().await()
        snapShot?.let {
            ans = ArrayList()
            it.forEach { doc ->
                ans?.add(doc.toObject(UserModel::class.java))
                Log.d(TAG, "data$db")
            }
        }
        return ans
    }

    override suspend fun getAllCars(): List<CarModel>? {
        var ans:ArrayList<CarModel>? = null
        val snapShot = db.collection("Cars").whereEqualTo("ownerID","").get().await()
        snapShot?.let {
            ans = ArrayList()
            it.forEach { doc ->
                ans?.add(doc.toObject(CarModel::class.java))
                Log.d(TAG, "data$db")
            }
        }
        return ans
    }

    override suspend fun linkOwnerAndCar(owner: UserModel, car: CarModel): Boolean {
        val dbKey = Hashing.sha256(owner.email).toString() // hashed email
            val snapShot1 = db.collection("Users").document(dbKey).get().await()
            val snapShot2 = db.collection("Cars").document(car.carName).get().await()

            if (snapShot1.exists() && snapShot2.exists()) {
                owner.carID = car.carName
                car.ownerID = dbKey
                db.collection("Users").document(dbKey).set(owner).await()
                db.collection("Cars").document(car.carName).set(car).await()
                return  true
            }
        return false
    }


}