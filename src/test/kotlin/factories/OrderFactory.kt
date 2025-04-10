package com.example.delivery.factories

import com.example.delivery.core.domain.model.order.Order
import com.example.delivery.factories.CourierFactory.freeCourier
import com.example.delivery.factories.LocationFactory.randomLocation
import java.util.*

object OrderFactory {
    fun unassignedOrder() = Order(UUID.randomUUID(), randomLocation())

    fun assignedOrder() = unassignedOrder().apply { assign(freeCourier()) }

    fun completedOrder() = assignedOrder().apply { complete() }
}
