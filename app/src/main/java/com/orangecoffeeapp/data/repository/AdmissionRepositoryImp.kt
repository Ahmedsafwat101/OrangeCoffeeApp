package com.orangecoffeeapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.data.models.LoginFormModel
import com.orangecoffeeapp.utils.Hashing
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject


class AdmissionRepositoryImp @Inject constructor(private val db:FirebaseFirestore):
    AdmissionRepository {
    private  val TAG = "MyRepoImp"
    override suspend fun validateUserLogin(user:LoginFormModel): UserModel? {
        try {
            val hashedPassword = Hashing.sha256(user.password).toString()
            val dbKey = Hashing.sha256(user.email).toString() // hashed email
            val snapShot = db.collection("Users").document(dbKey).get().await()
            snapShot?.let {
                return it.toObject(UserModel::class.java)
            }

        }catch (e: Exception){
            Log.e(TAG, "Error: ${e.message}")
            return null
        }
        return null
    }

    override suspend fun validateUserSignIn(user: UserModel): UserModel? {
        TODO("Not yet implemented")
    }



}
