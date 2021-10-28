package com.orangecoffeeapp.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.OrdersStatus
import com.orangecoffeeapp.data.models.Order
import com.orangecoffeeapp.databinding.RecyclerOrderStatusItemBinding
import java.lang.StringBuilder

class OrderStatusAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var orders = ArrayList<Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrdersViewHolder(
            RecyclerOrderStatusItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OrdersViewHolder -> holder.bind(orders[position])
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    //Custom class to describe your view
    class OrdersViewHolder
    constructor(private val binding: RecyclerOrderStatusItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {

            var stringTemplate = StringBuilder()
            val orderValues = order.orderContentValues
            val orderNames = order.orderContentKeys //List of Objects {Name,Ingredients}

            orderNames.forEachIndexed { index, _ ->
                var coffeeName = orderNames[index].coffeeName
                var count = orderValues[index].toString()
                stringTemplate.append(coffeeName+" "+ count + "x Cups \n")
            }

            binding.orderContentTxt.text = stringTemplate.toString().trim()
            binding.priceContentTxt.text = order.orderPrice.toString()+" EG"
            binding.status.text = order.orderStatus.toString()
            when (order.orderStatus) {
                OrdersStatus.Pending -> binding.status.setChipBackgroundColorResource( R.color.RoyalOrange)
                OrdersStatus.Processing -> binding.status.setChipBackgroundColorResource( R.color.Topaz)
                OrdersStatus.Delivered -> binding.status.setChipBackgroundColorResource( R.color.MetallicSeaweed)
            }

        }
    }


    fun submitList(ordersList: List<Order>) {
        orders = ordersList as ArrayList<Order>
    }


}
