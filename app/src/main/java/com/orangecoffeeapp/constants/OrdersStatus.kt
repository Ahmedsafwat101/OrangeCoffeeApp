package com.orangecoffeeapp.constants

import java.io.Serializable

enum class OrdersStatus:Serializable {
    Delivered,
    Pending,
    Processing,
    Cancelled
}