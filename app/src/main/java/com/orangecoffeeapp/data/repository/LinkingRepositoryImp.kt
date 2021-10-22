package com.orangecoffeeapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.LinkedCarsWithOwners
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.utils.Hashing
import kotlinx.coroutines.tasks.await
import java.lang.Exception
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
                db.collection("LinkedCarsWithOwners").document(dbKey+car.carName).set(LinkedCarsWithOwners(carModel = car,userModel = owner)).await()
                db.collection("Users").document(dbKey).set(owner).await()
                db.collection("Cars").document(car.carName).set(car).await()
                return  true
            }
        return false
    }

    override suspend fun getLinkedCarWithOwner(): List<LinkedCarsWithOwners>? {
        var ans:ArrayList<LinkedCarsWithOwners>? = null
        val snapShot = db.collection("LinkedCarsWithOwners").get().await()
        snapShot?.let {
            ans = ArrayList()
            it.forEach { doc ->
                ans?.add(doc.toObject(LinkedCarsWithOwners::class.java))
                Log.d(TAG, "data$db")
            }
        }
        return ans
    }

    override suspend fun updateLinkedCarWithOwner(
        car: CarModel,
        owner: UserModel
    ): Boolean {

        val dbKey = Hashing.sha256(owner.email).toString() // hashed email
        return try{
            db.collection("Cars").document(car.carName).set(car).await()
            db.collection("LinkedCarsWithOwners").document(dbKey+car.carName).set(LinkedCarsWithOwners(car,owner)).await()
            true
        }catch (e:Exception){
            false
        }


    }


}