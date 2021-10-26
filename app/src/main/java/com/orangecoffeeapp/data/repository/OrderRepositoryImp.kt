package com.orangecoffeeapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.orangecoffeeapp.data.models.Order
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class OrderRepositoryImp @Inject constructor(private val db: FirebaseFirestore):OrderRepository {

    private  val TAG = "OrderRepositoryImp"
    override suspend fun addOrder(currOrder: Order): Boolean {
        return try {
            db.collection("Orders").add(currOrder).await()
            true
        }catch (e:Exception){
            Log.d(TAG,"error "+e.toString())
            false
        }
    }
}