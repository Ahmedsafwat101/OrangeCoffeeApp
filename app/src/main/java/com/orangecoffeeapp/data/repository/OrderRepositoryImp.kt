package com.orangecoffeeapp.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.orangecoffeeapp.constants.OrdersStatus
import com.orangecoffeeapp.data.models.Order
import com.orangecoffeeapp.data.models.UserModel
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class OrderRepositoryImp @Inject constructor(private val db: FirebaseFirestore) : OrderRepository {

    private val TAG = "OrderRepositoryImp"
    override suspend fun addOrder(currOrder: Order): Boolean {
        return try {
            db.collection("Orders").add(currOrder).await()
            true
        } catch (e: Exception) {
            Log.d(TAG, "error $e")
            false
        }
    }

    override suspend fun getOrdersForUser(userEamil: String): List<Order>? {

        return try {
            var userOrders: ArrayList<Order>? = null
            val snapShot = db.collection("Orders").whereEqualTo("userID", userEamil).get().await()
            snapShot?.let {
                userOrders = ArrayList()
                it.forEach { doc ->
                    userOrders?.add(doc.toObject(Order::class.java))
                    Log.d(TAG, "data$db")
                }
            }
            userOrders
        } catch (e: Exception) {
            Log.d(TAG,e.toString())
            null
        }
    }
    //.whereNotEqualTo("orderStatus",OrdersStatus.Delivered)

    override suspend fun getOrdersForCAR(carName: String): List<Order>? {
        return try {
            var carOrders: ArrayList<Order>? = null
            val snapShot = db.collection("Orders").whereEqualTo("carName", carName).get().await()
            snapShot?.let {
                carOrders = ArrayList()
                it.forEach { doc ->
                    carOrders?.add(doc.toObject(Order::class.java))
                    Log.d(TAG, "data$db")
                }
            }
            carOrders
        } catch (e: Exception) {
            null
        }
    }


}