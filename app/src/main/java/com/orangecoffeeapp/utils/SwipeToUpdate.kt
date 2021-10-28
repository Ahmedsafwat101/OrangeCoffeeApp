package com.orangecoffeeapp.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.orangecoffeeapp.constants.OrdersStatus
import com.orangecoffeeapp.data.models.Order
import kotlin.properties.Delegates

abstract class SwipeToUpdate(context: Context, var ordersData: ArrayList<Order>) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val background = ColorDrawable()
    private var backgroundColor by Delegates.notNull<Int>()
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    private lateinit var textMsg:String

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        val pos = viewHolder.layoutPosition

        val currOrder = ordersData[pos]


        when (currOrder.orderStatus) {
            OrdersStatus.Pending ->{
                textMsg = OrdersStatus.Processing.toString()
                backgroundColor = Color.parseColor("#F7CB73")
            }
            OrdersStatus.Processing ->{
                textMsg = OrdersStatus.Delivered.toString()
                backgroundColor = Color.parseColor("#077E8C")
            }
            OrdersStatus.Delivered ->{
                textMsg =""
                backgroundColor = Color.parseColor("#00ffffff")
            }
        }

        // Draw the red delete background
        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)


        val deleteIconTop = itemView.top + (itemHeight) / 2
        val deleteIconMargin = (itemHeight ) / 2
        val deleteIconRight = itemView.right - deleteIconMargin


        clearPaint.textSize = 50f;
        c.drawText(textMsg, deleteIconRight.toFloat(), deleteIconTop.toFloat(), clearPaint)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}

