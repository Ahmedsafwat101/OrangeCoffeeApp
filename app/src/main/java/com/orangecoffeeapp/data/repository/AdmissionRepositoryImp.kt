package com.orangecoffeeapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.data.models.LoginFormModel
import com.orangecoffeeapp.utils.Hashing
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


class AdmissionRepositoryImp @Inject constructor(private val db: FirebaseFirestore) :
    AdmissionRepository {
    private val TAG = "MyRepoImp"

    override suspend fun validateUserLogin(user: LoginFormModel): UserModel? {
        try {
            val hashedPassword = Hashing.sha256(user.password).toString()
            val dbKey = Hashing.sha256(user.email).toString() // hashed email
            val snapShot = db.collection("Users").document(dbKey).get().await()
            Log.d(TAG, snapShot.data.toString())
            snapShot?.let {
                return it.toObject(UserModel::class.java)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            return null
        }
        return null
    }

    override suspend fun validateUserSignUp(user: UserModel): UserModel? {
        val dbKey = Hashing.sha256(user.email).toString() // hashed email
        val snapShot = db.collection("Users").document(dbKey).get().
        await()
        Log.d(TAG, "add " + snapShot.data.toString())

        return if (!snapShot.exists()) {
            db.collection("Users").document(dbKey).set(user).await()
            Log.d(TAG, "Add Done")
            user
        } else {
            null
        }

    }

    override suspend fun updateUser(user: UserModel): UserModel? {

        val dbKey = Hashing.sha256(user.email).toString() // hashed email
        val snapShot = db.collection("Users").document(dbKey).get().await()
        return if(snapShot.exists()) {
            db.collection("Users").document(dbKey).set(user).await()
            user
        }else{
            null
        }
    }
}
